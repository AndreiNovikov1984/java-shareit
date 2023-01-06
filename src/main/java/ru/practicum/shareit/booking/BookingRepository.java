package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.TypeStatus;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerIdOrderByStartDesc(long userID, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.booker.id = ?1 and ?2 between b.start and  b.end " +
            "order by b.start desc ")
    Page<Booking> findCurrentByBooker(long userID, Timestamp now, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.booker.id = ?1 and b.end < ?2 " +
            "order by b.start desc ")
    Page<Booking> findPastByBooker(long userID, Timestamp now, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.booker.id = ?1 and b.start > ?2 " +
            "order by b.start desc ")
    Page<Booking> findFutureByBooker(long userID, Timestamp now, Pageable page);

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userID, TypeStatus status, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.item.owner.id = ?1 " +
            "order by b.start desc ")
    Page<Booking> findAllByOwnerOrderByStartDesc(long userID, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.item.owner.id = ?1 and ?2 between b.start and  b.end " +
            "order by b.start desc ")
    Page<Booking> findCurrentByOwner(long userID, Timestamp now, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.item.owner.id = ?1 and b.end < ?2 " +
            "order by b.start desc ")
    Page<Booking> findPastByOwner(long userID, Timestamp now, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.item.owner.id = ?1 and b.start > ?2 " +
            "order by b.start desc ")
    Page<Booking> findFutureByOwner(long userID, Timestamp now, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.item.owner.id = ?1 and b.status = ?2 " +
            "order by b.start desc ")
    Page<Booking> findAllByOwnerAndStatusOrderByStartDesc(long userID, TypeStatus status, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.item.id = ?1 and b.end < ?2 and b.status = 'APPROVED' " +
            "order by b.start desc ")
    Optional<Booking> findLastByItem(long itemId, Timestamp now);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.item.id = ?1 and b.start > ?2 and b.status = 'APPROVED' " +
            "order by b.start asc ")
    Optional<Booking> findNextByItem(long itemId, Timestamp now);

    @Query(value = "select b " +
            "from Booking as b " +
            "WHERE b.item.id = ?1 and b.booker.id = ?2 " +
            "and b.status = 'APPROVED' and  b.end < ?3 " +
            "order by b.start asc ")
    Optional<Booking> findBokingByItemAndOwnerValidate(long itemId, long userID, Timestamp now);
}
