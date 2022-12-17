package org.catcom.classreserver.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.catcom.classreserver.model.classroom.Classroom;
import org.catcom.classreserver.model.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    @Getter @Setter
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    @Getter @Setter
    private String password;

    @Column(name = "first_name", nullable = false)
    @Getter @Setter
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Getter @Setter
    private String lastName;

    @Column(name = "role", nullable = false)
    @Getter @Setter
    private String role;

    @Column(name = "enabled", nullable = false)
    @Getter @Setter
    private Boolean enabled;

    // one-to-many relationship collections

    @OneToMany(targetEntity = Reservation.class, mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

}
