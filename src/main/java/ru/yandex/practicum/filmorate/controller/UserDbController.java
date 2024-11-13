package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserDbController {
    private final UserDbService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Добавление нового пользователя: {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id) {
        log.info("Запрос на получение пользователя по id: {}", id);
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int id) {
        log.info("Удаление пользователя с id: {}", id);
        userService.deleteUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.info("Пользователь с id {} добавил друга с id {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int userId, @PathVariable("id") int friendId) {
        log.info("Пользователь с id {} удалил друга с id {}", userId, friendId);
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int userId) {
        log.info("Запрос на получение друзей пользователя с id: {}", userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriend(@PathVariable("id") int userId,
                                      @PathVariable("otherId") int friendId) {
        return userService.getCommonFriend(userId, friendId);
    }

}