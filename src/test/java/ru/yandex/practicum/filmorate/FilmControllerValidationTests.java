package ru.yandex.practicum.filmorate;
/*
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

@SpringBootTest
class FilmControllerValidationTests {

    private FilmController filmController;
    private Film validFilm;
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @BeforeEach
    void setUp() {
        //filmController = new FilmController();
        validFilm = new Film();
        validFilm.setName("Test Film");
        validFilm.setDescription("Test Description");
        validFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        validFilm.setDuration(120);
    }

    @Test
    void createValidFilm() {
        Film createdFilm = filmController.create(validFilm);
        Assertions.assertNotNull(createdFilm.getId());
        Assertions.assertEquals(validFilm.getName(), createdFilm.getName());
    }

    @Test
    void createFilmWhenNameIsBlank() {
        validFilm.setName(" ");
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        Assertions.assertFalse(violations.isEmpty(), "Фильм с пустым именем не должен создаваться");
    }

    @Test
    void createFilmWhenNameIsNull() {
        validFilm.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        Assertions.assertFalse(violations.isEmpty(), "Фильм с пустым именем не должен создаваться");
    }

    @Test
    void createFilmWhenDescriptionIsTooLong() {
        validFilm.setDescription("a".repeat(201));
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        Assertions.assertFalse(violations.isEmpty(), "Превышена длинна описания в 200 символов");
    }

    @Test
    void createFilmWhenReleaseDateIsBeforeFilmBirthday() {
        validFilm.setReleaseDate(FILM_BIRTHDAY.minusDays(1L));
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(validFilm);
        });
    }

    @Test
    void createFilmWhenReleaseDateIsFilmBirthday() {
        validFilm.setReleaseDate(FILM_BIRTHDAY);
        Film createdFilm = filmController.create(validFilm);
        Assertions.assertNotNull(createdFilm.getId());
        Assertions.assertEquals(validFilm.getReleaseDate(), createdFilm.getReleaseDate());
    }

    @Test
    void createFilmWhenDurationIsNegative() {
        validFilm.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        Assertions.assertFalse(violations.isEmpty(), "Продолжительность фильма отрицательное число");
    }

    @Test
    void createFilmWhenDurationIsZero() {
        validFilm.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        Assertions.assertFalse(violations.isEmpty(), "Продолжительность фильма равна нулю");
    }

    @Test
    void updateFilmWhenFilmIsValid() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setName("Updated Film Name");
        Film updatedFilm = filmController.update(createdFilm);
        Assertions.assertEquals("Updated Film Name", updatedFilm.getName());
    }

    @Test
    void updateFilmWhenIdIsInvalid() {
        validFilm.setId(9999L);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.update(validFilm);
        });
    }

    @Test
    void updateFilmWhenNameIsBlank() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setName(" ");
        Set<ConstraintViolation<Film>> violations = validator.validate(createdFilm);
        Assertions.assertFalse(violations.isEmpty(), "Пустое имя фильма при обновлении");
    }

    @Test
    void updateFilmWhenNameIsNull() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(createdFilm);
        Assertions.assertFalse(violations.isEmpty(), "Пустое имя фильма при обновлении");
    }

    @Test
    void updateFilmWhenDescriptionIsTooLong() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setDescription("a".repeat(201));
        Set<ConstraintViolation<Film>> violations = validator.validate(createdFilm);
        Assertions.assertFalse(violations.isEmpty(), "Превышена длинна описания в 200 символов при обновлении фильма");
    }

    @Test
    void updateFilmWhenDescriptionIs200Char() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setDescription("a".repeat(200));
        Film updatedFilm = filmController.update(createdFilm);
        Assertions.assertEquals(createdFilm.getDescription(), updatedFilm.getDescription());
    }

    @Test
    void updateFilmWhenReleaseDateIsBeforeFilmBirthday() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setReleaseDate(FILM_BIRTHDAY.minusDays(1L));
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.update(createdFilm);
        });
    }

    @Test
    void updateFilmWhenDurationIsNegative() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(createdFilm);
        Assertions.assertFalse(violations.isEmpty(), "Продолжительность фильма отрицательное число при обновлении");
    }

    @Test
    void updateFilmWhenDurationIsZero() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        Assertions.assertFalse(violations.isEmpty(), "Продолжительность фильма равна нулю при обновлении");
    }
}

 */