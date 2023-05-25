package ru.yandex.practicum.filmorate.DAO;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipDbStorage {
    void addFriend(int userId, int friendId);

    List<User> getFriends(int id);

    void deleteFriend(int userId, int friendId);
}
