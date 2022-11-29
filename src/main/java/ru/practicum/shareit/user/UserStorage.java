package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAllUsers();

    User getUserById(long userId);

    User addUser(User user);

    User updateUser(User user);

    User deleteUserById(long userId);
}
