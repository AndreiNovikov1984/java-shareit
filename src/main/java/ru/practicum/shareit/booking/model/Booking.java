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
@Table(name = "BOOKINGS", schema = "public")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID")
    public long id;
    @Column(name = "START_DATE", nullable = false)
    public Timestamp start;
    @Column(name = "END_DATE", nullable = false)
    public Timestamp end;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    public Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKER_ID", nullable = false)
    public User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    public TypeStatus status;
}
