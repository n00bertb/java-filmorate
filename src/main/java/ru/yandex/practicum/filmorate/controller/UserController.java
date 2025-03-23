package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap();
    private int current = 0;

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        user.setId(++current);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Сущность успешно создана: id {}", user.getId());
        log.debug("user: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User newUser) {
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getName() == null) {
                newUser.setName(newUser.getLogin());
                oldUser.setName(newUser.getName());
            } else {
                oldUser.setName(newUser.getName());
            }

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            users.put(oldUser.getId(), oldUser);
            log.info("Сущность успешно обновлена: id {}", oldUser.getId());
            log.debug("user: {}", oldUser);
            return oldUser;
        } else {
            throw new ValidationException("Такого пользователя нет в списке!");
        }
    }
}