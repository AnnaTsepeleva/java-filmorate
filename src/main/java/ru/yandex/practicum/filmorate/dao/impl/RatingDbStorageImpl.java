package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.storage.RatingDbStorage;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RatingDbStorageImpl implements RatingDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public RatingDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return Rating.builder().id(resultSet.getInt("id")).name(resultSet.getString("name")).build();
    }

    @Override
    public List<Rating> findAllRating() {
        String sqlQuery = "select * from MPA";
        return (ArrayList<Rating>) jdbcTemplate.query(sqlQuery, this::mapRowToRating);

    }

    @Override
    public Rating getRatingById(int id) {
        String sqlQuery = "select * from MPA where id = ?";
        if (!jdbcTemplate.query(sqlQuery, this::mapRowToRating, id).isEmpty())
         {
             return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, id);
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND, "Рейтинг не найден");
    }
}
