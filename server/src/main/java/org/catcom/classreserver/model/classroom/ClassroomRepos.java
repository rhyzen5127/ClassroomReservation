package org.catcom.classreserver.model.classroom;

import org.catcom.classreserver.model.reservation.Reservation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;

public interface ClassroomRepos extends JpaRepository<Classroom, Integer>, JpaSpecificationExecutor<Classroom> {

}
