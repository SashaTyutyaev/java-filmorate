package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    private int generatedId = 1;

    private int generateId() {
        return generatedId++;
    }

    private void validateUser(User user) throws ValidationException {
        if (user == null) {
            log.info("Пустые поля пользователя");
            throw new ValidationException("Пустые поля пользователя");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("У пользователя неккоретная почта");
            throw new ValidationException("У пользователя неккоректная почта");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.info("У пользователя неккоректный логин");
            throw new ValidationException("У пользователя неккоректный логин");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("У пользователя пустое имя, поэтому его логин стал именем");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("У пользователя неккоректная дата рождения");
            throw new ValidationException("У пользователя неккоректная дата рождения");
        }
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        validateUser(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Создан пользователь - " + user);
        return user;
    }

    @PutMapping
    User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Пользователь отсуствует в списке");
            throw new ValidationException("Пользователь отсутствует в списке");
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.info("Обновили пользователя под идентификатором - " + user.getId());
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Текущее количестов пользователей - " + users.size());
        return new ArrayList<>(users.values());
    }
}
