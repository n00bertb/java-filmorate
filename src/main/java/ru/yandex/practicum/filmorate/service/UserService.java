package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullEqualsException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyIsFriend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        checkName(user);
        userStorage.createUser(user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new NullEqualsException("Id должен быть указан.");
        }
        if (userStorage.findUserById(user.getId()) == null) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден.");
        }
        User existingUser = userStorage.findUserById(user.getId()).toBuilder()
                .email(user.getEmail())
                .login(user.getLogin())
                .birthday(user.getBirthday())
                .name(user.getName() != null && !user.getName().isBlank() ? user.getName() : user.getLogin())
                .build();
        userStorage.updateUser(existingUser);
        return existingUser;
    }

    public List<User> getAllUsers() {
        return userStorage.findAllUsers();
    }

    public User getUserById(Long id) {
        User existingUser = userStorage.findUserById(id);
        if (existingUser == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
        return existingUser;
    }

    public void deleteUserById(Long id) {
        User existingUser = userStorage.findUserById(id);
        if (existingUser == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
        userStorage.deleteUser(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Один из пользователей не найден");
        }
        if (user.getFriends().contains(friendId)) {
            throw new UserAlreadyIsFriend("Пользователь уже в друзьях");
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Ошибка, проверьте правильность ввода userId и friendId.");
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> findFriends(Long id) {
        User user = userStorage.findUserById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
        return user.getFriends()
                .stream()
                .map(userStorage::findUserById)
                .toList();
    }

    public List<User> findCommonFriends(Long userId, Long otherId) {
        User user = getUserById(userId);
        User friend = getUserById(otherId);
        if (user == null || friend == null) {
            throw new NotFoundException("Ошибка, проверьте правильность ввода userId и otherId.");
        }
        return user.getFriends()
                .stream()
                .filter(friend.getFriends()::contains)
                .map(userStorage::findUserById)
                .toList();
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}