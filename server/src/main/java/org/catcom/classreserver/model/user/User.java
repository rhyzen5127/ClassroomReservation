package org.catcom.classreserver.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Integer id;

    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String email;

    @Column(nullable = false)
    @Getter @Setter
    private String password;

    @Column(nullable = false)
    @Getter @Setter
    private String firstName;

    @Column(nullable = false)
    @Getter @Setter
    private String lastName;

    @Column(nullable = false)
    @Getter @Setter
    private String role;

    @Column(nullable = false)
    @Getter @Setter
    private Boolean enabled;

    // one-to-many relationship collections

    @OneToMany(targetEntity = Reservation.class, mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

}
