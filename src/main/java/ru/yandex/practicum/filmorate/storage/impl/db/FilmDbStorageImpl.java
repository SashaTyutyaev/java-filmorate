package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class FilmDbStorageImpl implements FilmStorage {

    JdbcTemplate jdbcTemplate;

    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        try {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                    .withSchemaName("public")
                    .withTableName("films")
                    .usingGeneratedKeyColumns("id");
            int filmId = (int) insert.executeAndReturnKey(
                    new MapSqlParameterSource("name", film.getName())
                            .addValue("description", film.getDescription())
                            .addValue("release_date", film.getReleaseDate())
                            .addValue("duration", film.getDuration())
                            .addValue("mpa_id", film.getMpa().getId()));
            film.setId(filmId);

            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                SimpleJdbcInsert insert2 = new SimpleJdbcInsert(jdbcTemplate)
                        .withTableName("film_genre")
                        .usingColumns("film_id", "genre_id");

                for (Genre genre : film.getGenres()) {
                    insert2.execute(new MapSqlParameterSource("film_id", filmId)
                            .addValue("genre_id", genre.getId()));
                }
            }
        } catch (Exception e) {
            log.error("Ошибка в добавлении фильма в БД");
            throw new IncorrectParameterException("Ошибка в добавлении фильма в БД");
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            String sql = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                    "where id = ?";

            jdbcTemplate.update(sql,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());

            String deleteGenresSql = "delete from film_genre where film_id = ?";

            jdbcTemplate.update(deleteGenresSql, film.getId());

            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                        .withTableName("film_genre")
                        .usingColumns("film_id", "genre_id");

                for (Genre genre : film.getGenres()) {
                    insert.execute(new MapSqlParameterSource("film_id", film.getId())
                            .addValue("genre_id", genre.getId()));
                }
            }
        } catch (Exception e) {
            log.error("Ошибка в обновлении фильма в БД");
            throw new IncorrectParameterException("Ошибка в обновлении фильма в БД");
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        try {
            String sql = "select f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, mpa.mpa_name as mpa_name, " +
                    "string_agg(g.id, ', ') as g_ids, string_agg(g.genre_name, ', ') as g_names from films f " +
                    "left join film_genre fg on f.id = fg.film_id " +
                    "left join genre g on fg.genre_id = g.id " +
                    "left join mpa on f.mpa_id = mpa.id " +
                    "group by f.id " +
                    "order by f.id";

            return jdbcTemplate.query(sql, this::mapRow);
        } catch (Exception e) {
            log.error("Ошибка в получении всех фильмов из БД");
            throw new EntityNotFoundException("Ошибка в получении всех фильмов из БД");
        }
    }

    @Override
    public Film getFilmById(Integer id) {
        try {
            String sql = "select f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, mpa.mpa_name as mpa_name, " +
                    "string_agg(g.id, ', ') as g_ids, string_agg(g.genre_name, ', ') as g_names from films f " +
                    "left join film_genre fg on f.id = fg.film_id " +
                    "left join genre g on fg.genre_id = g.id " +
                    "left join mpa on f.mpa_id = mpa.id " +
                    "where f.id = ? " +
                    "group by f.id " +
                    "order by f.id";

            return jdbcTemplate.queryForObject(sql, this::mapRow, id);
        } catch (Exception e) {
            log.error("Ошибка в получении фильма по идентификатору из БД");
            throw new EntityNotFoundException("Неверный ид фильма");
        }
    }

    @Override
    public void deleteAllFilms() {
        try {
            String sql = "delete from films";
            jdbcTemplate.update(sql);
            String sql2 = "delete from film_genre";
            jdbcTemplate.update(sql2);
            String sql4 = "delete from films_users";
            jdbcTemplate.update(sql4);
        } catch (Exception e) {
            log.error("Ошибка в удалении фильма");
            throw new EntityNotFoundException("Ошибка в удалении фильма");
        }
    }

    @Override
    public void deleteFilmById(Integer id) {
        try {
            String sql = "delete from films where id = ?";
            jdbcTemplate.update(sql, id);
            String sql2 = "delete from film_genre where film_id = ?";
            jdbcTemplate.update(sql2, id);
            String sql4 = "delete from films_users where film_id = ?";
            jdbcTemplate.update(sql4, id);
        } catch (Exception e) {
            log.error("Ошибка в удалении фильма по идентификатору");
            throw new EntityNotFoundException("Ошибка в удалении фильма по идентификатору");
        }
    }

    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {

        Film film = new Film();

        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        Integer mpaId = rs.getObject("mpa_id", Integer.class);
        if (mpaId != null && mpaId != 0) {
            String mpaName = rs.getString("mpa_name");
            Mpa mpa = new Mpa(mpaId, mpaName);
            film.setMpa(mpa);
        } else {
            film.setMpa(null);
        }

        String genreIdString = rs.getString("g_ids");
        String genreNamesString = rs.getString("g_names");

        if (genreIdString != null && genreNamesString != null
                && !genreIdString.isEmpty() && !genreNamesString.isEmpty()) {
            String[] ids = genreIdString.split(", ");
            String[] names = genreNamesString.split(", ");

            Set<Genre> genres = new HashSet<>();
            for (int i = ids.length; i >= 0; i--) {
                if (i < names.length) {
                    int genreId = Integer.parseInt(ids[i]);
                    String genreName = names[i];
                    genres.add(new Genre(genreId, genreName));
                }
            }
            film.setGenres(genres);
        }
        String sql = "select user_id from films_users where film_id = ?";
        int likes = jdbcTemplate.queryForList(sql,film.getId()).size();
        film.setLikesCount(likes);

        return film;
    }
}
