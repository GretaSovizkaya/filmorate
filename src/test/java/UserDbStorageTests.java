import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes = FilmorateApplication.class)
@Import(DataBaseUserStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("DataBaseUserStorage")
class UserDbStorageTests {
    private final DataBaseUserStorage userDbStorage;

    @Test
    public void checkCreateNewUserAndGetById() {
        User user = new User(1, "Айгуль", "email@mail.ru", "login12", LocalDate.now());

        User savedUser = userDbStorage.addUser(user);

        User retrievedUser = userDbStorage.getUserById(savedUser.getId());

        assertThat(retrievedUser)
                .hasFieldOrPropertyWithValue("id", savedUser.getId())
                .hasFieldOrPropertyWithValue("name", "Айгуль")
                .hasFieldOrPropertyWithValue("email", "email@mail.ru")
                .hasFieldOrPropertyWithValue("login", "login12");

        // Clean up by deleting the user
        userDbStorage.deleteUser(savedUser.getId());
    }


    @Test
    public void checkGetAllUsers() {

        User user = new User(3, "Айгуль", "email@mail.ru", "login12", LocalDate.now());

        userDbStorage.addUser(user);

        List<User> users = userDbStorage.getAllUsers();

        Assertions.assertEquals(users.size(), 3);

        userDbStorage.deleteUser(user.getId());
    }

    @Test
    public void updateUserAndGetById() {

        User user = new User(1, "Айгуль", "email@mail.ru", "login12", LocalDate.now());
        userDbStorage.addUser(user);

        user.setName("Rita");
        userDbStorage.updateUser(user);

        User user1 = userDbStorage.getUserById(user.getId());

        assertThat(user1).hasFieldOrPropertyWithValue("name", "Rita");


        userDbStorage.deleteUser(user.getId());
    }

    @Test
    public void deleteUserAndGerAllUser() {

        User user = new User(1, "dima", "email@mail.ru", "login12", LocalDate.now());
        userDbStorage.addUser(user);

        List<User> users = userDbStorage.getAllUsers();

        userDbStorage.deleteUser(user.getId());

        List<User> users1 = userDbStorage.getAllUsers();

        Assertions.assertNotEquals(users1, users);
    }

    @Test
    public void compareUsers() {

        User user = new User(1, "Айгуль", "email@mail.ru", "login12", LocalDate.now());


        User user1 = new User(1, "Айгуль", "email@mail.ru", "login12", LocalDate.now());
        Assertions.assertEquals(user1, user);

    }
}

