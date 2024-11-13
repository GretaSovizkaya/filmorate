package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DataBaseFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {

        final String sqlQuery = "INSERT INTO films (film_name, description, release_date, duration,genre) VALUES (?, ?, ?, ?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setString(5, film.getGenre());
            return ps;
        }, keyHolder);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        final String sqlQuery = "UPDATE films SET " +
                "film_name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ? " +
                "WHERE film_id = ?;";
        int temp = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        if (temp == 0) {
            throw new NotFoundException("Невозможно обновить фильм с id = " + film.getId());
        }
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", id);
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, new FilmMapper());
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, new FilmMapper(), id);
    }

    @Override
    public void addLike(int filmId, int userId) {
        final String sqlQuery = "insert into likes (film_id,user_id) values (?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement pr = con.prepareStatement(sqlQuery);
            pr.setInt(1, filmId);
            pr.setInt(2, userId);
            return pr;
        });
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }
    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery = "SELECT * " +
                "FROM films " +
                "inner join rating_mpa on films.rating_id = rating_mpa.rating_id " +
                "WHERE film_id IN ( " +
                "    SELECT  likes.film_id " +
                "    FROM likes " +
                "    GROUP BY likes.film_id " +
                "    ORDER BY COUNT(likes.user_id) DESC " +
                "LIMIT ?" +
                ");";
        return jdbcTemplate.query(sqlQuery, new FilmMapper(),count);
    }
}