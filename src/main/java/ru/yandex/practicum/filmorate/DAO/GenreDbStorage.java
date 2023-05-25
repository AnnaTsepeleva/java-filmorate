package ru.yandex.practicum.filmorate.DAO;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDbStorage {
    List<Genre> findAllGenre();

    Genre getGenreById(int id);

    List<Genre> findGenreByFilm(int filmID);

    void setGenreInDB (int filmID, List<Genre> genres);
}
