package org.catcom.classreserver;

import org.catcom.classreserver.exceptions.ClassroomException;
import org.catcom.classreserver.exceptions.ReservationException;
import org.catcom.classreserver.form.EditClassroomForm;
import org.catcom.classreserver.form.EditReservationForm;
import org.catcom.classreserver.form.ReserveForm;
import org.catcom.classreserver.model.building.BuildingRepos;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.classroom.ClassroomManager;
import org.catcom.classreserver.model.classroom.ClassroomRepos;
import org.catcom.classreserver.model.reservation.*;
import org.catcom.classreserver.model.user.User;
import org.catcom.classreserver.model.user.UserDetail;
import org.catcom.classreserver.model.user.UserRepos;
import org.catcom.classreserver.model.user.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.Optional;

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

	//// endregion


	//// region Classroom

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
			throw new HttpClientErrorException(HttpStatusCode.valueOf(400), e.getMessage());
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
			throw new HttpClientErrorException(HttpStatusCode.valueOf(403), "Only staff can edit classroom details");
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
			throw new HttpClientErrorException(HttpStatusCode.valueOf(400), e.getMessage());
		}

	}

	//// endregion


	//// region Reservations

	/**
	 * GET /reservations?owned=(0|1)&status=(pending|accepted|rejected)
	 * @param owned owned
	 * @param status status
	 * @return list of classrooms
	 */
	@GetMapping("/reservations")
	@ResponseBody Iterable<Reservation> getAllReservations(
			Authentication auth,
			@RequestParam(defaultValue = "false") Boolean owned,
			@RequestParam(required = false) String status
	)
	{
		var userDetail = (UserDetail) auth.getDetails();

		if (!owned)
		{

			if (status == null)
			{
				return reservationManager.getAllReservation();
			}

			return reservationManager.getAllReservation(status);

		}

		if (status == null)
		{
			return reservationManager.getUserReservation(userDetail.getUser());
		}

		return reservationManager.getUserReservation(userDetail.getUser(), status);
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
			throw new HttpClientErrorException(HttpStatusCode.valueOf(400), e.getMessage());
		}

	}

	@PostMapping("/reservations")
	void reserve(Authentication auth, @RequestBody ReserveForm form)
	{

		var userDetail = (UserDetail) auth.getDetails();

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
			throw new HttpClientErrorException(HttpStatusCode.valueOf(400), e.getMessage());
		}
	}

	@PostMapping("/reservations/{id}")
	void editReservation(
			Authentication auth,
			@PathVariable Integer id,
			@RequestBody EditReservationForm form
	)
	{

		var userDetail = (UserDetail) auth.getDetails();

		try
		{

			// additional check for non-staff user
			if (!userDetail.getAuthorities().contains(UserRole.STAFF))
			{

				// not allow status change
				if (form.getStatus() == null)
				{
					throw new HttpClientErrorException(HttpStatusCode.valueOf(403), "Only staffs can edit reservation's status");
				}

				var editingReservation = reservationManager.getReservation(id);

				// not allow change other user's reservation
				if (!editingReservation.getOwner().equals(userDetail.getUser()))
				{
					throw new HttpClientErrorException(HttpStatusCode.valueOf(403), "Only staffs can edit other user's reservations");
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
			throw new HttpClientErrorException(HttpStatusCode.valueOf(400), e.getMessage());
		}

	}

	//// endregion

	@Autowired
	private UserRepos userRepos;

	//// region User
	@GetMapping("/users")
	@ResponseBody Iterable<User> getAllUsers()
	{
		return userRepos.findAll();
	}
	//// endregion

}
