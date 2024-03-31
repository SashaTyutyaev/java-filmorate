package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTest {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(inMemoryUserStorage);
    UserController userController = new UserController(userService);

    @AfterEach
    void afterEach() {
        userController.getUsers().clear();
    }

    @Test
    public void createUserShouldReturnUser() {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user);

        assertEquals(user, userController.getUsers().get(0));
    }

    @Test
    public void getAllUsersShouldReturnMapOfUsers() {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user);

        User user2 = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user2);

        User user3 = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user3);

        assertEquals(3, userController.getUsers().size());
    }

    @Test
    public void createUserShouldThrowExceptionWhenIncorrectEmail() {
        User user = User.builder()
                .name("asas")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUserShouldThrowExceptionWhenNoSobakaInEmail() {
        User user = User.builder()
                .name("asas")
                .email("shshshs.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUserShouldThrowExceptionWhenIncorrectLogin() {
        User user = User.builder()
                .name("asas")
                .email("shshshs@dd.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUserShouldReturnLoginAsNameWhenWithoutName() {
        User user = User.builder()
                .login("sasas")
                .email("shshshs@dd.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        userController.createUser(user);

        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void createUserShouldThrowExceptionWhenBirthdayInFuture() {
        User user = User.builder()
                .name("asas")
                .login("adad")
                .email("shshshs@dd.ru")
                .birthday(LocalDate.of(2025, 3, 17))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void updateUserReturnUpdatedUser() {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .name("asas")
                .id(user.getId())
                .email("asasadadas@mail.ru")
                .login("slslssss")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.updateUser(user2);

        assertEquals(user2, userController.getUsers().get(0));
    }

    @Test
    public void updateUserShouldThrowExceptionWhenWithoutEmail() {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .name("asas")
                .id(user.getId())
                .login("slslssss")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.updateUser(user2));
    }

    @Test
    public void updateUserShouldThrowExceptionWhenWithoutSobakaInEmail() {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .name("asas")
                .id(user.getId())
                .email("aadaddadad.ru")
                .login("slslssss")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.updateUser(user2));
    }

    @Test
    public void updateUserShouldThrowExceptionWhenWithoutLogin() {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .name("asas")
                .id(user.getId())
                .email("asass@mail.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.updateUser(user2));
    }

    @Test
    public void updateUserShouldReturnLoginAsNameWhenWithoutName() {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .id(user.getId())
                .login("slslls")
                .email("asass@mail.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        userController.updateUser(user2);

        assertEquals(user2.getName(), user2.getLogin());
    }

    @Test
    public void updateUserShouldThrowExceptionWhenBirthDayInFuture() {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .name("asas")
                .id(user.getId())
                .login("adad")
                .email("asass@mail.ru")
                .birthday(LocalDate.of(2025, 3, 17))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.updateUser(user2));
    }

}
