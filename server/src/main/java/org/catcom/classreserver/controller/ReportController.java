package org.catcom.classreserver.controller;

import com.nimbusds.jose.util.Pair;
import com.opencsv.CSVWriter;
import org.catcom.classreserver.form.ExportScheduleForm;
import org.catcom.classreserver.form.NotedReservation;
import org.catcom.classreserver.form.NotedReservationInstance;
import org.catcom.classreserver.model.reservation.Reservation;
import org.catcom.classreserver.service.ClassroomService;
import org.catcom.classreserver.service.ReportGeneratorService;
import org.catcom.classreserver.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.function.Function;

@RestController
public class ReportController
{
    @Autowired
    private ReportGeneratorService reportGeneratorService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClassroomService classroomService;

    @PostMapping("/reservations/schedules.{ext}")
    ResponseEntity<ByteArrayResource> exportReservationSchedules(
            @PathVariable String ext,
            @RequestBody ExportScheduleForm form
    )
    {

        var reservationInput = form.getReservations();

        if (reservationInput == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No reservation specified!");
        }

        if (reservationInput.length > 15)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot export more than 15 reservations in a single file!");
        }

        var notedReservation = Arrays.stream(reservationInput)
                .map(r -> new NotedReservationInstance(reservationService.getReservation(r.getId()), r.getNote()))
                .sorted((v1, v2) -> {

                    var compStartTime = v1.getReservation().getStartTime().compareTo(v2.getReservation().getStartTime());
                    if (compStartTime != 0) return compStartTime;

                    var compFinishTime = v1.getReservation().getFinishTime().compareTo(v2.getReservation().getFinishTime());
                    if (compFinishTime != 0) return compFinishTime;

                    return v1.getReservation().getBookingTime().compareTo(v2.getReservation().getBookingTime());
                })
                .toList()
                ;


        var reservations = new ArrayList<Reservation>();
        var notes = new ArrayList<String>();

        for (var i : notedReservation)
        {
            reservations.add(i.getReservation());
            notes.add(i.getNote());
        }

        if ("csv".equalsIgnoreCase(ext))
        {

            try
            {
                var bodyResource = reportGeneratorService.genCSVReport(reservations, notes);
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"schedules.csv\"").contentType(MediaType.APPLICATION_OCTET_STREAM).body(bodyResource);

            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DocumentException: " + e.getMessage());
            }

        } else if ("pdf".equalsIgnoreCase(ext)) {

            try
            {
                var bodyResource = reportGeneratorService.genPDFReport(reservations, notes);
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"schedules.pdf\"").contentType(MediaType.APPLICATION_PDF).body(bodyResource);

            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DocumentException: " + e.getMessage());
            }

        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot handle extension " + ext);
    }

}
