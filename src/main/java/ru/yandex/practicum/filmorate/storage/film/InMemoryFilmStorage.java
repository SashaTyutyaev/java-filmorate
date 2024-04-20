package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

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

    @Override
    public Film createFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Создан фильм - " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм в списке отсутсвует");
            throw new EntityNotFoundException("Фильм в списке отсуствует");
        }
        films.put(film.getId(), film);
        log.info("Обновили фильм под идентификатором - " + film.getId());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        log.info("Текущее количестов фильмов - " + films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer id) {
        return films.get(id);
    }


    @Override
    public void deleteFilm(Film film) {
        log.info("Фильм под идентификатором - " + film.getId() + " удален");
        films.remove(film.getId());
    }

}
