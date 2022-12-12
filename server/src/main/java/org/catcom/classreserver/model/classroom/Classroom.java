package org.catcom.classreserver.model.classroom;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;
import org.catcom.classreserver.model.building.Building;
import org.catcom.classreserver.model.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classrooms")
public class Classroom
{
    @Id @GeneratedValue
    @Getter @Setter
    private Integer id;

    @Column(nullable = false)
    @Getter @Setter
    private String name;

    @Column
    @Getter @Setter
    private String level;

    @Column(nullable = false)
    @Getter @Setter
    private String status;

    // width in meter unit (m)
    @Column
    @Getter @Setter
    private Integer width;

    // height in meter unit (m)
    @Column
    @Getter @Setter
    private Integer height;

    @Column
    @Getter @Setter
    private Integer seats;

    @ManyToOne()
    @JoinColumn(name = "building_id", referencedColumnName = "id")
    @Getter @Setter
    private Building building;

    // one-to-many relationship collections

    @OneToMany(targetEntity = Reservation.class, mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    public boolean isReady()
    {
        return "ready".equalsIgnoreCase(status);
    }

}