package ru.yandex.practicum.filmorate.DAO.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAO.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreDbStorageImpl implements GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder().id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public List<Genre> findAllGenre() {
        String sqlQuery = "select * from genre";
        return (List<Genre>) jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "select * from genre where id = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Жанр не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> findGenreByFilm(int filmID) {
        String sqlQuery = "select * from genre as g inner join film_genre as fg on fg.genre_id = g.id where fg.film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmID);
    }

    @Override
    public void setGenreInDB(int filmID, List<Genre> genres) {
        String sqlQuery3 = "delete from film_genre where film_id =?";
        jdbcTemplate.update(sqlQuery3, filmID);
        if (!genres.isEmpty()) {
            Set<Genre> genreSet = new HashSet<>(genres);
            List<Genre> genreWODouble = new ArrayList<>(genreSet);
            jdbcTemplate.batchUpdate("INSERT INTO film_genre (film_id, genre_id) VALUES(?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    preparedStatement.setString(1, String.valueOf(filmID));
                    preparedStatement.setString(2, String.valueOf(genreWODouble.get(i).getId()));
                }

                @Override
                public int getBatchSize() {
                    return genreWODouble.size();
                }
            });

        }
    }
}
