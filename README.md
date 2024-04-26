# java-filmorate
Template repository for Filmorate project.
![Untitled](https://github.com/SashaTyutyaev/java-filmorate/assets/145023074/d4d41906-357f-4075-9ff5-197fadc7284e)

## Примеры использования
### Добавление пользователя
`INSERT INTO user (login, name, email, birthday) VALUES ('user_login', 'user_name', 'user@mail.com', '2000-01-01');`
### Добавление фильма
`INSERT INTO film (name, release_date, duration, description, rating) VALUES ('movie_ame', '2020-01-01', 120, 'Description', 'PG-16');`
### Добавление жанра к фильму
`INSERT INTO film_genre (film_id, genre_id) VALUES (1,2)`
### Запрос в друзья
`INSERT INTO friendship (user1_id, user2_id, is_approved) VALUES (1, 2, false);`
### Принятие запроса в друзья
`UPDATE friendship SET is_approved = true WHERE user1_id = 1 AND user2_id = 2;`
### Поставить лайк фильму
`INSERT INTO films_users(film_id,user_id) VALUES (1,2);`
