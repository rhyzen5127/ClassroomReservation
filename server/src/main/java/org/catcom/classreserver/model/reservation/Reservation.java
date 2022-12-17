package org.catcom.classreserver.model.reservation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.user.User;

import java.time.LocalDateTime;

import static org.catcom.classreserver.model.reservation.ReservationStatus.*;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @Getter @Setter
    private User owner;

    @ManyToOne()
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    @Getter @Setter
    private Classroom room;

    @Column(name = "booking_time", nullable = false)
    @Getter @Setter
    private LocalDateTime bookingTime; // time when user make a reservation

    @Column(name = "start_time", nullable = false)
    @Getter @Setter
    private LocalDateTime startTime; // time to start using room

    @Column(name = "finish_time", nullable = false)
    @Getter @Setter
    private LocalDateTime finishTime; // time to finish using room

    @Column(name = "status", nullable = false)
    @Getter @Setter
    private String status; // ( pending | approved | rejected | canceled )

    public boolean isPending()
    {
        return PENDING.equalsIgnoreCase(status);
    }

    public boolean isApproved()
    {
        return APPROVED.equalsIgnoreCase(status);
    }

    public boolean isRejected()
    {
        return REJECTED.equalsIgnoreCase(status);
    }

    public boolean isCanceled()
    {
        return CANCELED.equalsIgnoreCase(status);
    }

    public void setPending()
    {
        setStatus(PENDING);
    }

    public void setApproved()
    {
        setStatus(APPROVED);
    }

    public void setRejected()
    {
        setStatus(REJECTED);
    }

    public void setCanceled()
    {
        setStatus(CANCELED);
    }

}
