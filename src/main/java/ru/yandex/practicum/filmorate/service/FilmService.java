package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    InMemoryFilmStorage inMemoryFilmStorage;
    UserService userService;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, UserService userService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.userService = userService;
    }

    Comparator<Film> filmComparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return o2.getLikes().compareTo(o1.getLikes());
        }
    };

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public void deleteFilm(Film film) {
        inMemoryFilmStorage.deleteFilm(film);
    }

    public void deleteAllFilms() {
        inMemoryFilmStorage.deleteAllFilms();
    }

    public List<Film> getFilms() {
        return inMemoryFilmStorage.getFilms();
    }

    public Map<Integer,Film> getMapOfFilms() {
        return inMemoryFilmStorage.getMapOfFilms();
    }

    public void addLike(Integer filmId, Integer userId) {
        User user = userService.getMapOfUsers().get(userId);
        Film film = getMapOfFilms().get(filmId);

        if (user == null) {
            log.debug("Пользователь под идентификатором - " + userId + " не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (film == null) {
            log.debug("Фильм под идентификатором - " + filmId + " не найден");
            throw new FilmNotFoundException("Фильм не найден");
        }

        int listFilmSize = user.getFilms().size();
        int likes;
        user.getFilms().add(filmId);
        if (user.getFilms().size() > listFilmSize) {
            if (film.getLikes() == null) {
                likes = 0;
            } else {
                likes = film.getLikes();
            }
            film.setLikes(likes + 1);
        }
        log.info("Пользователь " + user.getId() + " добавил лайк фильму " + film.getId());
    }

    public void deleteLike(Integer userId, Integer filmId) {
        User user = userService.getMapOfUsers().get(userId);
        Film film = getMapOfFilms().get(filmId);

        if (user == null) {
            log.debug("Пользователь под идентификатором - " + user.getId() + " не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (film == null) {
            log.debug("Фильм под идентификатором - " + film.getId() + " не найден");
            throw new FilmNotFoundException("Фильм не найден");
        }

        if (user.getFilms().contains(film.getId())) {
            user.getFilms().remove(film.getId());
            film.setLikes(film.getLikes() - 1);
            log.info("Пользователь " + user.getId() + " удалил лайк к фильму " + film.getId());
        } else {
            log.info("У пользователя " + user.getId() + " не найдено лайков к фильму " + film.getId());
        }
    }

    public List<Film> getPopularFilms(Integer size) {
        List<Film> popularFilms = new ArrayList<>(getFilms());
        if (popularFilms.isEmpty()) {
            log.debug("Список фильмов пуст");
            throw new FilmNotFoundException("Популярные фильмы не найдены");
        }
        if (size >= popularFilms.size()) {
            size = popularFilms.size();
            log.info("Размер списка популярных фильмов изменен на " + size);
        }

        return popularFilms.stream()
                .filter(film -> film.getLikes() != null)
                .sorted(filmComparator)
                .collect(Collectors.toList());
    }
}
