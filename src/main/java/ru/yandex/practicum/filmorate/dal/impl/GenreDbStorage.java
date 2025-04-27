package ru.yandex.practicum.filmorate.dal.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dal.repositories.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreDbStorage implements GenreStorage {

    GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        log.info("Запрос на получение всех возможных жанров");
        return genreRepository.findAll();
    }

    @Override
    public Genre findById(final int id) {
        log.info("запрос на получение жанра с id {}", id);
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанра с id = " + id + " не существует"));
    }
}