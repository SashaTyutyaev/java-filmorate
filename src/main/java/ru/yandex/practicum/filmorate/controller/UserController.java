package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
        User user1 = userService.createUser(user);
        validateUser(user1);
        return user1;
    }

    @PutMapping
    User updateUser(@RequestBody User user) {
        validateUser(user);
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @DeleteMapping
    public void deleteUser(User user) {
        userService.deleteUser(user);
    }

    @PutMapping("/{id}/friends/{friendsId}")
    public void addFriend(@RequestBody @PathVariable int id, @RequestBody @PathVariable int friendsId) {
        validateUser(userService.getUserById(id));
        validateUser(userService.getUserById(friendsId));
        userService.addFriend(id, friendsId);
    }

    @DeleteMapping("/{id}/friends/{friendsId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendsId) {
        validateUser(userService.getUserById(id));
        validateUser(userService.getUserById(friendsId));
        userService.deleteFriend(id, friendsId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
