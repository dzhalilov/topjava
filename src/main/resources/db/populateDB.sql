DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (dateTime, description, calories, userId)
VALUES ('2022-02-18 08:00', 'Завтрак User', 300, 100000),
       ('2022-02-18 12:30', 'Обед User', 1200, 100000),
       ('2022-02-18 20:00', 'Ужин User', 600, 100000),
       ('2022-02-19 07:10', 'Завтрак User', 250, 100000),
       ('2022-02-19 13:00', 'Обед User', 700, 100000),
       ('2022-02-19 20:00', 'Ужин User', 1000, 100000),
       ('2022-02-19 09:00', 'Завтрак Admin', 50, 100001),
       ('2022-02-19 12:00', 'Обед Admin', 1400, 100001),
       ('2022-02-19 22:00', 'Ужин Admin', 500, 100001),
       ('2022-02-19 10:00', 'Завтрак Guest', 600, 100002),
       ('2022-02-19 14:00', 'Обед Guest', 700, 100002),
       ('2022-02-19 19:00', 'Ужин Guest ', 600, 100002);
