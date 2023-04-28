package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

@Component
public interface FilmStorage {

    Film createFilm(Film film);

    void deleteFilm(Film film);

    Film editFilm(Film film);

    ArrayList<Film> getAllFilms();

    Film getFilmByID(int id);
}
