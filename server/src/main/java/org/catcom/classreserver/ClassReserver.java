package org.catcom.classreserver;

import org.catcom.classreserver.exceptions.ClassroomException;
import org.catcom.classreserver.exceptions.ReservationException;
import org.catcom.classreserver.form.EditClassroomForm;
import org.catcom.classreserver.form.EditReservationForm;
import org.catcom.classreserver.form.ReserveForm;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.service.ClassroomService;
import org.catcom.classreserver.model.reservation.*;
import org.catcom.classreserver.model.user.*;
import org.catcom.classreserver.service.JwtTokenService;
import org.catcom.classreserver.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@SpringBootApplication
@RestController
public class ClassReserver
{

	private static final Logger log = LoggerFactory.getLogger(ClassReserver.class);

	public static void main(String[] args)
	{
		SpringApplication.run(ClassReserver.class, args);
	}

	//// region Object managers

	@Autowired
	private ClassroomService classroomService;

	@Autowired
	private ReservationService reservationService;


	@Autowired
	private UserDetailService userDetailService;

	//// endregion


	//// region Classroom
	//// endregion


	//// region Reservations
	//// endregion

	@Autowired
	private UserRepos userRepos;

	@Autowired
	private JwtTokenService tokenService;
	@Autowired
	private ReservationRepos reservationRepos;

	//// region User
	@GetMapping("/users")
	@ResponseBody Iterable<User> getAllUsers()
	{
		return userRepos.findAll();
	}

	@PostMapping("/login/token")
	@ResponseBody String loginSuccess(Authentication auth)
	{
		return tokenService.generateAccessToken(auth);
	}

	//// endregion

}
