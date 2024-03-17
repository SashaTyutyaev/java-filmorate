package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    FilmController filmController = new FilmController();

    @AfterEach
    void afterEach() {
        filmController.getFilms().clear();
    }

    @Test
    public void createFilm_ShouldReturnFilm() throws ValidationException {
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
    public void getAllFilms_ShouldReturnMapOfFilms() throws ValidationException {
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
    public void createFilm_WithoutName_ShouldThrowException() {
        Film film = Film.builder()
                .description("adad")
                .duration(120)
                .releaseDate(LocalDate.of(2020, 10, 10))
                .build();
        assertThrowsExactly(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void createFilm_With200CharDescription_ShouldReturnFilm() throws ValidationException {
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
    public void createFilm_WithMoreThen200CharDescription_ShouldThrowException() throws ValidationException {
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
    public void createFilm_WithReleaseDateIsBeforeDateLimit_ShouldThrowException() {
        Film film = Film.builder()
                .name("adad")
                .description("dadd")
                .releaseDate(LocalDate.of(1985, 12, 27))
                .duration(120)
                .build();
        assertThrowsExactly(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void createFilm_WithNegativeDuration_ShouldThrowException() {
        Film film = Film.builder()
                .name("adad")
                .description("dadd")
                .releaseDate(LocalDate.of(2000, 12, 27))
                .duration(-1)
                .build();
        assertThrowsExactly(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void updateFilm_ShouldReturnUpdatedFilm() throws ValidationException {
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
    public void updateFilm_WithoutName_ShouldThrowException() throws ValidationException {
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
    public void updateFilm_With200CharDescription_ShouldReturnFilm() throws ValidationException {
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
    public void updateFilm_WithMoreThen200CharDescription_ShouldThrowException() throws ValidationException {
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
    public void updateFilm_WithReleaseDateBeforeDateLimit_ShouldThrowException() throws ValidationException {
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
                .releaseDate(LocalDate.of(1985, 10, 10))
                .duration(120)
                .build();

        assertThrowsExactly(ValidationException.class, () -> filmController.updateFilm(film2));
    }

    @Test
    public void updateFilm_WithNegativeDuration_ShouldThrowException() throws ValidationException {
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