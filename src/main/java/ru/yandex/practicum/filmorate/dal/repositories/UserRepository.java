package ru.yandex.practicum.filmorate.dal.repositories;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getById(Long id);

    List<User> findAll();

    User create(User user);

    User update(User user);

    void addNewFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    void deleteUser(Long id);

    List<User> getAllFriends(Long id);

    List<User> getMutualFriends(Long id, Long otherId);
}