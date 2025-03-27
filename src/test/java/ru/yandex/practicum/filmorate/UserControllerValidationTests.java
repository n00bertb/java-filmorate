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
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@SpringBootTest
class UserControllerValidationTests {

    private UserController userController;
    private UserService userService;
    private UserStorage userStorage;
    private User validUser;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
        validUser = new User();
        validUser.setEmail("test@example.com");
        validUser.setLogin("testLogin");
        validUser.setName("Test User");
        validUser.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void createValidUser() {
        User createdUser = userController.createUser(validUser);
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
        User createdUser = userController.createUser(validUser);
        createdUser.setName("Updated Name");
        User updatedUser = userController.updateUser(createdUser);
        Assertions.assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    void updateUserInvalidEmail() {
        User createdUser = userController.createUser(validUser);
        createdUser.setEmail("invalid-email");
        Set<ConstraintViolation<User>> violations = validator.validate(createdUser);
        Assertions.assertFalse(violations.isEmpty(), "Невалидный email при обновлении пользователя");
    }

    @Test
    void updateUserInvalidId() {
        validUser.setId(9999L);
        Assertions.assertThrows(NotFoundException.class, () -> {
            userController.updateUser(validUser);
        });
    }

    @Test
    void updateUserFutureBirthday() {
        User createdUser = userController.createUser(validUser);
        createdUser.setBirthday(LocalDate.now().plusDays(1L));
        Set<ConstraintViolation<User>> violations = validator.validate(createdUser);
        Assertions.assertFalse(violations.isEmpty(), "День рождения пользователя в будущем при обновлении");
    }
}