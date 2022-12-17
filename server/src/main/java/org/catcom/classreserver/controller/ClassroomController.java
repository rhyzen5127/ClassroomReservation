package org.catcom.classreserver.controller;

import org.catcom.classreserver.exceptions.ClassroomException;
import org.catcom.classreserver.form.EditClassroomForm;
import org.catcom.classreserver.model.building.BuildingRepos;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.service.UserDetailService;
import org.catcom.classreserver.model.user.UserRole;
import org.catcom.classreserver.service.ClassroomService;
import org.catcom.classreserver.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ClassroomController
{

    @Autowired
    private BuildingRepos buildingRepos;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClassroomService classroomService;


    @GetMapping("/classrooms")
    @ResponseBody
    Iterable<Classroom> getAllClassrooms()
    {
        return classroomService.getAllClassrooms();
    }

    @GetMapping("/buildings/{buildingId}/classrooms")
    @ResponseBody
    Iterable<Classroom> getClassroomOfBuilding(@PathVariable Integer buildingId)
    {
        var building = buildingRepos.findById(buildingId);

        if (building.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Building not found");
        }

        return building.get().getClassrooms();
    }

    @GetMapping("/classrooms/{id}")
    @ResponseBody Classroom getClassroomById(
            @PathVariable Integer id
    )
    {
        try
        {
            return classroomService.findRoom(id);
        }
        catch (ClassroomException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/classrooms/{id}")
    void editClassroom(
            Authentication auth,
            @PathVariable Integer id,
            @RequestBody EditClassroomForm form
    )
    {
        // Allow only staff to edit classroom
        if (!auth.getAuthorities().contains(UserRole.STAFF))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,  "Only staff can edit classroom details");
        }

        try
        {
            classroomService.updateRoomDetail(
                    id,
                    form.getWidth(),
                    form.getLength(),
                    form.getSeats(),
                    form.getStatus()
            );
        }
        catch (ClassroomException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }


}
