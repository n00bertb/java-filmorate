package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAllFilms();

    Film findFilmById(Long id);

    void deleteFilm(Long id);

    Film updateFilm(Film film);

    Film createFilm(Film film);

}