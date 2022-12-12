package org.catcom.classreserver.model.reservation;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.catcom.classreserver.exceptions.ClassroomException;
import org.catcom.classreserver.exceptions.ReservationException;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.classroom.ClassroomManager;
import org.catcom.classreserver.model.user.User;
import org.catcom.classreserver.model.user.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationManager
{
    @Autowired
    private ReservationRepos reservationRepos;

    @Autowired
    private ClassroomManager classroomManager;

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

        // reservationRepos.save(newReservation);

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

    public Iterable<Reservation> getUserReservation(@NonNull User user)
    {
        return reservationRepos.findByOwnerId(user.getId());
    }

    public Iterable<Reservation> getUserReservation(@NonNull User user, @NonNull String status)
    {
        return reservationRepos.findByOwnerIdAndStatus(user.getId(), status);
    }

    public Iterable<Reservation> getAllReservation()
    {
        return reservationRepos.findAll();
    }

    public Iterable<Reservation> getAllReservation(@NonNull String status)
    {
        return reservationRepos.findByStatus(status);
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

        var reservations = reservationRepos.findByRoomId(classroom.getId());

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
                throw new ReservationException("The requested room is not available for the given time");
            }
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
