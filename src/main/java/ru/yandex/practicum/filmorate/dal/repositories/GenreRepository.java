package ru.yandex.practicum.filmorate.dal.repositories;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> findAll();

    Optional<Genre> findById(int id);

    List<Genre> getListGenres(List<Integer> ids);
}