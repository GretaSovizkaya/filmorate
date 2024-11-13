package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingStorage ratingStorage;

    public List<Rating> getRatingList() {
        return ratingStorage.getRatingList();
    }

    public Rating findById(int id) {
        return ratingStorage.findById(id);
    }
}