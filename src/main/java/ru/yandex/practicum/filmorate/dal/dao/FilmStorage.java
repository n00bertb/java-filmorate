package ru.yandex.practicum.filmorate.dal.dao;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    List<Film> findAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void putLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);
    void deleteFilm(Long id);

    Film findFilmById(Long id);

    List<Film> getBestFilm(int count);
}
