package org.catcom.classreserver.model.reservation;

import org.catcom.classreserver.model.building.Building;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.user.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

public interface ReservationRepos extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation>
{
    static Specification<Reservation> hasOwner(User user) {
        return (r, cq, cb) -> cb.equal(r.get("owner"), user);
    }

    static Specification<Reservation> except(Reservation reservation) {
        return (r, cq, cb) -> cb.notEqual(r, reservation);
    }

    static Specification<Reservation> hasStatus(String status) {
        return (r, cq, cb) -> cb.equal(r.get("status"), status);
    }

    static Specification<Reservation> forRoom(Classroom classroom) {
        return (r, cq, cb) -> cb.equal(r.get("room"), classroom);
    }

    static Specification<Reservation> inBuilding(Building building) {

        return (r, cq, cb) -> cb.equal(r.get("room").get("building"), building);
    }

    static Specification<Reservation> scheduleDuring(@Nullable LocalDateTime minTime, @Nullable LocalDateTime maxTime) {

        Assert.isTrue(minTime != null || maxTime != null, "Both minTime and maxTime cannot be null at the same time!");

        return (r, cq, cb) -> {

            if (maxTime == null)
            {
                return cb.greaterThanOrEqualTo(r.get("startTime"), minTime);
            }

            if (minTime == null)
            {
                return cb.lessThanOrEqualTo(r.get("finishTime"), maxTime);
            }

            return cb.and(
                    cb.greaterThanOrEqualTo(r.get("startTime"), minTime),
                    cb.lessThanOrEqualTo(r.get("finishTime"), maxTime)
            );
        };
    }

    static Specification<Reservation> overlappingScheduleForRoom(Classroom classroom, LocalDateTime startTime, LocalDateTime finishTime) {
        return (r, cq, cb) -> cb.and(
                cb.equal(r.get("room"), classroom),
                cb.lessThan(r.get("startTime"), finishTime),
                cb.greaterThan(r.get("finishTime"), startTime)
        );
    }

    //static Specification<Reservation>

}
