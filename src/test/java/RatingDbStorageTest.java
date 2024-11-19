import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.DataBaseRatingStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes = FilmorateApplication.class)
@Import(DataBaseRatingStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDbStorageTest {
    private final RatingStorage ratingStorage;

    @Test
    public void shouldGetAllMPA() {
        List<Rating> listMPA = ratingStorage.getRatingList().stream().toList();

        assertThat(listMPA.size())
                .isEqualTo(5);
    }
}