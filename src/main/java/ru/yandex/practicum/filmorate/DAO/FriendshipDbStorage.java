package ru.yandex.practicum.filmorate.DAO;

import java.util.List;

public interface FriendshipDbStorage {
    void addFriend(int userId, int friendId);

    List<Integer> getFriends(int id);

    void deleteFriend(int userId, int friendId);
}
