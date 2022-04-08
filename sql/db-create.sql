CREATE SCHEMA IF NOT EXISTS restaurant;

USE restaurant;


CREATE TABLE IF NOT EXISTS language
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    short_name VARCHAR(16) NOT NULL UNIQUE
);

INSERT INTO language
VALUES (DEFAULT, 'en');

INSERT INTO language
VALUES (DEFAULT, 'ua');


CREATE TABLE IF NOT EXISTS category
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name_en VARCHAR(128) NOT NULL UNIQUE,
    name_ua VARCHAR(128) NOT NULL UNIQUE
);

INSERT INTO category
VALUES (DEFAULT, 'First courses', 'Перші страви');
INSERT INTO category
VALUES (DEFAULT, 'Second courses', 'Другі страви');
INSERT INTO category
VALUES (DEFAULT, 'Salads', 'Салати');

CREATE TABLE IF NOT EXISTS dish
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    price       INT NOT NULL,
    weight      INT NOT NULL,
    category_id INT NOT NULL,
    is_visible  BOOLEAN DEFAULT 1,

    FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO dish
VALUES (DEFAULT, 119, 350, 1, 1);
INSERT INTO dish
VALUES (DEFAULT, 105, 350, 1, 1);
INSERT INTO dish
VALUES (DEFAULT, 92, 350, 1, 1);
INSERT INTO dish
VALUES (DEFAULT, 102, 350, 1, 1);

INSERT INTO dish
VALUES (DEFAULT, 120, 200, 2, 1);
INSERT INTO dish
VALUES (DEFAULT, 250, 200, 2, 1);
INSERT INTO dish
VALUES (DEFAULT, 123, 250, 2, 1);
INSERT INTO dish
VALUES (DEFAULT, 107, 250, 2, 1);

INSERT INTO dish
VALUES (DEFAULT, 102, 250, 3, 1);
INSERT INTO dish
VALUES (DEFAULT, 150, 250, 3, 1);
INSERT INTO dish
VALUES (DEFAULT, 100, 250, 3, 1);
INSERT INTO dish
VALUES (DEFAULT, 97, 250, 3, 1);


CREATE TABLE IF NOT EXISTS dish_description
(
    dish_id     INT          NOT NULL,
    language_id INT          NOT NULL,
    name        VARCHAR(128) NOT NULL UNIQUE,
    description VARCHAR(512) NOT NULL UNIQUE,

    FOREIGN KEY (dish_id) REFERENCES dish (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (language_id) REFERENCES language (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO dish_description
VALUES (1, 1, 'Bograch', 'Made from meat, sweet peppers, ground paprika, tomatoes, potatoes, carrots and spices');

INSERT INTO dish_description
VALUES (1, 2, 'Бограч',
        'Приготований з м`яса, солодкого перцю, меленої паприки, помідорів, картоплі, моркви та спецій');

INSERT INTO dish_description
VALUES (2, 1, 'Borsch ', 'Made from chopped beets, cabbage with potatoes and various spices');

INSERT INTO dish_description
VALUES (2, 2, 'Борщ', 'Приготований з посічених буряків, капусти з додатком картоплі та різних приправ');

INSERT INTO dish_description
VALUES (3, 1, 'Chicken soup', 'Made from chicken and vegetables with the addition of pasta');

INSERT INTO dish_description
VALUES (3, 2, 'Курячий бульйон', 'Приготований з курки і овочів з додаванням макаронних виробів');

INSERT INTO dish_description
VALUES (4, 1, 'Fish soup ', 'Made from fish, vegetables, potatoes and spices');

INSERT INTO dish_description
VALUES (4, 2, 'Рибна юшка', 'Приготована з риби, овочів, картоплі і спецій');



INSERT INTO dish_description
VALUES (5, 1, 'Cabbage rolls', 'Made from fresh cabbage leaves and meat and rice fillings');

INSERT INTO dish_description
VALUES (5, 2, 'Голубці', 'Приготовані з листя свіжої капусти та начинки з м`яса та рису');

INSERT INTO dish_description
VALUES (6, 1, 'Baked salmon ', 'Salmon baked with vegetables, potatoes and spices ');

INSERT INTO dish_description
VALUES (6, 2, 'Запечений лосось', 'Лосось запечений з овочами, картоплею і спеціями');

INSERT INTO dish_description
VALUES (7, 1, 'Potato pancakes', 'Fried potato pancakes in sauce with cherry tomatoes and sour cream');

INSERT INTO dish_description
VALUES (7, 2, 'Деруни', 'Смажені деруни у соусі з помідорами черрі та сметаною');

INSERT INTO dish_description
VALUES (8, 1, 'Varenyky ', 'Varenyky with potatoes, mushrooms and sour cream ');

INSERT INTO dish_description
VALUES (8, 2, 'Вареники', 'Вареники з картоплею, грибами і сметаною');



INSERT INTO dish_description
VALUES (9, 1, 'Greek salad', 'Salad of cucumbers, tomatoes, olives and feta cheese');

INSERT INTO dish_description
VALUES (9, 2, 'Салат Грецький', 'Салат з огірків, помідорів, маслин та сиру фета');

INSERT INTO dish_description
VALUES (10, 1, 'Caesar salad ', 'Salad of chicken, croutons, parmesan cheese, tomatoes and sauce');

INSERT INTO dish_description
VALUES (10, 2, 'Салат Цезар', 'Салат з курки, грінків, сиру пармезан, помідорів і соусу');

INSERT INTO dish_description
VALUES (11, 1, 'Cobb salad', 'Salad of chicken, bacon, tomatoes, celery, eggs, avocado, cheese and greens');

INSERT INTO dish_description
VALUES (11, 2, 'Салат Кобб', 'Салат з курки, бекону, томатів, селери, яєць, авокадо, сиру і зелені');

INSERT INTO dish_description
VALUES (12, 1, 'Nisuaz salad ', 'Salad of fresh vegetables, boiled eggs, anchovies and olive oil');

INSERT INTO dish_description
VALUES (12, 2, 'Салат Нісуаз', 'Салат з свіжих овочів, варених яєць, анчоусів і оливкової олії');

CREATE TABLE IF NOT EXISTS role
(
    id      INT PRIMARY KEY,
    role_en VARCHAR(32) NOT NULL UNIQUE,
    role_ua VARCHAR(32) NOT NULL UNIQUE
);

INSERT INTO role
VALUES (1, 'admin', 'адмін');
INSERT INTO role
VALUES (2, 'customer', 'покупець');

CREATE TABLE IF NOT EXISTS user
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(128) NOT NULL,
    surname  VARCHAR(128) NOT NULL,
    login    VARCHAR(64)  NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    role_id  INT          NOT NULL,

    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO user
VALUES (DEFAULT, 'test', 'test', 'test',
        '-1838-80-3574-9-2573-8626-114-29-6310-23-1106397-119-128119467163-12025-91-44-1081413-78122-63-123-8-96-31-43-879-120-68-120127-42123205550-614-5295-87-83-11411187-11040-88-1',
        2);
INSERT INTO user
VALUES (DEFAULT, 'admin', 'admin', 'admin',
        '-57-8368-53-831184293-96-9282-7-2484-3-63-32-25-91425619535-13-22-79-4011-10929-441149977-6-5728-4578-6853-47106-73-5-118-112-5631-1058119-42-5783-115-58-99-40-34-112119-20',
        1);
# admin password: admin

CREATE TABLE IF NOT EXISTS status
(
    id      INT PRIMARY KEY,
    name_en VARCHAR(32) NOT NULL UNIQUE,
    name_ua VARCHAR(32) NOT NULL UNIQUE
);

INSERT INTO status
VALUES (1, 'New', 'Новий');
INSERT INTO status
VALUES (2, 'Accepted', 'Прийнято');
INSERT INTO status
VALUES (3, 'Cooking', 'Готується');
INSERT INTO status
VALUES (4, 'Delivering', 'Доставляється');
INSERT INTO status
VALUES (5, 'Done', 'Виконано');

CREATE TABLE IF NOT EXISTS receipt
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT       NOT NULL,
    manager_id  INT,
    status_id   INT       NOT NULL DEFAULT 1,
    total_price INT       NOT NULL DEFAULT 0,
    create_date TIMESTAMP NULL     DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (manager_id) REFERENCES user (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (status_id) REFERENCES status (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO receipt
VALUES (DEFAULT, 1, 2, 1, 329, DEFAULT);

CREATE TABLE IF NOT EXISTS receipt_has_dish
(
    receipt_id INT NOT NULL,
    dish_id    INT NOT NULL,
    count      INT NULL DEFAULT 1,

    UNIQUE KEY (receipt_id, dish_id),
    FOREIGN KEY (receipt_id) REFERENCES receipt (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dish (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO receipt_has_dish
VALUES (1, 1, 1);
INSERT INTO receipt_has_dish
VALUES (1, 2, 2);


CREATE TABLE IF NOT EXISTS user_cart
(
    user_id INT NOT NULL,
    dish_id INT NOT NULL,
    count   INT NOT NULL DEFAULT 1,

    UNIQUE KEY (user_id, dish_id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dish (id) ON DELETE CASCADE ON UPDATE CASCADE
);