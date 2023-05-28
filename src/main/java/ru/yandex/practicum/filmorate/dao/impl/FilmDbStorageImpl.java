package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.storage.FilmStorage;
import ru.yandex.practicum.filmorate.dao.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.storage.RatingDbStorage;
import ru.yandex.practicum.filmorate.dao.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final RatingDbStorage ratingDbStorage;
    private final UserStorage userDbStorage;

    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, RatingDbStorage ratingDbStorage, @Qualifier("userDbStorageImpl") UserStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.ratingDbStorage = ratingDbStorage;
        this.userDbStorage = userDbStorage;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder().id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getLong("duration"))
                .releaseDate(resultSet.getDate("release_Date").toLocalDate())
                .mpa(new Rating(resultSet.getInt("MPA.id"), resultSet.getString("MPA.name")))
                .build();
    }

    @Override
    public Film getFilmByID(int id) {
        String sqlQuery = "select * from films, MPA where films.rating_id = MPA.id and films.id = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id);
        if (!films.isEmpty()) {
            Film film = films.get(0);
            film.setGenres(genreDbStorage.findGenreByFilm(id));
            return film;
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND, "Фильм не найден");
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into films(name, description, release_date, duration, rating_id) values (?, ?, ?, ?, ?)";
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
        List<Genre> genreList = film.getGenres();
        genreDbStorage.setGenreInDB(film.getId(), genreList);
        return getFilmByID(film.getId());
    }

    @Override
    public void deleteFilm(Film film) {
        String sqlQuery = "delete from films where id =?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQueryFilms = "select * from films, MPA  where films.rating_id = MPA.id";
        List<Film> films = jdbcTemplate.query(sqlQueryFilms, this::mapRowToFilm);
        genreDbStorage.setGenresListFilmsDB(films);
        return films;
    }

    @Override
    public Film editFilm(Film film) {
        getFilmByID(film.getId());
        String sqlQuery = "update films set name = ?, description = ?, duration = ?, release_date = ?, rating_id =? where id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(), film.getMpa().getId(), film.getId());
        List<Genre> genreList = film.getGenres();
        genreDbStorage.setGenreInDB(film.getId(), genreList);
        return getFilmByID(film.getId());
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        getFilmByID(filmId);
        userDbStorage.getUserByID(userId);
        String sqlQuery = "merge into film_likes (film_id, user_id, date_like) key (film_id) values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId, LocalDateTime.now());
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        getFilmByID(filmId);
        userDbStorage.getUserByID(userId);
        String sqlQuery = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> filmRate(int count) {
        String sqlQuery = "select * from films join MPA on films.rating_id = MPA.id left join film_likes on film_likes.film_id = films.id group by films.name order by count(film_likes.film_id) desc limit ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
        genreDbStorage.setGenresListFilmsDB(films);
        return films;
    }
}
