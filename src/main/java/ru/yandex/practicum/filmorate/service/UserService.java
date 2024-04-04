package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage inMemoryUserStorage;

    public User createUser(User user) {
        if (user == null) {
            log.info("Пользователь " + user.getId() + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }
        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (user == null) {
            log.info("Пользователь " + user.getId() + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }
        return inMemoryUserStorage.updateUser(user);
    }

    public void deleteUser(User user) {
        if (user == null) {
            log.info("Пользователь " + user.getId() + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }
        inMemoryUserStorage.deleteUser(user);
    }

    public List<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    public User getUserById(Integer id) {
        return inMemoryUserStorage.getUserById(id);
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Добавили друга " + friendId + " пользователю " + userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Удалили друга " + friendId + " у пользователя " + userId);
    }

    public List<User> getFriends(Integer userId) {
        if (getUserById(userId) != null) {
            List<User> friendsList = new ArrayList<>();
            for (int friendId : getUserById(userId).getFriends()) {
                User friend = getUserById(friendId);
                friendsList.add(friend);
            }
            log.info("Список друзей у пользователя " + userId + " - " + friendsList);
            return friendsList;
        } else {
            log.info("Пользователь " + userId + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) {
        User user = getUserById(user1Id);
        User user2 = getUserById(user2Id);
        List<User> commonFriends = new ArrayList<>();

        for (User userInList : getUsers()) {
            if (user.getFriends().contains(userInList.getId())
                    && user2.getFriends().contains(userInList.getId())) {
                commonFriends.add(getUserById(userInList.getId()));
            }
        }
        log.info("Список общих друзей у пользователя " + user1Id + " и " + user2Id + " - " + commonFriends);
        return commonFriends;
    }
}
