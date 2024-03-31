package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int generatedId = 1;

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

    @Override
    public Film createFilm(@RequestBody Film film) {
        validateFilm(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Создан фильм - " + film);
        return film;
    }

    @Override
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм в списке отсутсвует");
            throw new ValidationException("Фильм в списке отсуствует");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Обновили фильм под идентификатором - " + film.getId());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        log.info("Текущее количестов фильмов - " + films.size());
        return new ArrayList<>(films.values());
    }

    public Map<Integer,Film> getMapOfFilms() {
        return films;
    }

    @Override
    public void deleteFilm(Film film) {
        log.info("Фильм под идентификатором - " + film.getId() + " удален");
        films.remove(film.getId());
    }

    @Override
    public void deleteAllFilms() {
        log.info("Удалены все фильмы");
        films.clear();
    }
}
