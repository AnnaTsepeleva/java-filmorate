package ru.yandex.practicum.filmorate.DAO;

import java.util.List;

public interface FriendshipDbStorage {
    public void addFriend(int userId, int friendId);

    public List<Integer> getFriends(int id);

    public void deleteFriend(int userId, int friendId);
}
