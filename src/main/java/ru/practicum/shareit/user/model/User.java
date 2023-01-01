package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

}
