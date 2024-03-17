package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    int generatedId = 1;

    private int generateId() {
        return generatedId++;
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film == null) {
            log.info("Пустые поля фильма");
            throw new ValidationException("Пустые поля фильма");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Неккоректное название фильма");
            throw new ValidationException("Неккоректное название фильма");
        }
        if (film.getDescription().length() > 200) {
            log.info("Неккоректное описание фильма");
            throw new ValidationException("Неккоректное описание фильма");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Неккоректная дата релиза фильма");
            throw new ValidationException("Неккоректная дата релиза фильма");
        }
        if (film.getDuration() <= 0) {
            log.info("Неккоректная длительность фильма");
            throw new ValidationException("Неккоретная длительность фильма");
        }
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        validateFilm(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Создан фильм - " + film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм в списке отсутсвует");
            throw new ValidationException("Фильм в списке отсуствует");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Обновили фильм под идентификатором - " + film.getId());
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Текущее количестов фильмов - " + films.size());
        return new ArrayList<>(films.values());
    }
}
