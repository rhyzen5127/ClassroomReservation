package org.catcom.classreserver.model.classroom;

import jakarta.annotation.Nullable;
import org.catcom.classreserver.exceptions.ClassroomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClassroomManager
{
    @Autowired
    private ClassroomRepos classroomRepos;

    public Iterable<Classroom> getAllClassrooms()
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
            String id,
            @Nullable Integer width,
            @Nullable Integer length,
            @Nullable Integer seats,
            @Nullable String status
    ) throws ClassroomException
    {
    }
}
