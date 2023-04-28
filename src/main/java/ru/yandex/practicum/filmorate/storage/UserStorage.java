package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

@Component
public interface UserStorage {
    User createUser(User user);

    void deleteUser(User user);

    User editUser(User user);

    ArrayList<User> getAllUsers();

    User getUserByID(int id);
}
