package org.catcom.classreserver.model.classroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.catcom.classreserver.model.building.Building;
import org.catcom.classreserver.model.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;

import static org.catcom.classreserver.model.classroom.ClassroomStatus.*;

@Entity
@Table(name = "classrooms")
public class Classroom
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter
    private Integer id;

    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Column(name = "level")
    @Getter @Setter
    private String level;

    @Column(name = "status", nullable = false)
    @Getter @Setter
    private String status;

    // width in meter unit (m)
    @Column(name = "width")
    @Getter @Setter
    private Integer width;

    // length in meter unit (m)
    @Column(name = "length")
    @Getter @Setter
    private Integer length;

    @Column(name = "seats")
    @Getter @Setter
    private Integer seats;

    @Column(name = "note")
    @Getter @Setter
    private String note;

    @ManyToOne()
    @JoinColumn(name = "building_id", referencedColumnName = "id")
    @Getter @Setter
    private Building building;

    // one-to-many relationship collections

    @OneToMany(targetEntity = Reservation.class, mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter
    private List<Reservation> reservations = new ArrayList<>();

    public boolean isReady()
    {
        return READY.equalsIgnoreCase(status);
    }

}
