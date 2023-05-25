package ru.yandex.practicum.filmorate.DAO.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAO.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class FriendShipDbStorageImpl implements FriendshipDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendShipDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder().id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate()).build();
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "insert into friendship(user_id, friend_id, status, created_from) values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, true, LocalDateTime.now());
    }

    @Override
    public List<User> getFriends(int id) {
        String sqlQuery = "select u.id, u.email, u.login, u.name, u.birthday from friendship as fs left join users as u on fs.friend_id = u.id "
                + "where user_id = ? and status = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, true);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "delete from friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
