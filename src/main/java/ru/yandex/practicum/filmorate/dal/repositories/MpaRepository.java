package ru.yandex.practicum.filmorate.dal.repositories;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaRepository {
    List<Mpa> findAll();

    Optional<Mpa> findById(int id);
}