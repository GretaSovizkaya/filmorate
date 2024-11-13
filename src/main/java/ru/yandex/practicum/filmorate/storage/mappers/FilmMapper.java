package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedHashSet;

@Component
public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("film_name"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getLong("duration"));
        film.setGenre(rs.getString("genre"));
        film.setRating(new Rating());
        film.setLikes(new LinkedHashSet<>());
        return film;
    }
}