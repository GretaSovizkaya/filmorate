package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {

    int id;

    String name;

    @Size(max = 200, message = "Description is too long!")
    String description;

    @PastOrPresent(message = "Release date cannot be in the future")
    LocalDate releaseDate;

    Long duration;

    String genre;

    Set<Integer> likes;
    Rating rating;
}