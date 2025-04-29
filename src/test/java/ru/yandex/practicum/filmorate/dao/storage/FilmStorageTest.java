package ru.yandex.practicum.filmorate.dao.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dal.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Import({UserDbStorage.class})
class FilmStorageTest {

    @Autowired
    FilmStorage filmStorage;


    @Test
    @DisplayName("Should find all films")
    void findAllTest() {
        Collection<Film> films = filmStorage.findAllFilms();

        assertThat(films)
                .isNotEmpty()
                .hasSize(7)
                .satisfies(filmList -> {
                    Film firstFilm = filmList.iterator().next();
                    assertThat(firstFilm)
                            .hasFieldOrPropertyWithValue("id", 1L)
                            .hasFieldOrPropertyWithValue("name", "Филосовский камень");
                });
    }

    @Test
    @DisplayName("Should create new film")
    void createTest() {
        Film newFilm = new Film();
        newFilm.setName("Проклятое дитя");
        newFilm.setDescription("description8");
        newFilm.setReleaseDate(LocalDate.of(2022, 1, 1));
        newFilm.setDuration(128);
        newFilm.setMpa(new Mpa(1, null));

        Film createFilm = filmStorage.createFilm(newFilm);

        assertThat(createFilm).hasFieldOrPropertyWithValue("id", 8L);
        assertThat(createFilm).hasFieldOrPropertyWithValue("name", "Проклятое дитя");
        assertThat(createFilm).hasFieldOrPropertyWithValue("description", "description8");
        assertThat(createFilm).hasFieldOrPropertyWithValue("duration", 128);
        assertThat(createFilm).hasFieldOrPropertyWithValue("releaseDate",
                LocalDate.of(2022, 1, 1));
        assertThat(createFilm.getMpa()).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @DisplayName("FilmRepository_should_not_update")
    void updateTest() {
        Film newFilm = new Film();
        newFilm.setId(8L);
        newFilm.setName("Фантастические твари");
        newFilm.setDescription("description8");
        newFilm.setReleaseDate(LocalDate.of(2001, 11, 22));
        newFilm.setDuration(121);
        newFilm.setMpa(new Mpa(1, null));

        assertThatThrownBy(() -> filmStorage.updateFilm(newFilm))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Should get all films")
    void getAllIdTest() {
        Collection<Film> films = filmStorage.findAllFilms();
        Collection<Long> ids = films.stream().map(Film::getId).collect(Collectors.toList());
        assertThat(ids)
                .isNotEmpty()
                .hasSize(7)
                .contains(1L, 2L, 3L, 4L, 5L, 6L, 7L);
    }

    @Nested
    @DisplayName("Get Film Tests")
    class GetFilmTests {
        @Test
        @DisplayName("Should get film by ID")
        void getByIdTest() {
            Film film = filmStorage.findFilmById(1L);

            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(film).hasFieldOrPropertyWithValue("name", "Филосовский камень");
            assertThat(film).hasFieldOrPropertyWithValue("description", "description1");
            assertThat(film).hasFieldOrPropertyWithValue("duration", 121);
            assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                    LocalDate.of(2001, 11, 22));
            assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 1);
            assertThat(film.getGenres()).hasSize(1);
            assertThat(film.getGenres()).element(0)
                    .hasFieldOrPropertyWithValue("id", 1);
        }

        @Test
        @DisplayName("Should throw exception when film not found")
        void shouldThrowExceptionWhenFilmNotFound() {
            assertThatThrownBy(() -> filmStorage.findFilmById(999L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Like Operations Tests")
    class LikeOperationsTests {
        @Test
        @DisplayName("Should put like to film")
        void putLikeTest() {
            filmStorage.putLike(1L, 1L);

            Collection<Film> bestFilms = filmStorage.getBestFilm(1);
            assertThat(bestFilms)
                    .isNotEmpty()
                    .anySatisfy(film ->
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L));
        }

        @Test
        @DisplayName("Should delete like from film")
        void deleteLikeTest() {
            filmStorage.putLike(1L, 1L);
            filmStorage.deleteLike(1L, 1L);

            Collection<Film> bestFilms = filmStorage.getBestFilm(1);
            assertThat(bestFilms).isEmpty();
        }
    }
}