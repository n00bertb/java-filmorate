package ru.yandex.practicum.filmorate.dal.repositories;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Optional<Film> getById(Long id);

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    void putLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);
    void deleteFilm(Long id);

    List<Film> getBestFilm(int count);

    List<Long> getAllId();
}