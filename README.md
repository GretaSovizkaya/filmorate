# Рейтинг фильмов

Filmorate - бэкенд для сервиса,  работающий с фильмами и оценками пользователей, а также возвращающий топ-5 фильмов, рекомендованных к просмотру.  

Используемые технологии: Spring Boot,  Lombok, Git, REST, Postman, H2, SQL, Maven.

![ ](https://github.com/GretaSovizkaya/java-filmorate/blob/213a8d6dbc386fbced81fc7f7822b7625fbcdeab/ER%20Diagram%20Filmorate%20(2).png)
Примеры запросов для основных операций приложения:
1) Получение всех фильмов
   SELECT * FROM films;
   
2) Получение фильма по ID:
   SELECT * FROM films
   WHERE id = 1;
   
3) Добавление нового пользователя:
   INSERT INTO users (name, email, login, birthday)
   VALUES ('John Doe', 'johndoe@example.com', 'john123', '1990-01-01');

4) Получение списка друзей пользователя:
   SELECT u.*
   FROM users u
   JOIN friends f ON u.id = f.friend_id
   WHERE f.user_id = 1;
