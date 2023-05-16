package ru.yandex.practicum.filmorate.DAO;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingDbStorage {
    List<Rating> findAllRating();

    Rating getRatingById(int id);
}
