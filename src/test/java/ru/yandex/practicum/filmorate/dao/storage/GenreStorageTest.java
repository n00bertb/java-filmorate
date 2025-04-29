package ru.yandex.practicum.filmorate.dao.storage;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dal.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreStorageTest {
    GenreStorage genreStorage;

    @Test
    @Order(1)
    @DisplayName("GenreRepository_findAll")
    void findAllTest() {
        Optional<List<Genre>> genresOptional = Optional.ofNullable(genreStorage.findAll());
        assertThat(genresOptional)
                .isPresent()
                .hasValueSatisfying(genres -> {
                    assertThat(genres).isNotEmpty();
                    assertThat(genres).hasSize(6);
                    assertThat(genres).element(0).hasFieldOrPropertyWithValue("id", 1);
                    assertThat(genres).element(0).hasFieldOrPropertyWithValue("name", "Комедия");
                    assertThat(genres).element(1).hasFieldOrPropertyWithValue("id", 2);
                    assertThat(genres).element(1).hasFieldOrPropertyWithValue("name", "Драма");
                    assertThat(genres).element(2).hasFieldOrPropertyWithValue("id", 3);
                    assertThat(genres).element(2).hasFieldOrPropertyWithValue("name", "Мультфильм");
                    assertThat(genres).element(3).hasFieldOrPropertyWithValue("id", 4);
                    assertThat(genres).element(3).hasFieldOrPropertyWithValue("name", "Триллер");
                    assertThat(genres).element(4).hasFieldOrPropertyWithValue("id", 5);
                    assertThat(genres).element(4).hasFieldOrPropertyWithValue("name", "Документальный");
                    assertThat(genres).element(5).hasFieldOrPropertyWithValue("id", 6);
                    assertThat(genres).element(5).hasFieldOrPropertyWithValue("name", "Боевик");
                });
    }

    @Test
    @Order(2)
    @DisplayName("GenreRepository_findById")
    void findByIdTest() {
        Genre genre = genreStorage.findById(1);
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1);
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    @Order(3)
    @DisplayName("GenreRepository_getListGenres")
    void getListGenresTest() {
        Collection<Genre> genres = genreStorage.findAll();
        assertThat(genres).isNotEmpty();
        assertThat(genres).hasSize(6);
        assertThat(genres).element(0).hasFieldOrPropertyWithValue("id", 1);
        assertThat(genres).element(0).hasFieldOrPropertyWithValue("name", "Комедия");
        assertThat(genres).element(1).hasFieldOrPropertyWithValue("id", 2);
        assertThat(genres).element(1).hasFieldOrPropertyWithValue("name", "Драма");
        assertThat(genres).element(2).hasFieldOrPropertyWithValue("id", 3);
        assertThat(genres).element(2).hasFieldOrPropertyWithValue("name", "Мультфильм");
    }
}