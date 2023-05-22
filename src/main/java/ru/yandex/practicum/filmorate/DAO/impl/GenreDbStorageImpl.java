package ru.yandex.practicum.filmorate.DAO.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAO.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDbStorageImpl implements GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder().id(resultSet.getInt("id")).name(resultSet.getString("name")).build();
    }

    @Override
    public List<Genre> findAllGenre() {
        String sqlQuery = "select id, name from genre";
        return (ArrayList<Genre>) jdbcTemplate.query(sqlQuery, this::mapRowToGenre);

    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "select id, name " + "from genre where id = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Жанр не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);

    }
}
