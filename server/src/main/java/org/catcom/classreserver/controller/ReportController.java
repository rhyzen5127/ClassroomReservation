package org.catcom.classreserver.controller;

import com.opencsv.CSVWriter;
import org.catcom.classreserver.exceptions.ClassroomException;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.reservation.ReservationRepos;
import org.catcom.classreserver.service.ClassroomService;
import org.catcom.classreserver.service.ReportGeneratorService;
import org.catcom.classreserver.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@RestController
public class ReportController
{
    @Autowired
    private ReportGeneratorService reportGeneratorService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/classrooms/{roomId}/schedules.{ext}")
    ResponseEntity<ByteArrayResource> getClassroomSchedule(@PathVariable Integer roomId, @PathVariable String ext)
    {

        Classroom room;
        try {
            room = classroomService.findRoom(roomId);
        } catch (ClassroomException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        if ("csv".equalsIgnoreCase(ext))
        {

            try {

                var byteStream = new ByteArrayOutputStream();
                var byteWriter = new OutputStreamWriter(byteStream);

                var csvWriter = new CSVWriter(byteWriter);

                csvWriter.writeAll(List.of(
                        new String[]{ "id", "fname", "lname" },
                        new String[]{ "1", "voraphat", "asawathongchai" },
                        new String[]{ "2", "sahachai", "plangrit" }
                ));

                byteWriter.flush();

                var bodyResource = new ByteArrayResource(byteStream.toByteArray());

                return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(bodyResource);

            } catch (IOException e)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "IOException: " + e.getMessage());
            }
        } else if ("pdf".equalsIgnoreCase(ext)) {

            try
            {
                var reservations = reservationService.findReservations(null, null, room, null, null, null, null);
                var bodyResource = reportGeneratorService.genReport(reservations);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bodyResource);

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
