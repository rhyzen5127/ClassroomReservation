package org.catcom.classreserver.controller;

import org.catcom.classreserver.exceptions.ClassroomException;
import org.catcom.classreserver.exceptions.ReservationException;
import org.catcom.classreserver.form.EditReservationForm;
import org.catcom.classreserver.form.ReserveForm;
import org.catcom.classreserver.model.reservation.Reservation;
import org.catcom.classreserver.service.UserDetailService;
import org.catcom.classreserver.model.user.UserRole;
import org.catcom.classreserver.service.ClassroomService;
import org.catcom.classreserver.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class ReservationController
{

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClassroomService classroomService;

    // GET reservation by id
    @GetMapping("/reservations/{id}")
    @ResponseBody Reservation getReservationById(@PathVariable Integer id)
    {
        try
        {
            return reservationService.getReservation(id);
        }
        catch (ReservationException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    // GET all reservations
    @GetMapping("/reservations")
    @ResponseBody List<Reservation> getAllReservations(
            Authentication auth,
            @RequestParam(required = false) String status
    )
    {
        return reservationService.findReservations(null, null, status);
    }

    // GET reservation of a room
    @GetMapping("/classrooms/{roomId}/reservations")
    @ResponseBody List<Reservation> getReservationOfRoom(
            @PathVariable Integer roomId,
            @RequestParam(required = false) String status
    )
    {
        try
        {
            var room = classroomService.findRoom(roomId);
            return reservationService.findReservations(room, status);
        }
        catch (ClassroomException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    // GET reservation of a user
    @GetMapping("/users/{userId}/reservations")
    @ResponseBody List<Reservation> getReservationOfUser(
            Authentication auth,
            @PathVariable Integer userId,
            @RequestParam(required = false) String status
    )
    {
        var requestingUserDetail = userDetailService.loadByAuthentication(auth);

        if (!requestingUserDetail.isStaff() && requestingUserDetail.getId() != userId)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only staffs can get reservations of other user directly");
        }

        var user = userDetailService.loadUserById(userId).getUser();

        return reservationService.findReservations(user, status);

    }

    // POST new reservation
    @PostMapping("/reservations")
    void reserve(Authentication auth, @RequestBody ReserveForm form)
    {

        var userDetail = userDetailService.loadByAuthentication(auth);

        try
        {

            var reserveTime = LocalDateTime.now();

            var reserveRoom = classroomService.findRoom(form.getRoomId());

            reservationService.reserve(
                    userDetail.getUser(),
                    reserveRoom,
                    reserveTime,
                    form.getStartTime().toLocalDateTime(),
                    form.getFinishTime().toLocalDateTime()
            );

        }
        catch (ReservationException | ClassroomException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }


    @GetMapping("/classrooms/{roomId}/availability")
    @ResponseBody Map<String, Boolean> checkScheduleAvailability(
            @PathVariable Integer roomId,
            @RequestParam ZonedDateTime startTime,
            @RequestParam ZonedDateTime finishTime
    )
    {
        try
        {

            var room = classroomService.findRoom(roomId);

            var roomAvailable = reservationService.isRoomAvailableAtGivenSchedule(room, startTime.toLocalDateTime(), finishTime.toLocalDateTime());

            return Map.of( "available", roomAvailable );

        }
        catch (ClassroomException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    // POST edit reservation data
    @PostMapping("/reservations/{id}")
    void editReservation(
            Authentication auth,
            @PathVariable Integer id,
            @RequestBody EditReservationForm form
    )
    {

        var userDetail = userDetailService.loadByAuthentication(auth);

        if (userDetail == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try
        {

            // additional check for non-staff user
            if (!userDetail.getAuthorities().contains(UserRole.STAFF))
            {

                // not allow status change
                if (form.getStatus() == null)
                {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only staffs can edit reservation's status");
                }

                var editingReservation = reservationService.getReservation(id);

                // not allow change other user's reservation
                if (!editingReservation.getOwner().equals(userDetail.getUser()))
                {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only staffs can edit other user's reservations");
                }

            }

            var newReservedRoom = classroomService.findRoom(form.getRoomId());

            var newStartTime = form.getStartTime() == null ? null : form.getStartTime().toLocalDateTime();
            var newFininshTime = form.getFinishTime() == null ? null : form.getFinishTime().toLocalDateTime();

            reservationService.updateReservation(
                    id,
                    newReservedRoom,
                    newStartTime,
                    newFininshTime,
                    form.getStatus()
            );

        }
        catch (ReservationException | ClassroomException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }
}
