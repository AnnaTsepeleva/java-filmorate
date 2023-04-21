package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.controller.validators.DateReleaseValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank
    private final String name;
    @Size(max = 200, message = "Описание не может быть более 200 символов")
    private final String description;
    @Positive
    private final long duration;
    @DateReleaseValidator(message = "Некорректная дата релиза")
    private final LocalDate releaseDate;

}
