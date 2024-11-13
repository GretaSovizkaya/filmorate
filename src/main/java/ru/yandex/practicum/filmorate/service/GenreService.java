package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> getGenreList() {
        return genreStorage.getGenres();
    }

    public Optional<Genre> findById(long id) {
        if (id > 6) {
            throw new NotFoundException("Нет жанра с таким айди");
        }
        return genreStorage.findById(id);
    }
}
