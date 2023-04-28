package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validators.ValidationException;

import java.util.*;

@Service
public class FilmService {

    @Autowired
    private FilmStorage filmStorage = new InMemoryFilmStorage();

    Comparator<Film> filmComparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            if (o1.getLikes().size() == 0 && o2.getLikes().size() == 0) {
                return 1;
            }
            return o2.getLikes().size() - o1.getLikes().size();
        }
    };


    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film editFilm(Film film) {
        return filmStorage.editFilm(film);
    }

    public void deleteFilm(Film film) {
        filmStorage.deleteFilm(film);
    }

    public void likeFilm(int filmId, int userId) {
        Set<Integer> likes = filmStorage.getFilmByID(filmId).getLikes();
        likes.add(userId);
        filmStorage.getFilmByID(filmId).setLikes(likes);
    }

    public void deleteLike(int filmId, int userId) {
        Set<Integer> likes = filmStorage.getFilmByID(filmId).getLikes();
        if (likes.isEmpty()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Фильм не имеет лайков");
        }
        if (!likes.contains(userId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь не лайкал фильм");
        }
        likes.remove(userId);
        filmStorage.getFilmByID(filmId).setLikes(likes);
    }

    public List<Film> filmRate(int count) {
        ArrayList<Film> films = filmStorage.getAllFilms();
        TreeMap<Film, Integer> likes = new TreeMap<>(filmComparator);
        List<Film> ratesList = new ArrayList<Film>();
        for (Film film : films) {
            likes.put(film, film.getId());
        }
        if (likes.size() < count) {
            count = likes.size();
        }
        for (int i = 0; i < count; i++) {
            ratesList.add(likes.firstKey());
            likes.remove(likes.firstKey());
        }
        return ratesList;
    }

    public Film findFilmById(int id) {
        if (filmStorage.getFilmByID(id) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        return filmStorage.getFilmByID(id);
    }

}
