package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.RatingDbStorage;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

@Service
public class RatingService {

    private final RatingDbStorage ratingDbStorage;

    @Autowired
    public RatingService(RatingDbStorage ratingDbStorage) {
        this.ratingDbStorage = ratingDbStorage;
    }

    public List<Rating> findAllRating() {
        return ratingDbStorage.findAllRating();
    }

    public Rating getRatingById(int id) {
        return ratingDbStorage.getRatingById(id);
    }

}
