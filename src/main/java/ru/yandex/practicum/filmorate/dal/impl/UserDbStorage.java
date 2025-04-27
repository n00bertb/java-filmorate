package ru.yandex.practicum.filmorate.dal.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.UserStorage;
import ru.yandex.practicum.filmorate.dal.repositories.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDbStorage implements UserStorage {

    UserRepository userRepository;

    @Override
    public User findUserById(final Long id) {
        log.info("Запрос на получение пользователя под id {}", id);
        validId(id);
        return userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = " + id + " не существует"));
    }

    public List<User> findAllUsers() {
        log.info("Запрос на получение списка пользователей");
        return userRepository.findAll();
    }

    public User createUser(final User user) {
        User newUser = userRepository.create(user);
        log.info("Пользователь успешно добавлен под id {}", user.getId());
        return newUser;
    }

    public User updateUser(final User user) {
        validId(user.getId());
        User newUser = userRepository.update(user);
        log.info("Пользователь с id {} успешно обновлен", user.getId());
        return newUser;
    }

    public void addNewFriend(final Long id, final Long friendId) {
        validUserEqualsFriend(id, friendId, "Нельзя добавить пользователя в друзья к самому себе");
        userRepository.addNewFriend(id, friendId);
        log.info("Пользователь с id {} успешно добавлен в друзья к пользователю с id {}", friendId, id);
    }

    public void deleteFriend(final Long id, final Long friendId) {
        validUserEqualsFriend(id, friendId, "Нельзя удалить пользователя из друзей у самого себя");
        userRepository.deleteFriend(id, friendId);
        log.info("Пользователь с id {} успешно удален из друзей у пользователя с id {}", friendId, id);
    }

    public void deleteUser(Long id) {
        User existingUser = userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));

        userRepository.deleteUser(id);
    }

    public List<User> getAllFriends(final Long id) {
        validId(id);
        log.info("Запрос на получение всех друзей пользователя с id {}", id);
        return userRepository.getAllFriends(id);
    }

    public List<User> getMutualFriends(final Long id, final Long otherId) {
        validUserEqualsFriend(id, otherId, "Нельзя проверять соответствие друзей у себя и себя");
        log.info("Общие друзья пользователь с id {} и пользователя с id {}", otherId, id);
        return userRepository.getMutualFriends(id, otherId);
    }

    private void validUserEqualsFriend(final Long id, final Long friendId, final String message) {
        validId(id);
        validId(friendId);
        if (Objects.equals(id, friendId)) {
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    private void validId(final Long id) {
        if (userRepository.getById(id).isEmpty()) {
            log.warn("Пользователя с id = {} нет.", id);
            throw new NotFoundException("Пользователя с id = {} нет." + id);
        }
    }
}