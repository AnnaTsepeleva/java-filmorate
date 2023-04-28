package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validators.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserStorage userStorage = new InMemoryUserStorage();

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public List<User> findAll() {
        return userStorage.getAllUsers();
    }

    public User saveUser(User user) {
        return userStorage.editUser(user);
    }

    public User findUserById(int id) {
        if (userStorage.getUserByID(id) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return userStorage.getUserByID(id);
    }

    public void addFriend(User user, int friendId) {
        user.getFriends().add(friendId);
        userStorage.getUserByID(friendId).getFriends().add(user.getId());
    }

    public void deleteFriend(User user, int friendId) {
        Set<Integer> friends = user.getFriends();
        user.getFriends().remove(friendId);
        userStorage.getUserByID(friendId).getFriends().remove(user.getId());
    }

    public List<User> getFriends(User user) {
        ArrayList<User> friendsList = new ArrayList<>();
        Set<Integer> friends = user.getFriends();
        if (friends.isEmpty()) {
            throw new ValidationException(HttpStatus.OK, "Список друзей пуст");
        }
        for (int friend : friends) {
            friendsList.add(userStorage.getUserByID(friend));
        }
        return friendsList;
    }

    public List<User> getCommonFriends(int idUser, int idFriend) {
        ArrayList<User> commonFriendsList = new ArrayList<>();
        User user = userStorage.getUserByID(idUser);
        User friend = userStorage.getUserByID(idFriend);
        Set<Integer> friends = user.getFriends();
        for (int friendID : friends) {
            if (friend.getFriends().contains(friendID))
                commonFriendsList.add(userStorage.getUserByID(friendID));
        }
        return commonFriendsList;
    }

}
