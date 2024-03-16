package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTest {

    UserController userController = new UserController();

    @AfterEach
    void afterEach() {
        userController.getUsers().clear();
    }

    @Test
    public void createUser_ShouldReturnUser() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.createUser(user);

        assertEquals(user,userController.getUsers().get(user.getId()));
    }

    @Test
    public void getAllUsers_ShouldReturnMapOfUsers() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.createUser(user);

        User user2 = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.createUser(user2);

        User user3 = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.createUser(user3);

        assertEquals(3,userController.getUsers().size());
    }

    @Test
    public void createUser_WithoutEmail_ShouldThrowException() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_WithoutSobakaInEmail_ShouldThrowException() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .email("shshshs.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_WithoutLogin_ShouldThrowException() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .email("shshshs@dd.ru")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_WithoutName_ShouldReturnLoginAsName() throws ValidationException {
        User user = User.builder()
                .login("sasas")
                .email("shshshs@dd.ru")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        userController.createUser(user);

        assertEquals(user.getName(),user.getLogin());
    }

    @Test
    public void createUser_WithBirthdayInFuture_ShouldThrowException() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .login("adad")
                .email("shshshs@dd.ru")
                .birthday(LocalDate.of(2024,3,17))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void updateUser_ReturnUpdatedUser() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .name("asas")
                .id(user.getId())
                .email("asasadadas@mail.ru")
                .login("slslssss")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.updateUser(user2);

        assertEquals(user2,userController.getUsers().get(user.getId()));
    }

    @Test
    public void updateUser_WithoutEmail_ShouldThrowException() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .name("asas")
                .id(user.getId())
                .login("slslssss")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.updateUser(user2));
    }

    @Test
    public void updateUser_WithoutSobakaInEmail_ShouldThrowException() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .name("asas")
                .id(user.getId())
                .email("aadaddadad.ru")
                .login("slslssss")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.updateUser(user2));
    }

    @Test
    public void updateUser_WithoutLogin_ShouldThrowException() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .name("asas")
                .id(user.getId())
                .email("asass@mail.ru")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.updateUser(user2));
    }

    @Test
    public void updateUser_WithoutName_ShouldReturnLoginAsName() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .id(user.getId())
                .login("slslls")
                .email("asass@mail.ru")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        userController.updateUser(user2);

        assertEquals(user2.getName(),user2.getLogin());
    }

    @Test
    public void updateUser_WhenBirthDayInFuture_ShouldThrowException() throws ValidationException {
        User user = User.builder()
                .name("asas")
                .email("asass@mail.ru")
                .login("slsls")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .name("asas")
                .id(user.getId())
                .login("adad")
                .email("asass@mail.ru")
                .birthday(LocalDate.of(2024,3,17))
                .build();

        assertThrowsExactly(ValidationException.class, () -> userController.updateUser(user2));
    }

}
