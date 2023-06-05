package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.DateReleaseValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private Rating mpa;
    private List<Genre> genres;

    public Film(String name, String description, Long duration, LocalDate releaseDate, int id, Rating mpa, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.mpa = mpa;
        this.genres = genres;
    }

    public List<Genre> getGenres() {
        if (genres == null) {
            genres = new ArrayList<>();
        }
        return genres;
    }
}