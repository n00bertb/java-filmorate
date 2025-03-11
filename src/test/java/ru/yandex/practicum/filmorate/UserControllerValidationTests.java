package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


@SpringBootTest
class UserControllerValidationTests {

    private UserController userController;
    private User validUser;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @BeforeEach
    void setUp() {
        userController = new UserController();
        validUser = new User();
        validUser.setEmail("test@example.com");
        validUser.setLogin("testLogin");
        validUser.setName("Test User");
        validUser.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void createValidUser() {
        User createdUser = userController.create(validUser);
        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertEquals(validUser.getEmail(), createdUser.getEmail());
    }

    @Test
    void createUserInvalidEmail() {
        validUser.setEmail("invalid-email");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        Assertions.assertFalse(violations.isEmpty(), "Невалидный email при создании пользователя");
    }

    @Test
    void createUserBlankEmail() {
        validUser.setEmail(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        Assertions.assertFalse(violations.isEmpty(), "Пустой email при создании пользователя");
    }

    @Test
    void createUserNullEmail() {
        validUser.setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        Assertions.assertFalse(violations.isEmpty(), "Пустой email при создании пользователя");
    }

    @Test
    void createUserBlankLogin() {
        validUser.setLogin(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        Assertions.assertFalse(violations.isEmpty(), "Пустой логин пользователя");
    }

    @Test
    void createUserNullLogin() {
        validUser.setLogin(null);
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        Assertions.assertFalse(violations.isEmpty(), "Пустой логин пользователя");
    }

    @Test
    void createUserFutureBirthday() {
        validUser.setBirthday(LocalDate.now().plusDays(1L));
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        Assertions.assertFalse(violations.isEmpty(), "День рождения пользователя в будущем");
    }

    @Test
    void updateUserValidUser() {
        User createdUser = userController.create(validUser);
        createdUser.setName("Updated Name");
        User updatedUser = userController.update(createdUser);
        Assertions.assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    void updateUserInvalidEmail() {
        User createdUser = userController.create(validUser);
        createdUser.setEmail("invalid-email");
        Set<ConstraintViolation<User>> violations = validator.validate(createdUser);
        Assertions.assertFalse(violations.isEmpty(), "Невалидный email при обновлении пользователя");
    }

    @Test
    void updateUserInvalidId() {
        validUser.setId(9999);
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.update(validUser);
        });
    }

    @Test
    void updateUserFutureBirthday() {
        User createdUser = userController.create(validUser);
        createdUser.setBirthday(LocalDate.now().plusDays(1L));
        Set<ConstraintViolation<User>> violations = validator.validate(createdUser);
        Assertions.assertFalse(violations.isEmpty(), "День рождения пользователя в будущем при обновлении");
    }
}