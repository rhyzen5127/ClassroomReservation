package org.catcom.classreserver.service;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import org.catcom.classreserver.exceptions.ReservationException;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.reservation.Reservation;
import org.catcom.classreserver.model.reservation.ReservationRepos;
import org.catcom.classreserver.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService
{

    private Logger log = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepos reservationRepos;

    @Autowired
    private ClassroomService classroomService;

    public void reserve(
            User reserver,
            Classroom room,
            @NonNull LocalDateTime bookingTime,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime finishTime
    ) throws ReservationException
    {

        validateReservedRoom(room);
        validateRoomSchedule(room, startTime, finishTime);

        var newReservation = new Reservation();
        newReservation.setOwner(reserver);
        newReservation.setRoom(room);
        newReservation.setBookingTime(bookingTime);
        newReservation.setStartTime(startTime);
        newReservation.setFinishTime(finishTime);
        newReservation.setStatus("pending");

        reservationRepos.save(newReservation);

    }

    public List<Reservation> findReservations(@Nullable User owner, @Nullable String status)
    {
        return findReservations(owner, null, status);
    }

    public List<Reservation> findReservations(@Nullable Classroom classroom, @Nullable String status)
    {
        return findReservations(null, classroom, status);
    }

    public List<Reservation> findReservations(
        @Nullable User owner,
        @Nullable Classroom classroom,
        @Nullable String status
    )
    {

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


    public Reservation getReservation(int id) throws ReservationException
    {
        var reservation = reservationRepos.findById(id);
        if (reservation.isEmpty())
        {
            throw new ReservationException("The requested reservation does not exist");
        }

        return reservation.get();
    }

    public void updateReservation(
            int id,
            @Nullable Classroom newRoom,
            @Nullable LocalDateTime newStartTime,
            @Nullable LocalDateTime newFinishTime,
            @Nullable String newStatus
    ) throws ReservationException {

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

        if (newStatus != null)
        {
            validateStatus(reservation, newStatus);
            reservation.setStatus(newStatus);
        }

        reservationRepos.save(reservation);

    }


    public boolean isRoomAvailableAtGivenSchedule(Classroom classroom, LocalDateTime startTime, LocalDateTime finishTime)
    {
        var reservations = reservationRepos.findByRoomIdAndStatus(classroom.getId(), "approved");

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
            throw new ReservationException("The requested room does not exist");
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
    ) throws ReservationException
    {
        if (!isRoomAvailableAtGivenSchedule(classroom, startTime, finishTime)) {
            throw new ReservationException("The requested room is not available for the given time");
        }
    }

    void validateStatus(
            @NonNull Reservation reservation,
            @NonNull String newStatus
    ) throws ReservationException
    {

        switch (newStatus)
        {
            case "pending": return;

            case "canceled":
                if (reservation.isApproved() || reservation.isPending()) return;
                throw new ReservationException("Only accepted and pending reservation can be canceled");

            case "accepted", "rejected":
                if (reservation.isPending()) return;
                throw new ReservationException("Only pending reservation can be either accepted or rejected");

            default:
                throw new ReservationException("Unknown reservation status: " + newStatus);
        }

    }

}
