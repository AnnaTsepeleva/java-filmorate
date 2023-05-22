package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rating {
    private int id;
    private final String name;

    public Rating(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
