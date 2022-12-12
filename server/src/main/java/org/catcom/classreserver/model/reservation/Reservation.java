package org.catcom.classreserver.model.reservation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id @GeneratedValue
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

    @Column(nullable = false)
    @Getter @Setter
    private LocalDateTime bookingTime; // time when user make a reservation

    @Column(nullable = false)
    @Getter @Setter
    private LocalDateTime startTime; // time to start using room

    @Column(nullable = false)
    @Getter @Setter
    private LocalDateTime finishTime; // time to finish using room

    @Column(nullable = false)
    @Getter @Setter
    private String status; // ( pending | approved | rejected | canceled )

    public boolean isPending()
    {
        return "pending".equalsIgnoreCase(status);
    }

    public boolean isApproved()
    {
        return "approved".equalsIgnoreCase(status);
    }

    public boolean isRejected()
    {
        return "rejected".equalsIgnoreCase(status);
    }

    public boolean isCanceled()
    {
        return "canceled".equalsIgnoreCase(status);
    }

    public void setApproved()
    {
        setStatus("approved");
    }

    public void setRejected()
    {
        setStatus("rejected");
    }

    public void setCanceled()
    {
        setStatus("canceled");
    }

}
