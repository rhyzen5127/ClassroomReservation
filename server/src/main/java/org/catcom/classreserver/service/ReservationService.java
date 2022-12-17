package org.catcom.classreserver.service;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.catcom.classreserver.exceptions.reservations.InvalidReservationStateException;
import org.catcom.classreserver.exceptions.reservations.ReservationException;
import org.catcom.classreserver.exceptions.reservations.ReservationNotFoundException;
import org.catcom.classreserver.exceptions.reservations.ReservationScheduleOverlapException;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.reservation.Reservation;
import org.catcom.classreserver.model.reservation.ReservationRepos;
import org.catcom.classreserver.model.reservation.ReservationStatus;
import org.catcom.classreserver.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        validateRoomSchedule(room, startTime, finishTime);

        var reservation = new Reservation();
        reservation.setOwner(reserver);
        reservation.setRoom(room);
        reservation.setBookingTime(bookingTime);
        reservation.setStartTime(startTime);
        reservation.setFinishTime(finishTime);
        reservation.setPending();

        // reservationRepos.save(reservation);

    }

    /* Find by owner and status */
    public List<Reservation> findReservations(@Nullable User owner, @Nullable String status)
    throws ReservationException
    {
        return findReservations(owner, null, status);
    }

    /* Find by room and status */
    public List<Reservation> findReservations(@Nullable Classroom classroom, @Nullable String status)
    throws ReservationException
    {
        return findReservations(null, classroom, status);
    }

    /* Find by owner, room and status */
    public List<Reservation> findReservations(
        @Nullable User owner,
        @Nullable Classroom classroom,
        @Nullable String status
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

        if (classroom != null)
        {
            specs.add(ReservationRepos.forRoom(classroom));
        }

        if (status != null)
        {
            specs.add(ReservationRepos.hasStatus(status));
        }

        var spec = Specification.allOf(specs);

        return reservationRepos.findAll(spec);

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
            @Nullable LocalDateTime newFinishTime,
            @Nullable String newStatus
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

        if (newStatus != null)
        {
            if (APPROVED.equalsIgnoreCase(newStatus))
            {
                requireScheduleValidated = true;
            }
            validateStatus(reservation, newStatus);
            reservation.setStatus(newStatus);
        }

        if (requireScheduleValidated)
        {
            validateRoomSchedule(reservation.getRoom(), reservation.getStartTime(), reservation.getFinishTime());
        }


        reservationRepos.save(reservation);

    }


    public boolean isRoomAvailableAtGivenSchedule(Classroom classroom, LocalDateTime startTime, LocalDateTime finishTime)
    {
        var reservations = findReservations(classroom, APPROVED);

        for (var reservation : reservations)
        {
            // check for overlapped schedule
            var curStartTime = reservation.getStartTime();
            var curFinishTime = reservation.getFinishTime();
            if (    curStartTime.isBefore(startTime) &&
                    curFinishTime.isAfter(startTime) ||
                    curStartTime.isBefore(finishTime) &&
                    curFinishTime.isAfter(finishTime)
            ) {
                return false;
            }
        }

        return true;
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

    void validateRoomSchedule(
            @NonNull Classroom classroom,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime finishTime
    ) throws ReservationScheduleOverlapException
    {
        if (!isRoomAvailableAtGivenSchedule(classroom, startTime, finishTime)) {
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
                if (reservation.isPending()) return;
                throw new InvalidReservationStateException("Cannot set reservation status to pending again");
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
