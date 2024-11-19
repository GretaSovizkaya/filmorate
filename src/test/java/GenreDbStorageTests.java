import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DataBaseGenreStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes = FilmorateApplication.class)
@Import(DataBaseGenreStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTests {
    private final DataBaseGenreStorage dataBaseGenreStorage;

    @Test
    public void shouldGetAllGenre() {
        List<Genre> listGenres = dataBaseGenreStorage.getGenres().stream().toList();


        assertThat(listGenres.size())
                .isEqualTo(6);
    }
}