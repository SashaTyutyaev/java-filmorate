package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private int generatedId = 1;

    private int generateId() {
        return generatedId++;
    }

    @Override
    public User createUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Создан пользователь - " + user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Пользователь отсуствует в списке");
            throw new EntityNotFoundException("Пользователь отсутствует в списке");
        }
        users.put(user.getId(), user);
        log.info("Обновили пользователя под идентификатором - " + user.getId());
        return user;
    }

    @Override
    public List<User> getUsers() {
        log.info("Текущее количестов пользователей - " + users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public Map<Integer, User> getMapOfUsers() {
        return users;
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public void deleteUser(User user) {
        log.info("Пользователь под идентификатором - " + user.getId() + " удален");
        users.remove(user.getId());
    }
}
