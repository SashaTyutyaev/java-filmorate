package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    private InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User createUser(User user) {
        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    public void deleteUser(User user) {
        inMemoryUserStorage.deleteUser(user);
    }

    public void deleteAllUsers() {
        inMemoryUserStorage.deleteAllUsers();
    }

    public List<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    public Map<Integer, User> getMapOfUsers() {
        return inMemoryUserStorage.getMapOfUsers();
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = getMapOfUsers().get(userId);
        User friend = getMapOfUsers().get(friendId);
        if (user == null) {
            log.debug("Пользователь " + userId + " не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (friend == null) {
            log.debug("Пользователь " + friendId + " не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Добавили друга " + friendId + " пользователю " + userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getMapOfUsers().get(userId);
        User friend = getMapOfUsers().get(friendId);
        if (user == null) {
            log.debug("Пользователь " + userId + " не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (friend == null) {
            log.debug("Пользователь " + friendId + " не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Удалили друга " + friendId + " у пользователя " + userId);
    }

    public List<User> getFriends(Integer userId) {
        if (getMapOfUsers().get(userId) != null) {
            List<User> friendsList = new ArrayList<>();
            for (int friendId : inMemoryUserStorage.getMapOfUsers().get(userId).getFriends()) {
                User friend = inMemoryUserStorage.getMapOfUsers().get(friendId);
                friendsList.add(friend);
            }
            log.info("Список друзей у пользователя " + userId + " - " + friendsList);
            return friendsList;
        } else {
            log.debug("Пользователь " + userId + " не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) {
        User user = getMapOfUsers().get(user1Id);
        User user2 = getMapOfUsers().get(user2Id);
        if (user == null) {
            log.debug("Пользователь " + user1Id + " не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (user2 == null) {
            log.debug("Пользователь " + user2Id + " не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        List<User> commonFriends = new ArrayList<>();

        for (Integer userId : getMapOfUsers().keySet()) {
            if (user.getFriends().contains(userId)
                    && user2.getFriends().contains(userId)) {
                commonFriends.add(getMapOfUsers().get(userId));
            }
        }
        log.info("Список общих друзей у пользователя " + user1Id + " и " + user2Id + " - " + commonFriends);
        return commonFriends;
    }
}
