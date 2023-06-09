package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.NotFoundException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDbStorageImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
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
    public User getUserByID(int id) {
        String sqlQuery = "select * from users where id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь не найден");
    }

    @Override
    public User createUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "insert into users(email, login, name, birthday) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId((int) keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public void deleteUser(User user) {
        String sqlQuery3 = "delete from users where id =?";
        jdbcTemplate.update(sqlQuery3, user.getId());
    }

    @Override
    public User editUser(User user) {
        getUserByID(user.getId());
        String sqlQuery = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        String sqlQuery = "select id, email, login, name, birthday from users";
        return (ArrayList<User>) jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

}
