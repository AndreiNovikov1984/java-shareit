package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    public Collection<User> getAllUsers();

    public User getUserById(long userId);

    public User addUser(User user);

    public User updateUser(User user);

    public void deleteUserById(long userId);
}
