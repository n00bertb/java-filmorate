package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "user не должен быть равен null.")
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
    private final Set<Long> friends = new HashSet<>();
}