package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.NotFoundException;
import ru.yandex.practicum.filmorate.validators.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> users = new HashMap<>();
    private int idU = 1;

    @Override
    public User createUser(User user) {
        log.info("Получен запрос.");
        if (users.containsKey(user.getId())) {
            log.warn("Такой пользователь уже существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Такой пользователь уже есть");
        }
        if (user.getName() == null || user.getName() == "") {
            user.setName(user.getLogin());
        }
        user.setId(idU++);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public User editUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь не найден");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        users.put(user.getId(), user);
        log.info("Пользователь обновлен");
        return user;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserByID(int id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь не найден");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return users.get(id);
    }
}
