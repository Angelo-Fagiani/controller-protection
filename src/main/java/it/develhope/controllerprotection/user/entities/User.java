package it.develhope.controllerprotection.user.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String surname;

    private boolean isActive = false;

    private String activationCode;

    private String passwordResetCode;

    @Column(unique = true)
    private String email;
    private String password;

    private LocalDateTime jwtCreatedOn;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Role.class)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {
            @JoinColumn(name = "USER_ID") },
            inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID") })
    private Set<Role> roles;

}
