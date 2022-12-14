package org.catcom.classreserver.model.reservation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepos extends CrudRepository<Reservation, Integer>
{
    @Query("SELECT r FROM Reservation r WHERE r.room.id = ?1")
    Iterable<Reservation> findByRoomId(int roomId);

    @Query("SELECT r FROM Reservation r WHERE r.owner.id = ?1")
    Iterable<Reservation> findByOwnerId(int ownerId);

    @Query("SELECT r FROM Reservation r WHERE r.owner.id = ?1 AND r.status = ?2")
    Iterable<Reservation> findByOwnerIdAndStatus(int ownerId, String status);

    @Query("SELECT r FROM Reservation r WHERE r.status = ?1")
    Iterable<Reservation> findByStatus(String status);

}
