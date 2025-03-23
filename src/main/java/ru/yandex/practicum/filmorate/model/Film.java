package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @EqualsAndHashCode.Include
    private Long id;
    @NotBlank(message = "Название не может быть пустым!")
    @EqualsAndHashCode.Exclude
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов!")
    @EqualsAndHashCode.Exclude
    private String description;
    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом!")
    @EqualsAndHashCode.Exclude
    private int duration;
    private Set<Long> usersLike = new HashSet<>();
}