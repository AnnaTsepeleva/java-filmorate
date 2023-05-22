package ru.yandex.practicum.filmorate.DAO.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAO.FriendshipDbStorage;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class FriendShipDbStorageImpl implements FriendshipDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendShipDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "insert into friendship(user_id, friend_id, status, created_from) " + "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, true, LocalDateTime.now());
    }

    @Override
    public List<Integer> getFriends(int id) {
        String sqlQuery = "select friend_id from friendship " + "where user_id = ? and status = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, id, true);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "delete from friendship " + "where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
