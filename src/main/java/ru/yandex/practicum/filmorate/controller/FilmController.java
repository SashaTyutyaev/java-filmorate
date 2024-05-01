package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

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

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        validateFilm(film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateFilm(film);
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @DeleteMapping
    public void deleteFilm(Film film) {
        filmService.deleteFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("Параметр count должен быть больше 0");
        }
        return filmService.getPopularFilms(count);
    }
}
