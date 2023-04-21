package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.validators.ValidationException;

import javax.validation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> users = new HashMap<>();
    private int idU =1;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос.");
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        if (validate.size() > 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Ошибка валидации");
        }
        validatorFactory.close();
        if (users.containsKey(user.getId())) {
            log.warn("Такой пользователь уже существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Такой пользователь уже есть");
        } else {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(idU);
            users.put(user.getId(), user);
            idU++;
            log.info("Пользователь добавлен");
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> findAll() {

        return users.entrySet()
                .stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    @PutMapping("/users")
    public User saveUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь обновлен");
        } else {
            log.warn("Пользователь не найден");
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return user;
    }

}
