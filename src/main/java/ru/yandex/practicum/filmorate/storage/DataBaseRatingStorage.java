package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mappers.RatingMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataBaseRatingStorage implements RatingStorage {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Rating findById(Integer id) {
        final String sqlQuery = "SELECT RATING_ID, RATING_NAME FROM RATING_MPA WHERE RATING_ID = :ratingId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ratingId", id);

        try {
            return (Rating) jdbc.query(sqlQuery, new RatingMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Рейтинg с id=" + id + " не найден");
        }
    }

    @Override
    public List<Rating> getRatingList() {
        final String SELECT_QUERY = "SELECT * FROM RATING_MPA";
        return jdbc.query(SELECT_QUERY, new RatingMapper());
    }

    @Override
    public Rating create(Rating rating) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String INSERT_RATING_QUERY = "INSERT INTO RATING_MPA (RATING_NAME) VALUES (:ratingName)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ratingName", rating.getName());

        jdbc.update(INSERT_RATING_QUERY, parameters, keyHolder);
        rating.setId(keyHolder.getKeyAs(Integer.class));
        return rating;
    }

    /*@Override
    public Rating update(Rating rating) {
        final String UPDATE_RATING_QUERY = "UPDATE RATING_MPA SET RATING_NAME = :ratingName WHERE RATING_ID = :ratingId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ratingId", rating.getId());
        parameters.addValue("ratingName", rating.getName());

        jdbc.update(UPDATE_RATING_QUERY, parameters);
        return rating;
    }

    @Override
    public void delete(Rating rating) {
        final String DELETE_RATING_QUERY = "DELETE FROM RATING_MPA WHERE RATING_ID = :ratingId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ratingId", rating.getId());

        jdbc.update(DELETE_RATING_QUERY, parameters);
    }*/
}