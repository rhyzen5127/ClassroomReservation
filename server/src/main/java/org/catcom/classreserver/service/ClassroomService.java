package org.catcom.classreserver.service;

import jakarta.annotation.Nullable;
import org.catcom.classreserver.exceptions.ClassroomException;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.classroom.ClassroomRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService
{
    @Autowired
    private ClassroomRepos classroomRepos;

    public List<Classroom> getAllClassrooms()
    {
        return classroomRepos.findAll();
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
            @Nullable String status
    ) throws ClassroomException
    {
        var room = findRoom(id);

        if (width != null) room.setWidth(width);
        if (length != null) room.setLength(length);
        if (seats != null) room.setSeats(seats);
        if (status != null) room.setStatus(status);

        classroomRepos.save(room);
    }
}
