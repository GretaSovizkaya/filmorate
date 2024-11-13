import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DataBaseFilmStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.FilmorateApplication;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes = FilmorateApplication.class)
@Import(DataBaseFilmStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTests {
    private final FilmStorage filmStorage;

    @Test
    public void testAddFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A sci-fi thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148L);
        film.setGenre("Sci-Fi");

        Film createdFilm = filmStorage.addFilm(film);

        assertThat(createdFilm).isNotNull();
    }

    @Test
    public void testDeleteFilm() {
        Film film = new Film();
        film.setName("Dunkirk");
        film.setDescription("A WWII thriller");
        film.setReleaseDate(LocalDate.of(2017, 7, 21));
        film.setDuration(148L);
        film.setGenre("War");

        Film createdFilm = filmStorage.addFilm(film);
        filmStorage.deleteFilm(createdFilm.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> {
            filmStorage.getFilmById(createdFilm.getId());
        });
    }

    @Test
    public void testGetAllFilms() {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setDescription("Description 1");
        film1.setReleaseDate(LocalDate.of(2020, 1, 1));
        film1.setDuration(148L);
        film1.setGenre("Drama");

        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setDescription("Description 2");
        film2.setReleaseDate(LocalDate.of(2021, 1, 1));
        film2.setDuration(148L);
        film2.setGenre("Comedy");

        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);

        List<Film> films = filmStorage.getAllFilms();
        assertThat(films).hasSizeGreaterThanOrEqualTo(2);
    }
}
