package ru.yandex.practicum.filmorate.dao.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    void deleteFilm(Film film);

    Film editFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmByID(int id);

    public void likeFilm(int filmId, int userId);

    public void deleteLike(int filmId, int userId);

    public List<Film> filmRate(int count);

}
