package org.catcom.classreserver.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

}
