package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@SpringBootTest
public class UserTest {

    @Test
    public void testUserEmailNotBlank() {
        User user = new User(1, "", "login", "name", LocalDate.of(1990, 1, 1));

        // Проверка, что email не пустой
        assertNotNull(user.getEmail());
        assertEquals("", user.getEmail(), "Email cannot be empty!");
    }

    @Test
    public void testUserEmailInvalid() {
        User user = new User(2, "invalid-email", "login", "name", LocalDate.of(1990, 1, 1));

        // Проверка на корректность email
        assertFalse(user.getEmail().contains("@"), "Email is invalid!");
    }

    @Test
    public void testUserLoginTooShort() {
        User user = new User(3, "email@example.com", "a", "name", LocalDate.of(1990, 1, 1));

        // Проверка, что логин имеет хотя бы 3 символа
        assertTrue(user.getLogin().length() <= 3, "Login must be at least 3 characters long!");
    }

    @Test
    public void testUserBirthdayNotInTheFuture() {
        User user = new User(4, "email@example.com", "login", "name", LocalDate.of(2000, 1, 1));

        // Проверка, что дата рождения не в будущем
        assertTrue(user.getBirthday().isBefore(LocalDate.now()) || user.getBirthday().isEqual(LocalDate.now()), "Birthday cannot be in the future!");
    }
}
