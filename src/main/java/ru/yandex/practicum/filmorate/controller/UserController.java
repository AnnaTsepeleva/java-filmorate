package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.validators.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> users = new HashMap<>();
    private int idU = 1;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос.");
        if (users.containsKey(user.getId())) {
            log.warn("Такой пользователь уже существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Такой пользователь уже есть");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(idU++);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User saveUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь не найден");
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        users.put(user.getId(), user);
        log.info("Пользователь обновлен");
        return user;
    }
}
