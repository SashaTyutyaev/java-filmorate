package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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

}
