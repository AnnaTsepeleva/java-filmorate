package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.validators.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
public class FilmController {

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap <Integer, Film> films = new HashMap();
    private int idF = 1;

    @PostMapping("/films")
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос.");
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Film>> validate = validator.validate(film);
        if (validate.size() > 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Ошибка валидации");
        }
        validatorFactory.close();

        if (films.containsKey(film.getId())) {
            log.warn("Такой фильм уже существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Такой фильм уже есть");
        } else {
            film.setId(idF);
            films.put(film.getId(), film);
            log.info("Фильм добавлен");
            idF++;
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return films.entrySet()
                .stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    @PutMapping("/films")
    public Film saveFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм обновлен");
        } else {
            log.warn("Фильм не найден");
            throw new ValidationException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        return film;
    }

}
