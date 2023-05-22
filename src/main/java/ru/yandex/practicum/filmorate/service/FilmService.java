package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        return filmDbStorage.createFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

    public Film editFilm(Film film) {
        return filmDbStorage.editFilm(film);
    }

    public void likeFilm(int filmId, int userId) {
        filmDbStorage.likeFilm(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmDbStorage.deleteLike(filmId, userId);
    }

    public List<Film> filmRate(int count) {
        return filmDbStorage.filmRate(count);
    }

    public Film findFilmById(int id) {
        return filmDbStorage.findFilmById(id);
    }

}
