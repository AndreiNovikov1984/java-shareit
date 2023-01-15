package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * TODO Sprint add-bookings.
 */
@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long id;
    @Column(name = "start_date", nullable = false)
    public Timestamp start;
    @Column(name = "end_date", nullable = false)
    public Timestamp end;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    public Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", nullable = false)
    public User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public TypeStatus status;
}
