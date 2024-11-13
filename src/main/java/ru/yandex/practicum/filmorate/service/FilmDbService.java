package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmDbService {

    private final FilmStorage filmStorage;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public Film addFilm(Film film) {
        checkValidation(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        checkValidation(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }

    public void addLike(int id, int userId) {
        filmStorage.addLike(id, userId);
    }

    public void removeLike(int id, int userId) {
        filmStorage.removeLike(id, userId);
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.getPopularFilms(count);
    }

    private void checkValidation(Film film) {
        if (film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if ((film.getReleaseDate().isBefore(LocalDate.parse("28.12.1895", formatter)))) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration() < 0) {
            log.error("Продолжительность не может быть отрицательной");
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }
    }

}