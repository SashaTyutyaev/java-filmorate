package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(Integer id);

    void deleteFilm(Film film);

    Map<Integer, Film> getMapOfFilms();

}
