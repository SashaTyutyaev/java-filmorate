# java-filmorate
Template repository for Filmorate project.

<img width="755" alt="Снимок экрана 2024-05-01 в 04 27 37" src="https://github.com/SashaTyutyaev/java-filmorate/assets/145023074/91b4ac5f-beef-4193-9556-015dd66cfffd">

## Примеры использования
### Добавление пользователя

`INSERT INTO user (login, name, email, birthday) VALUES ('user_login', 'user_name', 'user@mail.com', '2000-01-01');`

### Добавление фильма

`INSERT INTO film (name, release_date, duration, description, rating_id) VALUES ('movie_ame', '2020-01-01', 120, 'Description', '2');`

### Добавление жанра к фильму

`INSERT INTO film_genre (film_id, genre_id) VALUES (1,2)`

### Запрос в друзья
`INSERT INTO friendship (user1_id, user2_id) VALUES (1, 2);`

### Поставить лайк фильму

`INSERT INTO films_users(film_id,user_id) VALUES (1,2);`
