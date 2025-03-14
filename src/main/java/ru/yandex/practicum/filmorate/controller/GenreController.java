package ru.yandex.practicum.filmorate.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GenreController {
    GenreService genreService;

    @GetMapping
    public Collection<Genre> getGenreList() {
        return genreService.getGenreList();
    }

    @GetMapping("{id}")
    public Genre findUser(@PathVariable long id) {
        return genreService.findById(id);
    }
}