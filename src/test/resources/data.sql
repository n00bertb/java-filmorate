-- Отключаем проверку внешних ключей
SET REFERENTIAL_INTEGRITY FALSE;

-- Очистка таблиц перед вставкой данных
DELETE
FROM likes;
DELETE
FROM friends;
DELETE
FROM film_genres;
DELETE
FROM films;
DELETE
FROM users;
DELETE
FROM mpa;
DELETE
FROM genres;


INSERT INTO mpa (mpa_name)
VALUES ('G');
INSERT INTO mpa (mpa_name)
VALUES ('PG');
INSERT INTO mpa (mpa_name)
VALUES ('PG-13');
INSERT INTO mpa (mpa_name)
VALUES ('R');
INSERT INTO mpa (mpa_name)
VALUES ('NC-17');

INSERT INTO genres (genre_name)
VALUES ('Комедия');
INSERT INTO genres (genre_name)
VALUES ('Драма');
INSERT INTO genres (genre_name)
VALUES ('Мультфильм');
INSERT INTO genres (genre_name)
VALUES ('Триллер');
INSERT INTO genres (genre_name)
VALUES ('Документальный');
INSERT INTO genres (genre_name)
VALUES ('Боевик');

INSERT INTO users (name, login, email, birthday)
VALUES ('Антуан', 'antuan', 'antuangrad@mail.ru', '1993-03-12');
INSERT INTO users (name, login, email, birthday)
VALUES ('Сергей', 'serge87', 'serge87@mail.ru', '1987-02-02');
INSERT INTO users (name, login, email, birthday)
VALUES ('Василий', 'vasian88', 'vasian88@mail.ru', '1988-12-01');
INSERT INTO users (name, login, email, birthday)
VALUES ('Павел', 'pasha89', 'pasha89@mail.ru', '1989-05-24');

INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Филосовский камень', 'description1', '2001-11-22', 121, 1);
INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Тайная комната', 'description2', '2002-11-14', 174, 2);
INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Узник азкабана', 'description3', '2003-11-15', 180, 3);
INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Кубок огня', 'description4', '2004-11-06', 167, 4);
INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Орден феникса', 'description5', '2005-11-12', 188, 5);
INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Принц полукровка', 'description6', '2006-11-24', 167, 5);
INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Дары смерти', 'description7', '2010-11-07', 198, 5);

INSERT INTO film_genres (film_id, genre_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 5),
       (7, 2);
INSERT INTO friends (user_id, friend_user_id)
VALUES (1, 2),
       (1, 3),
       (2, 1),
       (2, 4),
       (3, 1),
       (3, 4),
       (4, 1);

-- Включаем проверку внешних ключей снова
SET REFERENTIAL_INTEGRITY TRUE;