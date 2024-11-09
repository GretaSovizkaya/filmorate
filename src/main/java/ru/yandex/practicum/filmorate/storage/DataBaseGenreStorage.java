package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenresMapper;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataBaseGenreStorage implements GenreStorage {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Collection<Genre> getGenres() {
        final String SELECT_QUERY = "SELECT * FROM GENRE";
        return jdbc.query(SELECT_QUERY, new GenresMapper());
    }

    @Override
    public Optional<Genre> findById(Long id) {
        final String SELECT_QUERY = "SELECT GENRE_ID, NAME_GENRE FROM GENRE WHERE GENRE_ID = :genreId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("genreId", id);

        try {
            return Optional.ofNullable(jdbc.queryForObject(SELECT_QUERY, parameters, new GenresMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Genre create(Genre genre) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String INSERT_GENRE_QUERY = "INSERT INTO GENRE (NAME_GENRE) VALUES (:genreName)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("genreName", genre.getName());

        jdbc.update(INSERT_GENRE_QUERY, parameters, keyHolder);
        genre.setId(keyHolder.getKeyAs(Long.class));
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        final String UPDATE_GENRE_QUERY = "UPDATE GENRE SET NAME_GENRE = :genreName WHERE GENRE_ID = :genreId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("genreId", genre.getId());
        parameters.addValue("genreName", genre.getName());
        jdbc.update(UPDATE_GENRE_QUERY, parameters);

        return genre;
    }

    @Override
    public void delete(Genre genre) {
        final String DELETE_GENRE_QUERY = "DELETE FROM GENRE WHERE GENRE_ID = :genreId";
        final String DELETE_FILM_GENRES_QUERY = "DELETE FROM GENRES_FILM WHERE GENRE_ID = :genreId";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("genreId", genre.getId());

        jdbc.update(DELETE_GENRE_QUERY, parameters);
        jdbc.update(DELETE_FILM_GENRES_QUERY, parameters);
    }

}