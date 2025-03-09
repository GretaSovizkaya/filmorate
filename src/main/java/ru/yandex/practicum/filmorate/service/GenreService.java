package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GenreService {
    GenreRepository genreRepository;

    public Collection<Genre> getGenreList() {
        return genreRepository.getGenres();
    }

    public Genre findById(long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }
}