package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {

    private int id;

    private String name;

    private String description;

    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;

    private int duration;

    private Set<Genre> genre;

    private Rating rating;

    Set<Long> likes;
}