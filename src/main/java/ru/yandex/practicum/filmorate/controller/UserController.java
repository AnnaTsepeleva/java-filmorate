package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validators.ValidationException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PutMapping
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        if (userService.findUserById(friendId) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь " + friendId + " не найден");
        }
        if (userService.findUserById(id) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь " + id + " не найден");
        }
        userService.addFriend(userService.findUserById(id), friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        if (userService.findUserById(friendId) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь " + friendId + " не найден");
        }
        if (userService.findUserById(id) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь " + id + " не найден");
        }
        userService.deleteFriend(userService.findUserById(id), friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int id) {
        return userService.getFriends(userService.findUserById(id));
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int id) {
        return userService.findUserById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int idUser, @PathVariable("otherId") int idFriend) {
        return userService.getCommonFriends(idUser, idFriend);
    }
}
