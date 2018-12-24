DELETE
FROM users;
DELETE
FROM roles;
DELETE
FROM restaurants;
DELETE
FROM dishes;
ALTER SEQUENCE global_seq
RESTART WITH 10000;

INSERT INTO USERS (NAME, EMAIL, PASSWORD, REGISTERED, ENABLED)
VALUES ('Sergey', 'admin@yandex.ru', '{noop}pass', '2018-12-05 00:00:00.000000', true),
       ('Ann', 'user@ya.ru', '{noop}pass', '2018-12-06 00:00:00.000000', true),
       ('Ann2', 'user2@ya.ru', '{noop}pass', '2018-12-07 00:00:00.000000', true),
       ('Ann3', 'user3@ya.ru', '{noop}pass', '2018-12-08 00:00:00.000000', true);

INSERT INTO ROLES (USER_ID, ROLE)
VALUES (10000, 'ROLE_ADMIN'),
       (10001, 'ROLE_USER'),
       (10002, 'ROLE_USER'),
       (10003, 'ROLE_USER');

INSERT INTO RESTAURANTS (NAME, CITY, DESCRIPTION)
VALUES ('KFC1', 'Москва', 'Куриные бургеры и картошка'),
       ('KFC2', 'Москва', 'Куриные бургеры и картошка'),
       ('KFC3', 'Москва', 'Куриные бургеры и картошка'),
       ('McDs1', 'Москва', 'Бургеры и картошка'),
       ('McDs2', 'Москва', 'Бургеры и картошка'),
       ('McDs3', 'Москва', 'Бургеры и картошка');

INSERT INTO MENUS (ADDED, RESTAURANT_ID)
VALUES ('2018-12-14', 10007),
       ('2018-12-14', 10008);


INSERT INTO DISHES (NAME, PRICE, MENU_ID)
VALUES ('Картошка1', 70.00, 10010),
       ('Картошка2', 70.00, 10011),
       ('Картошка3', 70.00, 10010),
       ('Бургер1', 70.99, 10010),
       ('Бургер2', 70.49, 10011),
       ('Бургер3', 70.49, 10010);

