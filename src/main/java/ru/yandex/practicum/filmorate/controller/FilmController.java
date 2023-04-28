package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validators.ValidationException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.getAllFilms();
    }

    @PutMapping
    public Film saveFilm(@Valid @RequestBody Film film) {
        return filmService.editFilm(film);
    }

    @DeleteMapping
    public void deleteFilm(@RequestBody Film film) {
        filmService.deleteFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        if (filmService.findFilmById(filmId) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Фильм " + filmId + " не найден");
        }
        if (userService.findUserById(userId) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь " + userId + " не найден");
        }
        filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        if (filmService.findFilmById(filmId) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Фильм " + filmId + " не найден");
        }
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> filmRate(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        return filmService.filmRate(count);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") int id) {
        return filmService.findFilmById(id);
    }
}
