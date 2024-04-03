package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Film film) {
        filmStorage.deleteFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer id) {
        if (getMapOfFilms().get(id) == null) {
            log.info("Фильм под идентификатором - " + id + " не найден");
            throw new EntityNotFoundException("Фильм не найден");
        }
        return filmStorage.getFilmById(id);
    }

    public Map<Integer, Film> getMapOfFilms() {
        return filmStorage.getMapOfFilms();
    }

    public void addLike(Integer filmId, Integer userId) {
        User user = userService.getUserById(userId);
        Film film = getFilmById(filmId);
        int likes;
        if (!user.getLikedFilms().contains(filmId)) {
            user.getLikedFilms().add(filmId);
            if (film.getLikesCount() == null) {
                likes = 0;
            } else {
                likes = film.getLikesCount();
            }
            film.setLikesCount(likes + 1);
            log.info("Пользователь " + user.getId() + " добавил лайк фильму " + film.getId());
        }
    }


    public void deleteLike(Integer userId, Integer filmId) {
        User user = userService.getUserById(userId);
        Film film = getFilmById(filmId);
        if (user.getLikedFilms().contains(film.getId())) {
            user.getLikedFilms().remove(film.getId());
            film.setLikesCount(film.getLikesCount() - 1);
            log.info("Пользователь " + user.getId() + " удалил лайк к фильму " + film.getId());
        } else {
            log.info("У пользователя " + user.getId() + " не найдено лайков к фильму " + film.getId());
        }
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
