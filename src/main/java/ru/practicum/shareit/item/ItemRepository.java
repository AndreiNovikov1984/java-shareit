package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwnerId(Long owner, Pageable page);

    @Query(value = "select it " +
            "from Item as it " +
            "WHERE (LOWER(it.name) like LOWER(concat('%', ?1, '%')) " +
            "OR LOWER(it.description) like LOWER(concat('%', ?1, '%'))) " +
            "AND it.available = true")
    Page<Item> searchItem(String text, Pageable page);

    Collection<Item> findAllByRequestId(Long request);


}
