package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStorage extends JpaRepository<User, Long> {
    List<User> findAll();

    Optional<User> findUserById(long userId);

    User save(User user);

    User deleteById(long userId);
}
