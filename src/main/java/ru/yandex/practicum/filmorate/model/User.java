package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;

    private String name;

    private String email;

    private String login;

    @NonNull
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;


}