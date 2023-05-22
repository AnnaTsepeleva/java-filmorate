package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.DAO.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    private final UserDbStorage userDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage, FriendshipDbStorage friendshipDbStorage) {
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
        return userDbStorage.findUserById(id);
    }

    public void addFriend(int userId, int friendId) {
        User user = userDbStorage.findUserById(userId);
        User friend = userDbStorage.findUserById(friendId);
        friendshipDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userDbStorage.findUserById(userId);
        User friend = userDbStorage.findUserById(friendId);
        friendshipDbStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(int id) {
        User user = userDbStorage.findUserById(id);
        ArrayList<User> friendsList = new ArrayList<>();
        List<Integer> friends = friendshipDbStorage.getFriends(id);
        for (int friend : friends) {
            friendsList.add(userDbStorage.findUserById(friend));
        }
        return friendsList;
    }

    public List<User> getCommonFriends(int idUser, int idFriend) {
        ArrayList<User> commonFriendsList = new ArrayList<>();
        List<Integer> userFriends = friendshipDbStorage.getFriends(idUser);
        List<Integer> friendFriends = friendshipDbStorage.getFriends(idFriend);
        for (int friend : userFriends) {
            if (friendFriends.contains(friend)) {
                commonFriendsList.add(userDbStorage.findUserById(friend));
            }
        }
        return commonFriendsList;
    }

}
