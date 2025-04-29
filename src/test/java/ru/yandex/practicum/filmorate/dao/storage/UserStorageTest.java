package ru.yandex.practicum.filmorate.dao.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import ru.yandex.practicum.filmorate.dal.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportResource
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserStorageTest {
    UserStorage userStorage;

    @Test
    @Order(1)
    @DisplayName("UserRepository_findById")
    void findByIdTest() {
        User user = userStorage.findUserById(1L);
        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(user).hasFieldOrPropertyWithValue("login", "antuan");
        assertThat(user).hasFieldOrPropertyWithValue("name", "Антуан");
        assertThat(user).hasFieldOrPropertyWithValue("email", "antuangrad@mail.ru");
        assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1993, 3, 12));
    }

    @Test
    @Order(2)
    @DisplayName("UserRepository_findAll")
    void findAllTest() {
        Collection<User> users = userStorage.findAllUsers();
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(4);
        assertThat(users).element(0).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(users).element(0).hasFieldOrPropertyWithValue("name", "Антуан");
        assertThat(users).element(1).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(users).element(1).hasFieldOrPropertyWithValue("name", "Сергей");
        assertThat(users).element(2).hasFieldOrPropertyWithValue("id", 3L);
        assertThat(users).element(2).hasFieldOrPropertyWithValue("name", "Василий");
        assertThat(users).element(3).hasFieldOrPropertyWithValue("id", 4L);
        assertThat(users).element(3).hasFieldOrPropertyWithValue("name", "Павел");
    }

    @Test
    @Order(3)
    @DisplayName("UserRepository_create")
    void createTest() {
        User newUser = new User();
        newUser.setLogin("anastasia_b");
        newUser.setName("Настя");
        newUser.setEmail("anastasia_b@mail.ru");
        newUser.setBirthday(LocalDate.of(1987, 9, 22));


        User user = userStorage.createUser(newUser);

        assertThat(user).hasFieldOrPropertyWithValue("id", newUser.getId());
        assertThat(user).hasFieldOrPropertyWithValue("login", "anastasia_b");
        assertThat(user).hasFieldOrPropertyWithValue("name", "Настя");
        assertThat(user).hasFieldOrPropertyWithValue("email", "anastasia_b@mail.ru");
        assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1987, 9, 22));
    }

    @Test
    @Order(4)
    @DisplayName("UserRepository_update")
    void updateTest() {
        User newUser = new User();
        newUser.setId(1L); // Используйте существующее значение id
        newUser.setLogin("anastasia_ba");
        newUser.setName("Настя Балдина");
        newUser.setEmail("anastasia_ba87@mail.ru");
        newUser.setBirthday(LocalDate.of(1987, 9, 22));

        User updatedUser = userStorage.updateUser(newUser);
        User user = userStorage.updateUser(newUser);

        assertThat(user).hasFieldOrPropertyWithValue("id", updatedUser.getId());
        assertThat(user).hasFieldOrPropertyWithValue("login", "anastasia_ba");
        assertThat(user).hasFieldOrPropertyWithValue("name", "Настя Балдина");
        assertThat(user).hasFieldOrPropertyWithValue("email", "anastasia_ba87@mail.ru");
        assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1987, 9, 22));
    }

    @Test
    @Order(5)
    @DisplayName("UserRepository_getAllFriends")
    void getAllFriendsTest() {
        List<User> users = userStorage.getAllFriends(1L);
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(2);
        assertThat(users).first().hasFieldOrPropertyWithValue("id", 2L);
        assertThat(users).element(1).hasFieldOrPropertyWithValue("id", 3L);
    }

    @Test
    @Order(6)
    @DisplayName("UserRepository_getMutualFriends")
    void getMutualFriendsTest() {
        List<User> users = userStorage.getMutualFriends(2L, 3L);
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(2);
        assertThat(users).element(0).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(users).element(1).hasFieldOrPropertyWithValue("id", 4L);
    }
}