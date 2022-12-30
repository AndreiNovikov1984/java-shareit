package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ItemStorage extends JpaRepository<Item, Long> {
    Collection<Item> findAllByOwner(Long owner);

    Optional<Item> findItemById(Long itemId);

    @Query(value = "select it " +
            "from Item as it " +
            "WHERE (LOWER(it.name) like LOWER(concat('%', ?1, '%')) " +
            "OR LOWER(it.description) like LOWER(concat('%', ?1, '%'))) " +
            "AND it.available = true")
    Collection<Item> searchItem(String text);

    Item save(Item item);
}
