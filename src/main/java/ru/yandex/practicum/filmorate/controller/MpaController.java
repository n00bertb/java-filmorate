package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.dao.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MpaController {
    MpaStorage mpaStorage;

    @GetMapping("/mpa")
    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    @GetMapping("/mpa/{id}")
    public Mpa findById(@PathVariable @Positive final int id) {
        return mpaStorage.findById(id);
    }
}