package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(Integer id);

    void deleteUser(User user);

    Map<Integer, User> getMapOfUsers();

}
