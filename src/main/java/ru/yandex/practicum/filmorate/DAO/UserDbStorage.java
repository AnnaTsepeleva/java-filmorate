package ru.yandex.practicum.filmorate.DAO;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserDbStorage {

    User findUserById(int id);

    User createUser(User user);

    User editUser(User user);

    ArrayList<User> getAllUsers();

}
