package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmDbController {
    private final FilmDbService filmService;

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10") int limit) {
        return filmService.getPopularFilm(limit);
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Добавление нового фильма: {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") int id) {
        log.info("Запрос на получение фильма по id: {}", id);
        return filmService.getFilmById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") int id) {
        log.info("Удаление фильма с id: {}", id);
        filmService.deleteFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("Пользователь с id {} удалил лайк у фильма с id {}", userId, filmId);
        filmService.removeLike(filmId, userId);
    }
    
}