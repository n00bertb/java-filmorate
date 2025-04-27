package ru.yandex.practicum.filmorate.dal.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {


    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void addNewFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    void deleteUser(Long id);

    List<User> getAllFriends(Long id);

    List<User> getMutualFriends(Long id, Long otherId);

    User findUserById(Long id);
}