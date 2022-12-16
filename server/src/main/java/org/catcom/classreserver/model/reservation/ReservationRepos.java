package org.catcom.classreserver.model.reservation;

import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.user.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepos extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation>
{
    @Query("SELECT r FROM Reservation r WHERE r.room.id = ?1")
    Iterable<Reservation> findByRoomId(int roomId);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = ?1 AND r.status = ?2")
    Iterable<Reservation> findByRoomIdAndStatus(int ownerId, String status);

    @Query("SELECT r FROM Reservation r WHERE r.owner.id = ?1")
    Iterable<Reservation> findByOwnerId(int ownerId);

    @Query("SELECT r FROM Reservation r WHERE r.owner.id = ?1 AND r.status = ?2")
    Iterable<Reservation> findByOwnerIdAndStatus(int ownerId, String status);

    @Query("SELECT r FROM Reservation r WHERE r.status = ?1")
    Iterable<Reservation> findByStatus(String status);

    static Specification<Reservation> hasOwner(User user) {
        return (r, cq, cb) -> cb.equal(r.get("owner"), user);
    }

    static Specification<Reservation> hasStatus(String status) {
        return (r, cq, cb) -> cb.equal(r.get("status"), status);
    }

    static Specification<Reservation> forRoom(Classroom classroom) {
        return (r, cq, cb) -> cb.equal(r.get("room"), classroom);
    }

}
