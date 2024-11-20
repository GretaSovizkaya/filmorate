package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.RatingRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmService {

    ValidateService validateService;
    FilmRepository filmRepository;
    RatingRepository ratingRepository;
    GenreRepository genreRepository;
    UserRepository userRepository;

    public Film addFilm(Film film) {
        validateService.validateFilm(film);
        validateGenresAndRating(film);
        return filmRepository.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validateService.validateFilm(film);
        filmRepository.getFilm(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        return filmRepository.updateFilm(film);
    }

    public Film getFilm(long id) {
        return filmRepository.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public Collection<Film> getAllFilms() {
        return filmRepository.getFilms();
    }

    public Collection<Film> getTopFilms(int count) {
        return filmRepository.findPopular(count);
    }

    public void addLike(long filmId, long userId) {
        userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        filmRepository.setLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        filmRepository.deleteLike(filmId, userId);
    }

    private void validateGenresAndRating(Film film) {
        if (film.getGenres() != null) {
            List<Long> genreIds = film.getGenres().stream().map(Genre::getId).toList();
            Collection<Genre> genres = genreRepository.findByIds(genreIds);
            if (genres.size() != genreIds.size()) {
                throw new ValidationException("Некорректные жанры");
            }
        }
        ratingRepository.findById(film.getMpa().getId())
                .orElseThrow(() -> new ValidationException("Рейтинг MPA не найден"));
    }
}