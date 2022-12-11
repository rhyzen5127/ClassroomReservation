package org.catcom.classreserver;

import org.catcom.classreserver.model.building.BuildingRepos;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.classroom.ClassroomRepos;
import org.catcom.classreserver.model.user.User;
import org.catcom.classreserver.model.user.UserRepos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@SpringBootApplication
@RestController
public class ClassReserver
{

	private static final Logger log = LoggerFactory.getLogger(ClassReserver.class);

	public static void main(String[] args)
	{
		SpringApplication.run(ClassReserver.class, args);
	}

	// region Database repositories
	@Autowired
	private UserRepos userRepos;
	@Autowired
	private BuildingRepos buildingRepos;
	@Autowired
	private ClassroomRepos classroomRepos;



	@GetMapping("/voraphat")
	@ResponseBody String voraphat()
	{
		return "Voraphat";
	}

	//// region Classroom

	@GetMapping("/classrooms")
	@ResponseBody Iterable<Classroom> getAllClassrooms()
	{
		return classroomRepos.findAll();
	}

	@GetMapping("/classrooms/{id}")
	@ResponseBody Classroom getClassroomById(@PathVariable Integer id)
	{
		var classroom = classroomRepos.findById(id);

		if (classroom.isEmpty())
			throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "Classroom not found.");

		return classroom.get();
	}

	@PutMapping("/classrooms/{id}")
	@ResponseBody String editClassroom(
			@PathVariable String id,
			@RequestBody String status
	) {
		return "Classroom #" + id + " edited to " + status;
	}

	//// endregion


	//// region Reservations

	@GetMapping("/reservations")
	@ResponseBody Iterable<String> getAllReservations()
	{
		return List.of("Classroom");
	}

	@GetMapping("/reservations/{id}")
	@ResponseBody String getReservationById(@PathVariable String id)
	{
		return "Reservation #" + id;
	}
	//// endregion


	@GetMapping("/users")
	@ResponseBody Iterable<User> getAllUsers()
	{
		return userRepos.findAll();
	}

}
