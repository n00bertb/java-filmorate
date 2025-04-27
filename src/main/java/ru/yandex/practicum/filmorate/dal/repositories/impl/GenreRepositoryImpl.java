package ru.yandex.practicum.filmorate.dal.repositories.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.repositories.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreRepositoryImpl implements GenreRepository {

    NamedParameterJdbcOperations jdbc;
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT * FROM genres;", genreRowMapper());
    }

    @Override
    public Optional<Genre> findById(final int id) {
        String sql = "SELECT * FROM genres WHERE genre_id = :genre_id;";
        return Optional.ofNullable(jdbc.query(sql, Map.of("genre_id", id), rs -> {
            if (!rs.next()) {
                return null;
            }
            return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
        }));
    }

    @Override
    public List<Genre> getListGenres(List<Integer> ids) {
        String sql = "SELECT * FROM genres WHERE genre_id IN (:genre_id);";
        return jdbc.query(sql, Map.of("genre_id", ids), genreRowMapper());
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
    }
}