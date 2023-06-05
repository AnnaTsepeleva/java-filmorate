package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GenreDbStorageImpl implements GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder().id(resultSet.getInt("id"))
                .name(resultSet.getString("name")).build();
    }

    @Override
    public List<Genre> findAllGenre() {
        String sqlQuery = "select * from genre";
        return (List<Genre>) jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "select * from genre where id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
        if (!genres.isEmpty()) {
            return genres.get(0);
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND, "Жанр не найден");
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

    @Override
    public void setGenresListFilmsDB(List<Film> films) {
        List<Integer> listID = new ArrayList<>();
        for (Film film : films) {
            listID.add(film.getId());
        }
        String sep = ",";
        String str = listID.stream().map(Object::toString)
                .collect(Collectors.joining(sep));
        String sqlQueryFilmGenre = "select film_genre.film_id, genre.id, genre.name from film_genre, genre where film_genre.genre_id = genre.id and film_genre.film_id in (" + str + ") order by film_genre.film_id";
        Map<Integer, Film> mapedFilms = films.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity()));

        List<Map<String, Object>> genresList = jdbcTemplate.queryForList(sqlQueryFilmGenre);

        for (Map<String, Object> t : genresList) {
            Film film = mapedFilms.get((Integer) t.get("film_id"));
            Genre genre = Genre.builder()
                    .id((Integer) t.get("id"))
                    .name(t.get("name").toString())
                    .build();
            film.getGenres().add(genre);
        }
    }


}
