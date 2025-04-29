package ru.yandex.practicum.filmorate.dal.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dal.repositories.MpaRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MpaDbStorage implements MpaStorage {

    MpaRepository mpaRepository;

    @Override
    public List<Mpa> findAll() {
        log.info("Запрос на получение всех возможных рейтингов");
        return mpaRepository.findAll();
    }

    @Override
    public Mpa findById(final int id) {
        log.info("запрос на получение рейтинга с id {}", id);
        return mpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA с id = " + id + " не существует"));
    }
}