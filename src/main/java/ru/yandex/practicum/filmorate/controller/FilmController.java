package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dal.dao.FilmStorage;

import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;

    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.findAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") final Long id) {
        return filmStorage.findFilmById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable("id") final Long id) {
        filmStorage.deleteFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") final Long filmId,
                         @PathVariable("userId") final Long userId) {
        filmStorage.putLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislikeFilm(@PathVariable("id") final Long filmId,
                            @PathVariable("userId") final Long userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(name = "count", defaultValue = "10") final Integer count) {
        return filmStorage.getBestFilm(count);
    }
}