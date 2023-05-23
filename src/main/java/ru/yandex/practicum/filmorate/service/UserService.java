package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.DAO.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Autowired
    private final UserStorage userStorage;


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
        return userStorage.getUserByID(id);
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserByID(userId);
        User friend = userStorage.getUserByID(friendId);
        Set<Integer> userFrList = user.getFriends();
        userFrList.add(friendId);
        user.setFriends(userFrList);
        Set<Integer> friendFrList = friend.getFriends();
        friendFrList.add(userId);
        friend.setFriends(friendFrList);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserByID(userId);
        User friend = userStorage.getUserByID(friendId);
        Set<Integer> userFrList = user.getFriends();
        userFrList.remove(friendId);
        user.setFriends(userFrList);
        Set<Integer> friendFrList = friend.getFriends();
        friendFrList.remove(userId);
        friend.setFriends(friendFrList);
    }

    public List<User> getFriends(int id) {
        ArrayList<User> friendsList = new ArrayList<>();
        Set<Integer> friends = userStorage.getUserByID(id).getFriends();
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
