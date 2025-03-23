package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @EqualsAndHashCode.Include
    private Long id;
    @NotBlank(message = "Электронная почта не может быть пустой!")
    @Email(message = "Введенный email не соответствует формату email-адресов!")
    @EqualsAndHashCode.Exclude
    private String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы!")
    @EqualsAndHashCode.Exclude
    private String login;
    @EqualsAndHashCode.Exclude
    private String name;
    @Past(message = "Дата рождения не может быть в будущем!")
    @EqualsAndHashCode.Exclude
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}