package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "ITEMS", schema = "public")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    public long id;
    @Column(name = "ITEM_NAME", nullable = false)
    public String name;
    @Column(name = "DESCRIPTION", nullable = false)
    public String description;
    @Column(name = "AVAILABLE", nullable = false)
    public Boolean available;
    @Column(name = "OWNER_ID", nullable = false)
    public long owner;
    @Column(name = "REQUEST")
    public long request;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Comment> comments;
}
