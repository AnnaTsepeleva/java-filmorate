package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmDbStorage filmDbStorage;


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
        return filmDbStorage.createFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

    public Film editFilm(Film film) {
        return filmDbStorage.editFilm(film);
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
        return filmDbStorage.filmRate(count);
    }

    public Film findFilmById(int id) {
        return filmStorage.getFilmByID(id);

    }

}
