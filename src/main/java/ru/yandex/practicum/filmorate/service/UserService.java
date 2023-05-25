package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    private final UserStorage userDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorageImpl") UserStorage userDbStorage, FriendshipDbStorage friendshipDbStorage) {
        this.friendshipDbStorage = friendshipDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public User createUser(User user) {
        return userDbStorage.createUser(user);
    }

    public List<User> findAll() {
        return userDbStorage.getAllUsers();
    }

    public User saveUser(User user) {
        return userDbStorage.editUser(user);
    }

    public User findUserById(int id) {
        return userDbStorage.getUserByID(id);
    }

    public void addFriend(int userId, int friendId) {
        User user = userDbStorage.getUserByID(userId);
        User friend = userDbStorage.getUserByID(friendId);
        friendshipDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userDbStorage.getUserByID(userId);
        User friend = userDbStorage.getUserByID(friendId);
        friendshipDbStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(int id) {
        User user = userDbStorage.getUserByID(id);
        ArrayList<User> friendsList = new ArrayList<>();
        List<User> friends = friendshipDbStorage.getFriends(id);
        for (User friend : friends) {
            friendsList.add(friend);
        }
        return friendsList;
    }

    public List<User> getCommonFriends(int idUser, int idFriend) {
        ArrayList<User> commonFriendsList = new ArrayList<>();
        List<User> userFriends = friendshipDbStorage.getFriends(idUser);
        List<User> friendFriends = friendshipDbStorage.getFriends(idFriend);
        for (User friend : userFriends) {
            if (friendFriends.contains(friend)) {
                commonFriendsList.add(friend);
            }
        }
        return commonFriendsList;
    }

}