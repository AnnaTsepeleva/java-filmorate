package ru.yandex.practicum.filmorate.DAO.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAO.FilmDbStorage;
import ru.yandex.practicum.filmorate.DAO.GenreDbStorage;
import ru.yandex.practicum.filmorate.DAO.RatingDbStorage;
import ru.yandex.practicum.filmorate.DAO.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Component
public class FilmDbStorageImpl implements FilmDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final RatingDbStorage ratingDbStorage;
    private final UserDbStorage userDbStorage;

    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, RatingDbStorage ratingDbStorage, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.ratingDbStorage = ratingDbStorage;
        this.userDbStorage = userDbStorage;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder().id(resultSet.getInt("id")).name(resultSet.getString("name")).description(resultSet.getString("description")).duration(resultSet.getLong("duration")).releaseDate(resultSet.getDate("release_Date").toLocalDate()).mpa(ratingDbStorage.getRatingById(resultSet.getInt("rating_id"))).build();
    }

    @Override
    public Film findFilmById(int id) {
        String sqlQuery = "select id, name, description, duration, release_date, rating_id " + "from films where id = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        String sqlQuery2 = "select genre_id from film_genre where film_id = ?";
        List<Integer> listGenre = jdbcTemplate.queryForList(sqlQuery2, Integer.class, film.getId());
        Set<Genre> genres = film.getGenres();
        for (int genre : listGenre) {
            genres.add(genreDbStorage.getGenreById(genre));
        }
        film.setGenres(genres);
        return film;

    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into films(name, description, release_date, duration, rating_id) " + "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId((int) keyHolder.getKey().longValue());
        Set<Genre> genreList = film.getGenres();
        if (!genreList.isEmpty()) {
            for (Genre genre : genreList) {
                String sqlQuery2 = "merge into film_genre(film_id, genre_id) key (film_id) " + "values (?, ?)";
                jdbcTemplate.update(sqlQuery2, film.getId(), genre.getId());
            }
        }
        return findFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select id, name, description, duration, release_date, rating_id from films";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        for (Film film : films) {
            String sqlQuery2 = "select genre_id from film_genre where film_id = ?";
            List<Integer> listGenre = jdbcTemplate.queryForList(sqlQuery2, Integer.class, film.getId());
            Set<Genre> genres = film.getGenres();
            for (int genre : listGenre) {
                genres.add(genreDbStorage.getGenreById(genre));
            }
        }
        return films;
    }

    @Override
    public Film editFilm(Film film) {
        findFilmById(film.getId());
        String sqlQuery = "update films set " + "name = ?, description = ?, duration = ?, release_date = ?, rating_id =? " + "where id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(), film.getMpa().getId(), film.getId());
        Set<Genre> genreList = film.getGenres();
        String sqlQuery3 = "delete from film_genre where film_id =?";
        jdbcTemplate.update(sqlQuery3, film.getId());
        if (!genreList.isEmpty()) {
            for (Genre genre : genreList) {
                String sqlQuery2 = "insert into film_genre(film_id, genre_id) " + "values (?, ?)";
                jdbcTemplate.update(sqlQuery2, film.getId(), genre.getId());
            }
        }
        return findFilmById(film.getId());
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        findFilmById(filmId);
        userDbStorage.findUserById(userId);
        String sqlQuery = "merge into film_likes (film_id, user_id, date_like) key (film_id) " + "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId, LocalDateTime.now());
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        findFilmById(filmId);
        userDbStorage.findUserById(userId);
        String sqlQuery = "delete from film_likes " + "where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> filmRate(int count) {
        String sqlQuery = "select id, name, description, duration, release_date, rating_id from films as f left join film_likes as fl on fl.film_id = f.id " + "group by f.name " + "order by count(fl.film_id) desc " + "limit ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
        for (Film film : films) {
            String sqlQuery2 = "select genre_id from film_genre where film_id = ?";
            List<Integer> listGenre = jdbcTemplate.queryForList(sqlQuery2, Integer.class, film.getId());
            Set<Genre> genres = film.getGenres();
            for (int genre : listGenre) {
                genres.add(genreDbStorage.getGenreById(genre));
            }
        }
        return films;
    }
}
