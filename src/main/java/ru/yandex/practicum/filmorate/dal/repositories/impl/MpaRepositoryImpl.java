package ru.yandex.practicum.filmorate.dal.repositories.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.repositories.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MpaRepositoryImpl implements MpaRepository {
    NamedParameterJdbcOperations jdbc;

    @Override
    public List<Mpa> findAll() {
        String sql = "SELECT * FROM mpa;";
        return jdbc.query(sql, (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"),
                rs.getString("mpa_name")));
    }

    @Override
    public Optional<Mpa> findById(final int id) {
        String sql = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = :mpa_id";
        Map<String, Object> param = Map.of("mpa_id", id);
        return Optional.ofNullable(jdbc.query(sql, param, rs -> {
            if (!rs.next()) {
                return null;
            }
            return new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"));
        }));
    }
}