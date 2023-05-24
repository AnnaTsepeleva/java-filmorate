package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {

    Comparator<Film> filmComparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            if (o1.getLikes().size() == 0 && o2.getLikes().size() == 0) {
                return 1;
            }
            return o2.getLikes().size() - o1.getLikes().size();
        }
    };
    @Autowired
    private final FilmStorage filmStorage;

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
        Film film = filmStorage.getFilmByID(filmId);
        Set<Integer> likeList = film.getLikes();
        likeList.add(userId);
        film.setLikes(likeList);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmByID(filmId);
        Set<Integer> likeList = film.getLikes();
        likeList.remove(userId);
        film.setLikes(likeList);
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
        return filmStorage.getFilmByID(id);
    }

}
