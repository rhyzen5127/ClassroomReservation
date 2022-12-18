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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.catcom.classreserver.model.reservation.ReservationStatus.*;

@Service
public class ReservationService
{

    private Logger log = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepos reservationRepos;

    @Autowired
    private ClassroomService classroomService;

    public void reserve(
            @NonNull User reserver,
            @NonNull Classroom room,
            @NonNull LocalDateTime bookingTime,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime finishTime
    ) throws ReservationException
    {

        validateReservedRoom(room);
        validateBookingSchedule(bookingTime, startTime, finishTime);
        validateRoomSchedule(room, startTime, finishTime);

        var reservation = new Reservation();
        reservation.setOwner(reserver);
        reservation.setRoom(room);
        reservation.setBookingTime(bookingTime);
        reservation.setStartTime(startTime);
        reservation.setFinishTime(finishTime);

        if (reserver.isStaff())
        {
            reservation.setApproved();
        }
        else
        {
            reservation.setPending();
        }

        reservationRepos.save(reservation);

    }

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
            specs.add(ReservationRepos.hasOwner(owner));
        }

        if (building != null)
        {
            specs.add(ReservationRepos.inBuilding(building));
        }

        if (classroom != null)
        {
            specs.add(ReservationRepos.forRoom(classroom));
        }

        if (status != null)
        {
            specs.add(ReservationRepos.hasStatus(status));
        }

        if (minReserveTime != null || maxReserveTime != null)
        {
            specs.add(ReservationRepos.scheduleDuring(minReserveTime, maxReserveTime));
        }

        var spec = Specification.allOf(specs);

        var results = reservationRepos.findAll(spec);


        return limit == null ? results : results.subList(0, limit);
    }


    public Reservation getReservation(int id) throws ReservationNotFoundException
    {
        var reservation = reservationRepos.findById(id);
        if (reservation.isEmpty())
        {
            throw new ReservationNotFoundException("The requested reservation does not exist");
        }

        return reservation.get();
    }

    public void updateReservation(
            int id,
            @Nullable Classroom newRoom,
            @Nullable LocalDateTime newStartTime,
            @Nullable LocalDateTime newFinishTime
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

        if (requireScheduleValidated)
        {
            validateRoomSchedule(reservation.getRoom(), reservation.getStartTime(), reservation.getFinishTime());
        }


        reservationRepos.save(reservation);

    }

    public void updateReservationStatus(int id, String status)
    {
        var reservation = getReservation(id);

        validateStatus(reservation, status);

        reservation.setStatus(status);

        if (reservation.isApproved())
        {
            validateRoomSchedule(reservation.getRoom(), reservation.getStartTime(), reservation.getFinishTime());

            // automatically reject all other overlapping schedules
            var specs = Specification.allOf(
                    ReservationRepos.except(reservation),
                    ReservationRepos.hasStatus(PENDING),
                    ReservationRepos.overlappingScheduleForRoom(reservation.getRoom(), reservation.getStartTime(), reservation.getFinishTime())
            );

            var overlappingReservations = reservationRepos.findAll(specs);

            for (var r : overlappingReservations) {
                r.setStatus(REJECTED);
            }

            reservationRepos.saveAll(overlappingReservations);
        }

        reservationRepos.save(reservation);
    }

    public void deleteReservation(int id, User deleter)
    {
        var reservation = getReservation(id);

        if (!reservation.isRejected())
            throw new ReservationException("Only rejected or pending reservations can be deleted");

        if (!reservation.getOwner().equals(deleter))
            throw new ReservationException("Only owner can delete their reservation");

        reservationRepos.delete(reservation);
    }

    public boolean isRoomAvailableAtGivenSchedule(Classroom classroom, LocalDateTime startTime, LocalDateTime finishTime)
    {
        if (!classroom.isReady()) return false;

        var specs = Specification.allOf(
                ReservationRepos.hasStatus(APPROVED),
                ReservationRepos.overlappingScheduleForRoom(classroom, startTime, finishTime)
        );

        var overlapped = reservationRepos.findAll(specs);
        return overlapped.isEmpty();
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

    // private static final Duration MIN_BOOKING_ADVANCE_DURATION = Duration.of(1, ChronoUnit.DAYS);

    private void validateBookingSchedule(LocalDateTime bookingTime, LocalDateTime startTime, LocalDateTime finishTime)
    {
        if (startTime.isAfter(finishTime))
        {
            throw new ReservationException("Invalid reservation time range");
        }

        //var duration = Duration.between(bookingTime, startTime);

        //if (duration.compareTo(MIN_BOOKING_ADVANCE_DURATION) < 0)
        //{
        //    throw new ReservationException("The reservation must be requested in advance for at least " + MIN_BOOKING_ADVANCE_DURATION);
        //}
    }

    void validateRoomSchedule(
            @NonNull Classroom classroom,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime finishTime
    ) throws ReservationScheduleOverlapException
    {
        if (!isRoomAvailableAtGivenSchedule(classroom, startTime, finishTime) || !classroom.isReady()) {
            throw new ReservationScheduleOverlapException("The requested room is not available for the given time");
        }
    }

    void validateStatus(
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
                if (reservation.isApproved() || reservation.isPending()) return;
                throw new InvalidReservationStateException("Only accepted and pending reservation can be canceled");
            }
            case APPROVED, REJECTED ->
            {
                if (reservation.isPending()) return;
                throw new InvalidReservationStateException("Only pending reservation can be either accepted or rejected");
            }
            default -> throw new ReservationException("Unknown reservation status: " + newStatus);
        }

    }

}
