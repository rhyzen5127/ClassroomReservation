package org.catcom.classreserver.model.building;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.catcom.classreserver.model.classroom.Classroom;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buildings")
public class Building {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter
    private Integer id;

    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Column(name = "levels", nullable = false)
    @Getter @Setter
    private Integer levels;

    // one-to-many relationship collections

    @OneToMany(targetEntity = Classroom.class, mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Getter
    private List<Classroom> classrooms = new ArrayList<>();

}
