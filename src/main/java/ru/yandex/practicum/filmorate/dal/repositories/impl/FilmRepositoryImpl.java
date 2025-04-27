package ru.yandex.practicum.filmorate.dal.repositories.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.mappers.FilmCollectionMapper;
import ru.yandex.practicum.filmorate.dal.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.dal.repositories.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmRepositoryImpl implements FilmRepository {


    NamedParameterJdbcOperations jdbc;
    JdbcTemplate jdbcTemplate;
    FilmMapper filmMapper;
    FilmCollectionMapper filmCollectionMapper;

    @Override
    public Optional<Film> getById(final Long id) {
        String sql = "SELECT * " +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id = :film_id; ";
        Film film = jdbc.query(sql, Map.of("film_id", id), filmMapper);
        return Optional.ofNullable(film);
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * " +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id; ";
        Map<Integer, Film> films = jdbc.query(sql, Map.of(), filmCollectionMapper);
        Objects.requireNonNull(films, "The films map should not be null");
        return films.values().stream().toList();
    }

    @Override
    public Film create(final Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO FILMS (name, description, release_date, duration, mpa_id) " +
                "VALUES (:name, :description, :release_date, :duration, :mpa_id); ";
        Map<String, Object> params = new HashMap<>();
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpa_id", film.getMpa().getId());

        jdbc.update(sql, new MapSqlParameterSource().addValues(params), keyHolder, new String[]{"film_id"});
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        addGenres(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public Film update(final Film film) {
        String sql = "UPDATE films SET name = :name, " +
                "description = :description, " +
                "release_date = :release_date, " +
                "duration = :duration, " +
                "mpa_id = :mpa_id " +
                "WHERE film_id = :film_id; ";
        Map<String, Object> params = new HashMap<>();
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpa_id", film.getMpa().getId());
        params.put("film_id", film.getId());
        jdbc.update(sql, params);
        addGenres(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public void putLike(final Long id, final Long userId) {
        String sql = "MERGE INTO likes(film_id, user_id) VALUES (:film_id, :user_id); ";
        jdbc.update(sql, Map.of("film_id", id, "user_id", userId));
    }

    @Override
    public void deleteLike(final Long id, final Long userId) {
        String sql = "DELETE FROM likes WHERE film_id = :film_id AND user_id = :user_id; ";
        jdbc.update(sql, Map.of("film_id", id, "user_id", userId));
    }

    @Override
    public void deleteFilm(final Long id) {
        String sql = "DELETE FROM film " +
                "WHERE film_id = :film_id; ";
        jdbc.update(sql, Map.of("film_id", id));
    }

    @Override
    public List<Film> getBestFilm(final int count) {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
                "f.mpa_id, m.mpa_name, " +
                "fg.genre_id, g.genre_name, " +
                "COUNT(DISTINCT l.user_id) AS like_count " +
                "FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                " inner JOIN likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id, fg.genre_id " +
                "ORDER BY like_count DESC " +
                "LIMIT :count;";

        Map<Integer, Film> films = jdbc.query(sql, Map.of("count", count), filmCollectionMapper);
        Objects.requireNonNull(films, "The films map should not be null");
        return films.values().stream().toList();
    }

    @Override
    public List<Integer> getAllId() {
        return jdbcTemplate.query("SELECT film_id FROM films; ", (rs, rowNum) -> rs.getInt("film_id"));
    }

    private void addGenres(final Long filmId, final Set<Genre> genres) {
        Map<String, Object>[] batch = new HashMap[genres.size()];
        int count = 0;
        for (Genre genre : genres) {
            Map<String, Object> map = new HashMap<>();
            map.put("film_id", filmId);
            map.put("genre_id", genre.getId());
            batch[count++] = map;
        }
        String sqlDelete = "DELETE FROM film_genres WHERE film_id = :film_id AND genre_id = :genre_id; ";
        String sqlInsert = "INSERT INTO film_genres (film_id, genre_id) VALUES (:film_id, :genre_id); ";
        jdbc.batchUpdate(sqlDelete, batch);
        jdbc.batchUpdate(sqlInsert, batch);
    }
}