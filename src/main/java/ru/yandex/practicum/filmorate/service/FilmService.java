package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.FilmsUsersDbStorageImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final FilmsUsersDbStorageImpl filmsUsersDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorageImpl") FilmStorage filmStorage, UserService userService, FilmsUsersDbStorageImpl filmsUsersDbStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.filmsUsersDbStorage = filmsUsersDbStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        if (getFilmById(film.getId()) == null) {
            throw new EntityNotFoundException("Фильм отсуствует в БД");
        } else {
            return filmStorage.updateFilm(film);
        }
    }

    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }

    public void deleteFilmById(Integer id) {
        filmStorage.deleteFilmById(id);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(Integer filmId, Integer userId) {

        if (userService.getUserById(userId) == null) {
            log.info("Пользователь под идентификатором - " + userId + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }

        if (getFilmById(filmId) == null) {
            log.info("Фильм под идентификатором - " + filmId + " не найден");
            throw new EntityNotFoundException("Фильм не найден");
        }

        filmsUsersDbStorage.addLike(filmId, userId);
        log.info("Пользователь " + userId + " добавил лайк фильму " + filmId);
    }

    public void deleteLike(Integer userId, Integer filmId) {

        if (userService.getUserById(userId) == null) {
            log.info("Пользователь под идентификатором - " + userId + " не найден");
            throw new EntityNotFoundException("Пользователь не найден");
        }

        if (getFilmById(filmId) == null) {
            log.info("Фильм под идентификатором - " + filmId + " не найден");
            throw new EntityNotFoundException("Фильм не найден");
        }


        filmsUsersDbStorage.deleteLike(filmId, userId);
        log.info("Пользователь " + userId + " удалил лайк к фильму " + filmId);
    }

    public int getLikes(int filmId) {
        return filmsUsersDbStorage.getLikes(filmId);
    }

    public List<Film> getPopularFilms(Integer size) {
        Comparator<Film> filmComparator = (o1, o2) -> o2.getLikesCount().compareTo(o1.getLikesCount());

        List<Film> popularFilms = new ArrayList<>(getFilms());
        if (popularFilms.isEmpty()) {
            log.info("Список фильмов пуст");
            throw new EntityNotFoundException("Популярные фильмы не найдены");
        }
        if (size >= popularFilms.size()) {
            size = popularFilms.size();
            log.info("Размер списка популярных фильмов изменен на " + size);
        }

        return popularFilms.stream()
                .filter(film -> film.getLikesCount() != null)
                .sorted(filmComparator)
                .collect(Collectors.toList());
    }
}

