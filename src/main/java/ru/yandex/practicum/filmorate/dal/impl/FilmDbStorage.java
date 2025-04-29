package ru.yandex.practicum.filmorate.dal.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dal.repositories.FilmRepository;
import ru.yandex.practicum.filmorate.dal.repositories.GenreRepository;
import ru.yandex.practicum.filmorate.dal.repositories.MpaRepository;
import ru.yandex.practicum.filmorate.dal.repositories.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmDbStorage implements FilmStorage {

    FilmRepository filmRepository;
    UserRepository userRepository;
    GenreRepository genreRepository;
    MpaRepository mpaRepository;
    FilmValidator filmValidator;

    public Film findFilmById(final Long id) {
        log.info("Запрос на получение фильма с id = {}", id);
        validId(id);
        return filmRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильма с id = " + id + " не существует"));
    }

    public List<Film> findAllFilms() {
        log.info("Запрос на получение списка фильмов");
        List<Film> films = filmRepository.findAll();
        if (films.isEmpty()) {
            log.info("В приложение еще не добавлен ни один фильм");
            return new ArrayList<>();
        }
        return films;
    }

    public Film createFilm(final Film film) {
        log.info("Запрос на добавление нового фильма");
        filmValidator.validReleaseDate(film);
        Film filmGenre = validAndAddMpaGenres(film);
        log.info("Запрос на добавление нового фильма в репозиторий");
        Film newFilm = filmRepository.create(filmGenre);
        log.info("Фильм успешно добавлен под id {}", newFilm.getId());
        return newFilm;
    }

    public Film updateFilm(final Film film) {
        log.info("Запрос на обновление фильма");
        validId(film.getId());
        Film filmGenre = validAndAddMpaGenres(film);
        Film newFilm = filmRepository.update(filmGenre);
        log.info("Фильм с id {} успешно обновлен", film.getId());
        return newFilm;
    }

    public void putLike(final Long id, final Long userId) {
        validId(id);
        validIdUser(userId);
        filmRepository.putLike(id, userId);
        log.info("Пользователь с id {} поставил like фильму с id {}", userId, id);
    }

    public void deleteLike(final Long id, final Long userId) {
        validId(id);
        validIdUser(userId);
        filmRepository.deleteLike(id, userId);
        log.info("Пользователь с id {} удалил like у фильма с id {}", userId, id);
    }

    public void deleteFilm(Long id) {
        if (filmRepository.getById(id) == null) {
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }
        filmRepository.deleteFilm(id);
    }

    public List<Film> getBestFilm(final int count) {
        log.info("Запрос на получение списка лучших фильмов");
        int size = filmRepository.findAll().size();
        if (size < count) {
            log.info("В запросе на получение списка лучших фильмов count превышвет размер списка");
            return filmRepository.getBestFilm(size);
        }
        log.info("Отбираем лучшие фильмы");
        return filmRepository.getBestFilm(count);
    }

    private void validId(final Long id) {
        if (!filmRepository.getAllId().contains(id)) {
            log.error("Фильма с id = {} нет.", id);
            throw new NotFoundException("Фильма с id = {} нет." + id);
        }
    }

    private void validIdUser(final Long id) {
        if (userRepository.getById(id).isEmpty()) {
            log.error("Киномана с id = {} нет.", id);
            throw new NotFoundException("Киномана с id = {} нет." + id);
        }
    }

    private Film validAndAddMpaGenres(final Film film) {
        if (Objects.isNull(film.getMpa()) && Objects.isNull(film.getGenres())) {
            return film;
        }

        if (Objects.nonNull(film.getMpa())) {
            log.info("Проверка на корректность введенного к фильму mpa");
            film.setMpa(mpaRepository.findById(film.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("В приложении не предусмотрено такое mpa")));
        }

        if (Objects.nonNull(film.getGenres())) {
            log.info("Проверка на корректность введенных к фильму жанров");
            List<Integer> genresId = film.getGenres().stream().map(Genre::getId).toList();
            LinkedHashSet<Genre> genres = genreRepository.getListGenres(genresId).stream()
                    .sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toCollection(LinkedHashSet::new));
            if (film.getGenres().size() == genres.size()) {
                log.info("Жанры переданы верно");
                film.getGenres().clear();
                film.setGenres(genres);
            } else {
                log.warn("Передан несуществующий жанр");
                throw new NotFoundException("Передан несуществующий жанр");
            }
        }

        return film;
    }

}