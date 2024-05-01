package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class MpaDbStorageImpl implements MpaStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        try {
            String sql = "select id, mpa_name from mpa order by id";
            return jdbcTemplate.query(sql, this::mapRow);
        } catch (Exception e) {
            log.error("Ошибка в получении списка mpa из БД");
            throw new EntityNotFoundException("Ошибка в получении списка mpa из БД");
        }
    }

    @Override
    public Mpa getById(int id) {
        try {
            String sql = "select id, mpa_name from mpa where id = ? order by id";
            return jdbcTemplate.queryForObject(sql, this::mapRow, id);
        } catch (Exception e) {
            log.error("Ошибка в получении mpa по идентификатору из БД");
            throw new EntityNotFoundException("Ошибка в получении mpa по идентификатору из БД");
        }
    }

    private Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {

        Mpa mpa = new Mpa();

        mpa.setId(rs.getInt("id"));
        mpa.setName(rs.getString("mpa_name"));

        return mpa;
    }
}
