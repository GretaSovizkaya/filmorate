package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.LinkedHashSet;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Integer likes = 0;
    LinkedHashSet<Genre> genres;
    Rating mpa;
}