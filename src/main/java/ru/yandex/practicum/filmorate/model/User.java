package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Integer id;

    @Email(message = "Email должен быть в корректном формате")
    @NotNull(message = "Email не должен быть null")
    @NotBlank(message = "Email должен быть пустым")
    private String email;

    @NotNull(message = "Логин не должен быть null")
    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин должен быть без пробелов")
    private String login;

    private String name;

    @NotBlank(message = "Дата рождения не должна быть пустой")
    @NotNull(message = "Дата рождения не должна быть null")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
