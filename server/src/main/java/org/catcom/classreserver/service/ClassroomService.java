package org.catcom.classreserver.service;

import jakarta.annotation.Nullable;
import org.catcom.classreserver.exceptions.ClassroomException;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.classroom.ClassroomRepos;
import org.catcom.classreserver.model.reservation.Reservation;
import org.catcom.classreserver.model.reservation.ReservationRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.catcom.classreserver.model.reservation.ReservationStatus.APPROVED;

@Service
public class ClassroomService
{
    @Autowired
    private ClassroomRepos classroomRepos;


    public List<Classroom> getAllClassrooms()
    {
        var sort = Sort.by(Sort.Order.asc("name"));
        return classroomRepos.findAll(sort);
    }

    public Classroom findRoom(int roomId) throws ClassroomException
    {
        var room = classroomRepos.findById(roomId);
        if (room.isEmpty())
        {
            throw new ClassroomException("Classroom not found");
        }
        return room.get();
    }

    public void updateRoomDetail(
            int id,
            @Nullable Integer width,
            @Nullable Integer length,
            @Nullable Integer seats,
            @Nullable String status,
            @Nullable String note
    ) throws ClassroomException
    {
        var room = findRoom(id);

        if (width != null) room.setWidth(width);
        if (length != null) room.setLength(length);
        if (seats != null) room.setSeats(seats);
        if (status != null) room.setStatus(status);
        if (note != null) room.setNote(note);

        classroomRepos.save(room);

    }

}
