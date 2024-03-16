package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Builder
@Data
@AllArgsConstructor
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;

    public Film(int id, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
