
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsUsers;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmsUsersStorage;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.FilmsUsersDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.memory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.memory.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
class FilmControllerTest {

    FriendshipStorage friendshipStorage;
    FilmsUsersDbStorageImpl filmsUsersStorage;
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(inMemoryUserStorage, friendshipStorage);
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage, userService,filmsUsersStorage);
    FilmController filmController = new FilmController(filmService);

    @AfterEach
    void afterEach() {
        filmController.getFilms().clear();
    }

    @Test
    public void createFilmShouldReturnFilm() {
        Film film = Film.builder()
                .name("adad")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film);
        assertEquals(film, filmController.getFilms().get(0));
    }

    @Test
    public void getAllFilmsShouldReturnMapOfFilms() {
        Film film = Film.builder()
                .name("adad")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film);
        Film film2 = Film.builder()
                .name("adaddaad")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film2);
        Film film3 = Film.builder()
                .name("adassd")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film3);

        assertEquals(3, filmController.getFilms().size());
    }

    @Test
    public void createFilmShouldThrowExceptionWithoutName() {
        Film film = Film.builder()
                .description("adad")
                .duration(120)
                .releaseDate(LocalDate.of(2020, 10, 10))
                .build();
        assertThrowsExactly(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void createFilmShouldReturnFilmWith200CharDescription() {
        Film film = Film.builder()
                .name("adad")
                .description("daadadaaaafjsdhfjhfjhdsfjhfjksdhfjsdfhjdsfhjsfhsjfhkjsfhjshdfu3hfur3hfurhugh3gurogjrigjirejgiegghjgytvytyghghkghkghghgkjgjhghjkhghkghghghjjghghkgj" +
                        "ireigjreoijietrjboitrjgtifhfhfhfhfhhfhffhfhfhfhfhhfhfh")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film);
        assertEquals(film, filmController.getFilms().get(0));
    }

    @Test
    public void createFilmShouldThrowExceptionWithMoreThen200CharDescription() {
        Film film = Film.builder()
                .name("adad")
                .description("daadadaaaafjsdhfjhfjhdsfjhfjksdhfjsdfhjdsfhjsfhsjfhkjsfhjshdfu3hfur3hfurhugh3gurogjrigjirejgiegghjgytvytyghghkghkghghgkjgjhghjkhghkghghghjjghghkgj" +
                        "ireigjreoijietrjboidadtrjgtihgjhgjhghfjffghfgfgfgf5yjftkuflygygyigyigy")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        assertThrowsExactly(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void createFilmShouldThrowExceptionWithReleaseDateIsBeforeDateLimit() {
        Film film = Film.builder()
                .name("adad")
                .description("dadd")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(120)
                .build();
        assertThrowsExactly(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void createFilmShouldThrowExceptionWithNegativeDuration() {
        Film film = Film.builder()
                .name("adad")
                .description("dadd")
                .releaseDate(LocalDate.of(2000, 12, 27))
                .duration(-1)
                .build();
        assertThrowsExactly(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void updateFilmShouldReturnUpdatedFilm() {
        Film film = Film.builder()
                .name("adad")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film);

        Film film2 = Film.builder()
                .id(film.getId())
                .name("adad")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.updateFilm(film2);

        assertEquals(film2, filmController.getFilms().get(0));
    }

    @Test
    public void updateFilmShouldThrowExceptionWithoutName() {
        Film film = Film.builder()
                .name("adad")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film);

        Film film2 = Film.builder()
                .id(film.getId())
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();

        assertThrowsExactly(ValidationException.class, () -> filmController.updateFilm(film2));
    }

    @Test
    public void updateFilmShouldReturnFilmWith200CharDescription() {
        Film film = Film.builder()
                .name("adad")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film);

        Film film2 = Film.builder()
                .id(film.getId())
                .name("adad")
                .description("daadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgug" +
                        "yuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhh")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.updateFilm(film2);

        assertEquals(film2, filmController.getFilms().get(0));
    }

    @Test
    public void updateFilmShouldThrowExceptionWithMoreThen200CharDescription() {
        Film film = Film.builder()
                .name("adad")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film);

        Film film2 = Film.builder()
                .id(film.getId())
                .name("adad")
                .description("daadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgug" +
                        "yuguygyuhhhdaadgtgugyuguygyuhdfsdfsdfdsfdshhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhhdaadgtgugyuguygyuhhh")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();

        assertThrowsExactly(ValidationException.class, () -> filmController.updateFilm(film2));
    }

    @Test
    public void updateFilmShouldThrowExceptionWithReleaseDateBeforeDateLimit() {
        Film film = Film.builder()
                .name("adad")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film);

        Film film2 = Film.builder()
                .id(film.getId())
                .name("adad")
                .description("guygyuhhh")
                .releaseDate(LocalDate.of(1895, 10, 10))
                .duration(120)
                .build();

        assertThrowsExactly(ValidationException.class, () -> filmController.updateFilm(film2));
    }

    @Test
    public void updateFilmShouldThrowExceptionWithNegativeDuration() {
        Film film = Film.builder()
                .name("adad")
                .description("daad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(120)
                .build();
        filmController.createFilm(film);

        Film film2 = Film.builder()
                .id(film.getId())
                .name("adad")
                .description("adad")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(-1)
                .build();

        assertThrowsExactly(ValidationException.class, () -> filmController.updateFilm(film2));
    }
}
