import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DataBaseUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes = FilmorateApplication.class)
@Import(DataBaseUserStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("DataBaseUserStorage")
class UserDbStorageTests {
    private final UserStorage userStorage;

    @Test
    void testAddUser() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setLogin("alice123");
        user.setBirthday(LocalDate.of(1995, 5, 15));

        User savedUser = userStorage.addUser(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Alice");
    }

    @Test
    void testFindUserById() {
        User user = new User();
        user.setName("Bob");
        user.setEmail("bob@example.com");
        user.setLogin("bob321");
        user.setBirthday(LocalDate.of(1990, 3, 10));

        User savedUser = userStorage.addUser(user);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(savedUser.getId()));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "Bob")
                );
    }
}

