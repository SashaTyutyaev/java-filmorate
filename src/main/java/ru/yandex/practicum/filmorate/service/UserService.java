package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorageImpl") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public User createUser(User user) {
        if (user == null) {
            log.info("Пользователь " + user.getId() + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (getUserById(user.getId()) == null) {
            log.info("Пользователь " + user.getId() + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        } else {
            return userStorage.updateUser(user);
        }
    }

    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    public void deleteUserById(Integer id) {
        if (getUsers().get(id) == null) {
            log.info("Пользователь " + id + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }
        userStorage.deleteUserById(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(Integer userId, Integer friendId) {

        if (getUserById(userId) == null) {
            log.info("Пользователь под идентификатором - " + userId + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }
        if (getUserById(friendId) == null) {
            log.info("Пользователь под идентификатором - " + friendId + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }

        friendshipStorage.addUserToFriends(userId, friendId);
        log.info("Добавили друга " + friendId + " пользователю " + userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {

        if (getUserById(userId) == null) {
            log.info("Пользователь под идентификатором - " + userId + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }
        if (getUserById(friendId) == null) {
            log.info("Пользователь под идентификатором - " + friendId + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }

        friendshipStorage.deleteUserFromFriends(userId, friendId);
        log.info("Удалили друга " + friendId + " у пользователя " + userId);
    }

    public List<User> getFriends(Integer userId) {
        if (getUserById(userId) == null) {
            throw new EntityNotFoundException("Пользователь отсуствует в БД");
        } else {
            return friendshipStorage.getFriends(userId);
        }
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) {
        List<User> user1List = friendshipStorage.getFriends(user1Id);
        List<User> user2List = friendshipStorage.getFriends(user2Id);

        List<User> commonFriends = new ArrayList<>();

        for (User userInList : getUsers()) {
            if (user1List.contains(userInList)
                    && user2List.contains(userInList)) {
                commonFriends.add(userInList);
            }
        }
        log.info("Список общих друзей у пользователя " + user1Id + " и " + user2Id + " - " + commonFriends);
        return commonFriends;
    }

}
