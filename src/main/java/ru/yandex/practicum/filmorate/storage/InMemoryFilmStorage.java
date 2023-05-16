package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.NotFoundException;
import ru.yandex.practicum.filmorate.validators.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int idF = 1;

    @Override
    public Film createFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.warn("Такой фильм уже существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Такой фильм уже есть");
        }
        film.setId(idF++);
        films.put(film.getId(), film);
        log.info("Фильм добавлен {}", film);
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм не найден");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        films.remove(film.getId(), film);
        log.info("Фильм удален", film);
    }

    @Override
    public Film editFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм не найден");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        films.put(film.getId(), film);
        log.info("Фильм обновлен {}", film);
        return film;
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmByID(int id) {
        if (!films.containsKey(id)) {
            log.warn("Фильм не найден");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        return films.get(id);
    }
}
