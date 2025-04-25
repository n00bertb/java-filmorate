package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyIsFriend extends RuntimeException {
    public UserAlreadyIsFriend(String msg) {
        super(msg);
    }
}