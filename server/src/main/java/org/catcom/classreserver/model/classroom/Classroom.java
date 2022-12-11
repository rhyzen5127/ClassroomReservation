package org.catcom.classreserver.model.classroom;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.catcom.classreserver.model.building.Building;

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

    @Column(nullable = false)
    @Getter @Setter
    private String status;

    @Column
    @Getter @Setter
    private String level;

    @ManyToOne()
    @JoinColumn(name = "building_id", referencedColumnName = "id")
    @Getter @Setter
    private Building building;

}
