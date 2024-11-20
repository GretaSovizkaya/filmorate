import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {FilmorateApplication.class})
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        User user = new User(1L, "TestUser", "test@example.com", "login", null);
        userRepository.addUser(user);
    }

    @Test
    void getUser_ShouldThrowException_WhenUserNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUser(999L));
        assertEquals("Пользователь не найден", exception.getMessage());
    }

}
