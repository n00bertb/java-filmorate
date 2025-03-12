package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final LocalDate filmBirthday = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap();
    private int current = 0;

    @GetMapping
    public Collection<Film> getAllUsers() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(filmBirthday)) {
            log.error("Ошибка создания сущности {}", film);
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        } else {
            film.setId(++current);
            films.put(film.getId(), film);
            log.info("Сущность успешно создана: id {}", film.getId());
            log.debug("film: {}", film);
            return film;
        }
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            if (newFilm.getReleaseDate().isBefore(filmBirthday)) {
                log.error("Ошибка обновления сущности:{}", newFilm);
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
            } else {
                Film oldFilm = films.get(newFilm.getId());
                oldFilm.setName(newFilm.getName());
                oldFilm.setDescription(newFilm.getDescription());
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
                oldFilm.setDuration(newFilm.getDuration());
                films.put(oldFilm.getId(), oldFilm);
                log.info("Сущность успешно обновлена: id={}", oldFilm.getId());
                log.debug("film: {}", oldFilm);
                return oldFilm;
            }
        } else {
            throw new ValidationException("Такого фильма нет в списке!");
        }
    }
}