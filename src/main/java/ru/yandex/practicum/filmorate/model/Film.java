package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.DateReleaseValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    @NotBlank
    private final String name;
    @Size(min = 1, max = 200, message = "Описание не может быть пустым и более 200 символов")
    private final String description;
    @Positive
    private final long duration;
    @DateReleaseValidator(message = "Некорректная дата релиза")
    private final LocalDate releaseDate;
    private int id;
    private Set<Integer> likes;

    Film(int id, String name, String description, long duration, LocalDate releaseDate, Set<Integer> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.likes = new HashSet<>();
    }

}
