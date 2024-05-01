package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {

    void addUserToFriends(int user1Id, int user2Id);

    void deleteUserFromFriends(int user1Id, int user2Id);

    List<User> getFriends(int userId);
}
