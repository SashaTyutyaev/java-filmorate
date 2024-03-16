package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    int generatedId = 0;

    private int generateId() {
        return generatedId++;
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Неккоректное название фильма");
            throw new ValidationException("Неккоректное название фильма");
        }
        if (film.getDescription().length() > 200) {
            log.info("Неккоректное описание фильма");
            throw new ValidationException("Неккоректное описание фильма");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            log.info("Неккоректная дата релиза фильма");
            throw new ValidationException("Неккоректная дата релиза фильма");
        }
        if (film.getDuration() <= 0) {
            log.info("Неккоректная длительность фильма");
            throw new ValidationException("Неккоретная длительность фильма");
        }
    }

    @PostMapping("/films/film")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        validateFilm(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Создан фильм - " + film);
        return film;
    }

    @PutMapping("/films/film")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Обновили фильм под идентификатором - " + film.getId());
        return film;
    }

    @GetMapping("/films")
    public Map<Integer, Film> getFilms() {
        log.info("Текущее количестов фильмов - " + films.size());
        return films;
    }
}
