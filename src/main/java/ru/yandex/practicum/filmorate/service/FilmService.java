package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmValidator filmValidator;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, FilmValidator filmValidator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmValidator = filmValidator;
    }

    public Film createFilm(Film film) {
        filmValidator.validReleaseDate(film);
        filmStorage.createFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmValidator.validFilmsIdNotNull(film);
        if (filmStorage.findFilmById(film.getId()) == null) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден.");
        }
        filmValidator.validReleaseDate(film);
        Film updatedFilm = filmStorage.findFilmById(film.getId()).toBuilder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();
        filmStorage.updateFilm(updatedFilm);
        return updatedFilm;
    }

    public void deleteFilm(Long id) {
        if (filmStorage.findFilmById(id) == null) {
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }
        filmStorage.deleteFilm(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film getFilmById(Long id) {
        Film requiredFilm = filmStorage.findFilmById(id);
        if (requiredFilm == null) {
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }
        return requiredFilm;
    }

    public void likeFilm(Long filmId, Long userId) {
        Film requiredFilm = filmStorage.findFilmById(filmId);
        if (requiredFilm == null || userStorage.findUserById(userId) == null) {
            throw new NotFoundException("Ошибка, проверьте правильность ввода filmId и userId.");
        }
        requiredFilm.getUsersLike().add(userId);
    }

    public void dislikeFilm(Long filmId, Long userId) {
        Film requiredFilm = filmStorage.findFilmById(filmId);
        if (requiredFilm == null || userStorage.findUserById(userId) == null) {
            throw new NotFoundException("Ошибка, проверьте правильность ввода filmId и userId.");
        }
        requiredFilm.getUsersLike().remove(userId);
    }

    public List<Film> getTopFilms(Integer count) {
        return filmStorage.findAllFilms()
                .stream()
                .sorted(Comparator.comparing(Film::getUsersLike, (s1, s2) -> Integer.compare(s2.size(), s1.size())))
                .limit(count)
                .collect(Collectors.toList());
    }
}