package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DataBaseUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO users (user_name,email,login,birthday) " +
                "VALUES (?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement pr = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            pr.setString(1, user.getName());
            pr.setString(2, user.getEmail());
            pr.setString(3, user.getLogin());
            pr.setDate(4, Date.valueOf(user.getBirthday()));
            return pr;
        }, keyHolder);

        Number generatedKey = keyHolder.getKey();
        user.setId(generatedKey.intValue());
        return user;

    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET user_name = ?, email = ?, login = ?, birthday = ? WHERE user_id = ?";
        int execp = jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getLogin(),
                user.getBirthday(), user.getId());
        if (execp == 0) {
            throw new NotFoundException("Невозможно обновить пользователя с id =" + user.getId());
        }
        return user;
    }

    @Override
    public void deleteUser(int id) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, new UserMapper());
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, new Object[]{id}, new UserMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
    }

    @Override
    public void addFriend(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        final String sqlQuery = "INSERT INTO friends (user_id, friend_id,status) VALUES (?,?,?);";
        jdbcTemplate.update(sqlQuery, friendId, userId, "unconfirmed");
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        getUserById(userId);
        String sqlQuery = "SELECT * FROM users " +
                "INNER JOIN friends ON users.user_id = friends.user_id WHERE friend_id = ?";
        return jdbcTemplate.query(sqlQuery, new UserMapper(), userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        final String sqlQuery = "select distinct u.* from users u " +
                "inner join friends f1 on u.user_id = f1.friend_id " +
                "inner join friends f2 on u.user_id = f2.friend_id " +
                "where f1.user_id = ? and f2.user_id = ?";
        return jdbcTemplate.query(sqlQuery, new UserMapper(), friendId, userId);
    }

}