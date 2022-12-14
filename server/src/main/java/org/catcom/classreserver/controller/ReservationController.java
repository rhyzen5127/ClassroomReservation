package org.catcom.classreserver.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import org.catcom.classreserver.exceptions.ClassroomException;
import org.catcom.classreserver.exceptions.reservations.ReservationException;
import org.catcom.classreserver.form.ApproveReservationForm;
import org.catcom.classreserver.form.EditReservationForm;
import org.catcom.classreserver.form.ReserveForm;
import org.catcom.classreserver.model.building.BuildingRepos;
import org.catcom.classreserver.model.reservation.Reservation;
import org.catcom.classreserver.model.reservation.ReservationRepos;
import org.catcom.classreserver.model.reservation.ReservationStatus;
import org.catcom.classreserver.service.UserDetailService;
import org.catcom.classreserver.model.user.UserRole;
import org.catcom.classreserver.service.ClassroomService;
import org.catcom.classreserver.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
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
    @Autowired
    private BuildingRepos buildingRepos;
    @Autowired
    private ReservationRepos reservationRepos;

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
            @RequestParam(required = false) String status,
            @RequestParam(required = false) ZonedDateTime minReserveTime,
            @RequestParam(required = false) ZonedDateTime maxReserveTime,
            @RequestParam(required = false) Integer limit
    )
    {
        var minLocalReserveTime = minReserveTime == null ? null : minReserveTime.toLocalDateTime();
        var maxLocalReserveTime = maxReserveTime == null ? null : maxReserveTime.toLocalDateTime();

        return reservationService.findReservations(
                null,
                null,
                null,
                status,
                minLocalReserveTime,
                maxLocalReserveTime,
                limit
        );
    }

    // GET reservation of a room
    @GetMapping("/classrooms/{roomId}/reservations")
    @ResponseBody List<Reservation> getReservationOfRoom(
            @PathVariable Integer roomId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) ZonedDateTime minReserveTime,
            @RequestParam(required = false) ZonedDateTime maxReserveTime,
            @RequestParam(required = false) Integer limit
            )
    {
        try
        {
            var room = classroomService.findRoom(roomId);

            var minLocalReserveTime = minReserveTime == null ? null : minReserveTime.toLocalDateTime();
            var maxLocalReserveTime = maxReserveTime == null ? null : maxReserveTime.toLocalDateTime();

            return reservationService.findReservations(
                    null,
                    null,
                    room,
                    status,
                    minLocalReserveTime,
                    maxLocalReserveTime,
                    limit
            );

        }
        catch (ClassroomException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    // GET reservation of a user
    @GetMapping("/users/{userId}/reservations")
    @ResponseBody List<Reservation> getReservationOfUser(
            @PathVariable Integer userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) ZonedDateTime minReserveTime,
            @RequestParam(required = false) ZonedDateTime maxReserveTime,
            @RequestParam(required = false) Integer limit
    )
    {
        try
        {
            var user = userDetailService.loadUserById(userId).getUser();

            var minLocalReserveTime = minReserveTime == null ? null : minReserveTime.toLocalDateTime();
            var maxLocalReserveTime = maxReserveTime == null ? null : maxReserveTime.toLocalDateTime();

            return reservationService.findReservations(
                    user,
                    null,
                    null,
                    status,
                    minLocalReserveTime,
                    maxLocalReserveTime,
                    limit
            );
        }
        catch (UsernameNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }

    }

    // GET reservation of room in building
    @GetMapping("/buildings/{buildingId}/reservations")
    @ResponseBody List<Reservation> getReservationOfBuilding(
            @PathVariable Integer buildingId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) ZonedDateTime minReserveTime,
            @RequestParam(required = false) ZonedDateTime maxReserveTime,
            @RequestParam(required = false) Integer limit
    )
    {
        try
        {
            var building = buildingRepos.findById(buildingId);

            if (building.isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown building id: " + buildingId);

            var minLocalReserveTime = minReserveTime == null ? null : minReserveTime.toLocalDateTime();
            var maxLocalReserveTime = minReserveTime == null ? null : maxReserveTime.toLocalDateTime();

            return reservationService.findReservations(
                    null,
                    building.get(),
                    null,
                    status,
                    minLocalReserveTime,
                    maxLocalReserveTime,
                    limit
            );
        }
        catch (UsernameNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }

    }


    // POST new reservation
    @PostMapping("/reservations")
    void reserve(Authentication auth, @RequestBody ReserveForm form)
    {

        var userDetail = userDetailService.loadByAuthentication(auth);
        if (userDetail == null)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        try
        {

            var reserveRoom = classroomService.findRoom(form.getRoomId());

            var doReserveTime = LocalDateTime.now();
            var startTime = form.getStartTime().toLocalDateTime();
            var finishTime = form.getFinishTime().toLocalDateTime();

            reservationService.reserve(
                    userDetail.getUser(),
                    reserveRoom,
                    doReserveTime,
                    startTime,
                    finishTime,
                    form.getReserveNote()
            );

        }
        catch (ReservationException | ClassroomException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }


    @GetMapping("/classrooms/{roomId}/availability")
    @ResponseBody Map<String, Boolean> checkScheduleAvailability(
            Authentication auth,
            @PathVariable Integer roomId,
            @RequestParam ZonedDateTime startTime,
            @RequestParam ZonedDateTime finishTime
    )
    {
        // Note: user maybe null
        var userDetail = userDetailService.loadByAuthentication(auth);

        try
        {

            var room = classroomService.findRoom(roomId);
            var startTimeLocal = startTime.toLocalDateTime();
            var finishTimeLocal = finishTime.toLocalDateTime();

            var roomAvailable = room.isReady() && !reservationService.isRoomHasOverlapSchedule(room, startTimeLocal, finishTimeLocal);
            var userAvailable = userDetail == null || !reservationService.isUserHasOverlapSchedule(userDetail.getUser(), startTimeLocal, finishTimeLocal);

            return Map.of( "available", roomAvailable && userAvailable );

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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        try
        {

            // additional check for non-staff user
            if (!userDetail.getAuthorities().contains(UserRole.STAFF))
            {

                var editingReservation = reservationService.getReservation(id);

                // not allow change other user's reservation
                if (!editingReservation.getOwner().equals(userDetail.getUser()))
                {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only staffs can edit other user's reservations");
                }

            }

            var newReservedRoom = form.getRoomId() == null ? null : classroomService.findRoom(form.getRoomId());

            var newStartTime = form.getStartTime() == null ? null : form.getStartTime().toLocalDateTime();
            var newFininshTime = form.getFinishTime() == null ? null : form.getFinishTime().toLocalDateTime();

            reservationService.updateReservation(
                    id,
                    newReservedRoom,
                    newStartTime,
                    newFininshTime,
                    form.getReserveNote()
            );

        }
        catch (ReservationException | ClassroomException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }


    // POST approve reservation
    @PostMapping("/reservations/{id}/approve")
    void approveReservation(Authentication auth, @PathVariable int id, @RequestBody ApproveReservationForm form)
    {
        var userDetail = userDetailService.loadByAuthentication(auth);
        if (userDetail == null || !userDetail.isStaff())
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        reservationService.updateReservationStatus(id, ReservationStatus.APPROVED, userDetail.getUser(), form.getReason());
    }


    // POST reject reservation
    @PostMapping("/reservations/{id}/reject")
    void rejectReservation(Authentication auth, @PathVariable int id, @RequestBody ApproveReservationForm form)
    {
        var userDetail = userDetailService.loadByAuthentication(auth);
        if (userDetail == null || !userDetail.isStaff())
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        reservationService.updateReservationStatus(id, ReservationStatus.REJECTED, userDetail.getUser(), form.getReason());
    }


    // POST reject reservation
    @PostMapping("/reservations/{id}/cancel")
    void cancelReservation(Authentication auth, @PathVariable int id, @RequestBody ApproveReservationForm form)
    {
        var userDetail = userDetailService.loadByAuthentication(auth);
        if (userDetail == null)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        var toCancel = reservationService.getReservation(id);
        if (!toCancel.getOwner().equals(userDetail.getUser()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can only cancel their own reservation");
        }
        reservationService.updateReservationStatus(id, ReservationStatus.CANCELED, userDetail.getUser(), form.getReason());
    }

    /*
    // POST reject reservation
    @DeleteMapping("/reservations/{id}")
    void deleteReservation(Authentication auth, @PathVariable int id)
    {
        var userDetail = userDetailService.loadByAuthentication(auth);
        if (userDetail == null)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        reservationService.deleteReservation(id, userDetail.getUser());
    }
    */
}
