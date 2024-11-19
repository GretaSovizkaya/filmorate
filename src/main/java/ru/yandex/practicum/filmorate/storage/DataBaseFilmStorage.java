package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
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

        final String sqlQuery = "INSERT INTO films (film_name, description, release_date, duration,rating_id) VALUES (?, ?, ?, ?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getRating().getId());
            return ps;
        }, keyHolder);

        Number number = keyHolder.getKey();
        film.setId(number.intValue());
        if (film.getGenre() != null) {
            for (Genre genre : film.getGenre()) {
                boolean exists = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM genre WHERE genre_id = ?", Integer.class, genre.getId()) > 0;

                if (!exists) {
                    String insertGenreSql = "INSERT INTO genre (genre_id, name_genre) VALUES (?, ?)";
                    jdbcTemplate.update(insertGenreSql, genre.getId(), genre.getName());
                }
            }

            String sqlInsertGenre = "INSERT INTO genres_film (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : film.getGenre()) {
                jdbcTemplate.update(sqlInsertGenre, film.getId(), genre.getId());
            }
        }

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
        String sqlQuery = "SELECT * FROM films " +
                "left join rating_mpa on films.rating_id = rating.rating_id " +
                "LEFT JOIN genres_film ON films.film_id = genres_film.film_id " +
                "LEFT JOIN genre ON genres_film.genre_id = genre.genre_id " +
                "LEFT JOIN likes ON likes.film_id = films.film_id;";
        return jdbcTemplate.query(sqlQuery, new FilmMapper());
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sql, new FilmMapper(), id);
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
        final String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery = "SELECT * " +
                "FROM films " +
                "inner join rating_mpa on films.rating_id = rating.rating_id " +
                "WHERE film_id IN ( " +
                "    SELECT likes.film_id " +
                "    FROM likes " +
                "    GROUP BY likes.film_id " +
                "    ORDER BY COUNT(likes.user_id) DESC " +
                "limit ?" +
                ");";
        return jdbcTemplate.query(sqlQuery, new FilmMapper(), count);
    }


}