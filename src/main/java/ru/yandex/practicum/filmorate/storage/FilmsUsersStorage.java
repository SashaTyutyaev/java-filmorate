package ru.yandex.practicum.filmorate.storage;

public interface FilmsUsersStorage {

    int getLikes(int filmId);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);
}
