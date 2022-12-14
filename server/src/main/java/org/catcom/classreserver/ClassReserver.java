package org.catcom.classreserver;

import org.catcom.classreserver.config.RsaKeyProperties;
import org.catcom.classreserver.exceptions.ClassroomException;
import org.catcom.classreserver.exceptions.ReservationException;
import org.catcom.classreserver.form.EditClassroomForm;
import org.catcom.classreserver.form.EditReservationForm;
import org.catcom.classreserver.form.ReserveForm;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.classroom.ClassroomManager;
import org.catcom.classreserver.model.reservation.*;
import org.catcom.classreserver.model.user.*;
import org.catcom.classreserver.service.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
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
	private ClassroomManager classroomManager;

	@Autowired
	private ReservationManager reservationManager;


	@Autowired
	private UserDetailService userDetailService;

	//// endregion


	//// region Classroom

	@GetMapping("/")
	String home(
			Authentication auth
	)
	{
		return "home";
	}

	@GetMapping("/classrooms")
	@ResponseBody Iterable<Classroom> getAllClassrooms()
	{
		return classroomManager.getAllClassrooms();
	}

	@GetMapping("/classrooms/{id}")
	@ResponseBody Classroom getClassroomById(@PathVariable Integer id)
	{
		try
		{
			return classroomManager.findRoom(id);
		}
		catch (ClassroomException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	@PostMapping("/classrooms/{id}")
	void editClassroom(
			Authentication auth,
			@PathVariable String id,
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
			classroomManager.updateRoomDetail(
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

	//// endregion


	//// region Reservations

	/**
	 * GET /reservations?owned=(0|1)&status=(pending|accepted|rejected)
	 * @param status status
	 * @return list of classrooms
	 */
	@GetMapping("/reservations")
	@ResponseBody Iterable<Reservation> getAllReservations(
			Authentication auth,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String status
	)
	{

		var userDetail = userDetailService.loadByAuthentication(auth);

		if (userDetail == null)
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		if (username == null)
		{

			if (status == null)
			{
				return reservationManager.getAllReservation();
			}

			return reservationManager.getAllReservation(status);

		}

		else
		{

			if (status == null)
			{
				return reservationManager.getUserReservation(userDetail.getUser());
			}

			return reservationManager.getUserReservation(userDetail.getUser(), status);
		}

	}

	@GetMapping("/reservations/{id}")
	@ResponseBody Reservation getReservationById(@PathVariable Integer id)
	{

		try
		{
			return reservationManager.getReservation(id);
		}
		catch (ReservationException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}

	}

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

			var reserveTime = LocalDateTime.now();

			var reserveRoom = classroomManager.findRoom(form.getRoomId());

			reservationManager.reserve(
					userDetail.getUser(),
					reserveRoom,
					reserveTime,
					form.getStartTime(),
					form.getFinishTime()
			);

		}
		catch (ReservationException | ClassroomException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

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

				var editingReservation = reservationManager.getReservation(id);

				// not allow change other user's reservation
				if (!editingReservation.getOwner().equals(userDetail.getUser()))
				{
					throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only staffs can edit other user's reservations");
				}

			}

			var newReservedRoom = classroomManager.findRoom(form.getRoomId());

			reservationManager.updateReservation(
					id,
					newReservedRoom,
					form.getStartTime(),
					form.getFinishTime(),
					form.getStatus()
			);

		}
		catch (ReservationException | ClassroomException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}

	}

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
