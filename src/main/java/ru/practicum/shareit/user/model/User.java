package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "USERS", schema = "public")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false, updatable = false, unique = true)
    private long id;
    @Column(name = "USER_NAME", nullable = false)
    private String name;
    @Column(name = "USER_EMAIL", nullable = false, unique = true)
    private String email;

}
