package org.catcom.classreserver.service;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.catcom.classreserver.exceptions.reservations.InvalidReservationStateException;
import org.catcom.classreserver.exceptions.reservations.ReservationException;
import org.catcom.classreserver.exceptions.reservations.ReservationNotFoundException;
import org.catcom.classreserver.exceptions.reservations.ReservationScheduleOverlapException;
import org.catcom.classreserver.model.building.Building;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.reservation.Reservation;
import org.catcom.classreserver.model.reservation.ReservationRepos;
import org.catcom.classreserver.model.reservation.ReservationStatus;
import org.catcom.classreserver.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.catcom.classreserver.model.reservation.ReservationStatus.*;
import static org.catcom.classreserver.model.reservation.ReservationRepos.*;

@Service
public class ReservationService
{

    private Logger log = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepos reservationRepos;

    @Autowired
    private ClassroomService classroomService;

    // User requests a reservation
    public void reserve(
            @NonNull User reserver,
            @NonNull Classroom room,
            @NonNull LocalDateTime bookingTime,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime finishTime,
            @Nullable String reserveNote
    ) throws ReservationException
    {

        validateReservedRoom(room);
        validateBookingSchedule(bookingTime, startTime, finishTime);
        validateRoomSchedule(room, startTime, finishTime);
        validateUserSchedule(reserver, room, startTime, finishTime);

        var reservation = new Reservation();
        reservation.setOwner(reserver);
        reservation.setRoom(room);
        reservation.setBookingTime(bookingTime);
        reservation.setStartTime(startTime);
        reservation.setFinishTime(finishTime);
        reservation.setReserveNote(reserveNote);
        reservation.setPending();
        reservationRepos.save(reservation);

    }

    // Find reservations with various filters
    public List<Reservation> findReservations(
        @Nullable User owner,
        @Nullable Building building,
        @Nullable Classroom classroom,
        @Nullable String status,
        @Nullable LocalDateTime minReserveTime,
        @Nullable LocalDateTime maxReserveTime,
        @Nullable Integer limit
    ) throws ReservationException
    {

        if (status != null && !ReservationStatus.isValid(status))
        {
            throw new ReservationException("Unknown reservation status: " + status);
        }

        var specs = new ArrayList<Specification<Reservation>>();

        if (owner != null)
        {
            specs.add(hasOwner(owner));
        }

        if (building != null)
        {
            specs.add(inBuilding(building));
        }

        if (classroom != null)
        {
            specs.add(forRoom(classroom));
        }

        if (status != null)
        {
            specs.add(hasStatus(status));
        }

        if (minReserveTime != null || maxReserveTime != null)
        {
            specs.add(scheduleDuring(minReserveTime, maxReserveTime));
        }

        var spec = Specification.allOf(specs);

        var results = reservationRepos.findAll(spec);

        return limit == null ? results : results.subList(0, Math.min(limit, results.size()));
    }


    // Find reservation by its id
    public Reservation getReservation(int id) throws ReservationNotFoundException
    {
        var reservation = reservationRepos.findById(id);
        if (reservation.isEmpty())
        {
            throw new ReservationNotFoundException("The requested reservation does not exist");
        }

        return reservation.get();
    }

    // Update a reservation details except status
    public void updateReservation(
            int id,
            @Nullable Classroom newRoom,
            @Nullable LocalDateTime newStartTime,
            @Nullable LocalDateTime newFinishTime,
            @Nullable String newReserveNote
    ) throws ReservationException
    {

        var reservation = getReservation(id);

        var requireScheduleValidated = false;

        if (newRoom != null)
        {
            validateReservedRoom(newRoom);
            reservation.setRoom(newRoom);
            requireScheduleValidated = true;
        }

        if (newStartTime != null)
        {
            reservation.setStartTime(newStartTime);
            requireScheduleValidated = true;
        }

        if (newFinishTime != null)
        {
            reservation.setStartTime(newFinishTime);
            requireScheduleValidated = true;
        }

        if (newReserveNote != null)
        {
            reservation.setReserveNote(newReserveNote);
        }

        if (requireScheduleValidated)
        {
            validateRoomSchedule(reservation.getRoom(), reservation.getStartTime(), reservation.getFinishTime());
        }


        reservationRepos.save(reservation);

    }

    private static final Duration MAX_CANCELABLE_DURATION = Duration.of(12, ChronoUnit.HOURS);

    // Update reservation status
    public void updateReservationStatus(
            int id,
            @NonNull String status,
            @NonNull User updater,
            @Nullable String statusNote
    )
    {

        var reservation = getReservation(id);

        validateStatusChange(reservation, status);

        var now = LocalDateTime.now();

        // additional check to ensure no approved schedule can be overlapped
        if (APPROVED.equalsIgnoreCase(status))
        {
            validateRoomSchedule(reservation.getRoom(), reservation.getStartTime(), reservation.getFinishTime());
        }
        else if (CANCELED.equalsIgnoreCase(status))
        {
            var duration = Duration.between(now, reservation.getBookingTime());
            if (duration.compareTo(MAX_CANCELABLE_DURATION) > 0)
            {
                throw new ReservationException("Only reservation made withing 12 hours can be canceled.");
            }
        }

        reservation.setStatus(status);
        reservation.setApprover(updater);
        reservation.setApproveNote(statusNote);
        reservation.setApproveTime(now);

        reservationRepos.save(reservation);

        // automatically reject all other overlapping schedules
        if (reservation.isApproved())
        {
            rejectAllOverlapSchedule(reservation);

        }

    }

    // Delete a reservation
    public void deleteReservation(int id, User deleter)
    {
        var reservation = getReservation(id);

        if (reservation.isApproved())
            throw new ReservationException("Approved reservations cannot be deleted");

        if (!reservation.getOwner().equals(deleter))
            throw new ReservationException("Only owner can delete their reservation");

        reservationRepos.delete(reservation);
    }

    // Check if a given schedule overlapped with another approved reservation of the given room
    public boolean isRoomHasOverlapSchedule(Classroom classroom, LocalDateTime startTime, LocalDateTime finishTime)
    {
        if (!classroom.isReady()) return false;

        var overlapped = reservationRepos.findOne(
                hasStatus(APPROVED).and(overlappingScheduleForRoom(classroom, startTime, finishTime))
        );

        return overlapped.isPresent();
    }

    // Check if a given schedule overlapped with another non-rejected reservation of the given user
    public boolean isUserHasOverlapSchedule(User user, Classroom classroom, LocalDateTime startTime, LocalDateTime finishTime)
    {
        var overlapped = reservationRepos.findOne(
                hasOwner(user).and(hasStatus(PENDING).or(hasStatus(APPROVED))).and(overlappingScheduleForRoom(classroom, startTime, finishTime))
        );

        return overlapped.isPresent();
    }


    void validateReservedRoom(
            @Nullable Classroom room
    ) throws ReservationException
    {
        if (room == null)
        {
            throw new IllegalArgumentException("The requested room does not exist");
        }

        if (!room.isReady())
        {
            throw new ReservationException("The requested room is not ready to be used yet");
        }

    }


    private static final Duration MIN_BOOKING_ADVANCE_DURATION = Duration.of(3, ChronoUnit.DAYS);

    private void validateBookingSchedule(LocalDateTime bookingTime, LocalDateTime startTime, LocalDateTime finishTime)
    {
        if (startTime.isAfter(finishTime))
        {
            throw new ReservationException("Invalid reservation time range");
        }

        var duration = Duration.between(bookingTime, startTime);

        if (duration.compareTo(MIN_BOOKING_ADVANCE_DURATION) < 0)
        {
            throw new ReservationException("The reservation must be requested in advance for at least " + MIN_BOOKING_ADVANCE_DURATION);
        }
    }

    void validateRoomSchedule(
            @NonNull Classroom classroom,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime finishTime
    ) throws ReservationScheduleOverlapException
    {
        if (isRoomHasOverlapSchedule(classroom, startTime, finishTime) || !classroom.isReady()) {
            throw new ReservationScheduleOverlapException("The requested room is not available for the given time");
        }
    }

    void validateUserSchedule(
            @NonNull User user,
            @NonNull Classroom classroom,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime finishTime
    ) throws ReservationScheduleOverlapException
    {
        if (isUserHasOverlapSchedule(user, classroom, startTime, finishTime) || !classroom.isReady()) {
            throw new ReservationScheduleOverlapException("The user has already requested a reservation whose schedule overlapped with the current request");
        }
    }

    void validateStatusChange(
            @NonNull Reservation reservation,
            @NonNull String newStatus
    ) throws ReservationException
    {
        switch (newStatus)
        {
            case PENDING -> {
                throw new InvalidReservationStateException("Cannot set reservation status to pending");
            }
            case CANCELED ->
            {
                if (reservation.isPending()) return;

                throw new InvalidReservationStateException("Only pending reservation can be canceled");
            }
            case APPROVED, REJECTED ->
            {
                if (reservation.isPending()) return;
                throw new InvalidReservationStateException("Only pending reservation can be either accepted or rejected");
            }
            default -> throw new ReservationException("Unknown reservation status: " + newStatus);
        }

    }

    void rejectAllOverlapSchedule(@NonNull Reservation reservation)
    {

        var overlaps = reservationRepos.findAll(
                except(reservation).and(hasStatus(PENDING)).and(overlappingScheduleForRoom(reservation.getRoom(), reservation.getStartTime(), reservation.getFinishTime()))
        );

        for (var r : overlaps) {
            r.setStatus(REJECTED);
        }

        reservationRepos.saveAll(overlaps);
    }

}
