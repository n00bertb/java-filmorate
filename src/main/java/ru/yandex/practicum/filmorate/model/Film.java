package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashSet;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "film не должен быть равен null.")
public class Film {
    @EqualsAndHashCode.Include
    private Long id;
    @NotBlank(message = "Название не может быть пустым!")
    @EqualsAndHashCode.Exclude
    private String name;
    @NotNull
    @Size(max = 200, message = "Максимальная длина описания — 200 символов!")
    @EqualsAndHashCode.Exclude
    private String description;
    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом!")
    @EqualsAndHashCode.Exclude
    private Integer duration;
    private final Set<Long> usersLike = new HashSet<>();
    @NotNull
    Mpa mpa;
    LinkedHashSet<Genre> genres = new LinkedHashSet<>();
}