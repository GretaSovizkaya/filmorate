import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DataBaseFilmStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.FilmorateApplication;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

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
        film.setDuration(Duration.ofMinutes(148));
        film.setGenre("Sci-Fi");

        Film createdFilm = filmStorage.addFilm(film);

        assertThat(createdFilm).isNotNull();
        assertThat(createdFilm.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetFilmById() {
        Film film = new Film();
        film.setName("Interstellar");
        film.setDescription("A space exploration thriller");
        film.setReleaseDate(LocalDate.of(2014, 11, 7));
        film.setDuration(Duration.ofMinutes(169));
        film.setGenre("Sci-Fi");

        Film createdFilm = filmStorage.addFilm(film);
        Optional<Film> foundFilm = Optional.ofNullable(filmStorage.getFilmById(createdFilm.getId()));

        assertThat(foundFilm).isPresent().hasValueSatisfying(f ->
                assertThat(f).hasFieldOrPropertyWithValue("id", createdFilm.getId())
        );
    }

    @Test
    public void testDeleteFilm() {
        Film film = new Film();
        film.setName("Dunkirk");
        film.setDescription("A WWII thriller");
        film.setReleaseDate(LocalDate.of(2017, 7, 21));
        film.setDuration(Duration.ofMinutes(106));
        film.setGenre("War");

        Film createdFilm = filmStorage.addFilm(film);
        filmStorage.deleteFilm(createdFilm.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> {
            filmStorage.getFilmById(createdFilm.getId());
        });
    }
    @Test
    public void testAddFilmAndGet () {

        Set<Long> likkes = new HashSet<>();
        likkes.add(1L);
        likkes.add(2L);

        Genre genre = new Genre();
        Genre genre1 = new Genre();
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        genres.add(genre1);

        Film film = new Film();

        filmStorage.addFilm(film);

        Film film1 = filmStorage.getFilmById(film.getId());

        assertThat(film1).hasFieldOrPropertyWithValue("id",1L);
    }
}
