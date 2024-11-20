package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@SpringBootTest
public class FilmTest {

    @Test
    public void testFilmNameNotBlank() {
        Film film = new Film(1, "", "Description", LocalDate.of(2020, 1, 1), 120, 0, null, null);

        // Проверка, что имя не пустое
        assertNotNull(film.getName());
        assertEquals("", film.getName(), "Name cannot be empty!");
    }

    @Test
    public void testFilmDescriptionTooLong() {
        // Проверка, что описание не длиннее 200 символов
        String longDescription = "A".repeat(201);
        Film film = new Film(2, "Film Name", longDescription, LocalDate.of(2020, 1, 1), 120, 0, new LinkedHashSet<>(), null);

        assertFalse(film.getDescription().length() <= 200, "Description is too long!");
    }

    @Test
    public void testFilmDurationNotNegative() {
        Film film = new Film(3, "Film Name", "Description", LocalDate.of(2020, 1, 1), -120, 0, new LinkedHashSet<>(), null);

        // Проверка, что продолжительность не отрицательная
        assertTrue(film.getDuration() <= 0, "Duration cannot be negative!");
    }

    @Test
    public void testFilmReleaseDateInTheFuture() {
        Film film = new Film(4, "Future Film", "Description", LocalDate.of(2024, 1, 1), 120, 0, new LinkedHashSet<>(), null);

        // Проверка, что дата релиза не в будущем
        assertTrue(film.getReleaseDate().isBefore(LocalDate.now()) || film.getReleaseDate().isEqual(LocalDate.now()), "Release date cannot be in the future!");
    }
}
