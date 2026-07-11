-- ============================================================
--  FOOD APP DATABASE — Full Schema + Seed Data
--  Designed for database testing (graduation project)
--  Engine: MySQL 8.0+  (InnoDB, utf8mb4)
-- ============================================================
-- DROP DATABASE food_app ;

  CREATE DATABASE food_app
 -- CHARACTER SET utf8mb4
-- COLLATE utf8mb4_unicode_ci;

USE food_app ;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS
  notifications, favorites, user_coupons, coupons,
  deliveries, delivery_drivers,
  reviews, payments,
  order_item_options, order_items, orders,
  cart_item_options, cart_items, carts,
  item_options, item_option_groups,
  menu_items, menu_categories,
  restaurant_hours, restaurant_category_map,
  restaurant_categories, restaurants,
  user_addresses, users;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. USERS
-- ============================================================
CREATE TABLE users (
    user_id       INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100)  NOT NULL,
    email         VARCHAR(150)  NOT NULL UNIQUE,
    phone         VARCHAR(20)   NOT NULL UNIQUE,
    password_hash VARCHAR(255)  NOT NULL,
    gender        ENUM('male','female','other','prefer_not_to_say') DEFAULT 'prefer_not_to_say',
    date_of_birth DATE,
    profile_pic   VARCHAR(255),
    wallet_balance DECIMAL(10,2) NOT NULL DEFAULT 0.00
        CHECK (wallet_balance >= 0),
    status        ENUM('active','suspended','deleted') NOT NULL DEFAULT 'active',
    email_verified TINYINT(1)   NOT NULL DEFAULT 0,
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 2. USER ADDRESSES
-- ============================================================
CREATE TABLE user_addresses (
    address_id    INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       INT UNSIGNED NOT NULL,
    label         ENUM('home','work','other') NOT NULL DEFAULT 'home',
    street        VARCHAR(200) NOT NULL,
    building      VARCHAR(50),
    floor_apt     VARCHAR(50),
    city          VARCHAR(100) NOT NULL,
    district      VARCHAR(100),
    latitude      DECIMAL(10,7),
    longitude     DECIMAL(10,7),
    delivery_notes TEXT,
    is_default    TINYINT(1)   NOT NULL DEFAULT 0,
    CONSTRAINT fk_ua_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE
);

-- ============================================================
-- 3. RESTAURANT CATEGORIES  (e.g. Pizza, Sushi, Burgers …)
-- ============================================================
CREATE TABLE restaurant_categories (
    category_id   INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(80)  NOT NULL UNIQUE,
    icon_url      VARCHAR(255),
    is_active     TINYINT(1)   NOT NULL DEFAULT 1,
    display_order TINYINT UNSIGNED NOT NULL DEFAULT 0
);

-- ============================================================
-- 4. RESTAURANTS
-- ============================================================
CREATE TABLE restaurants (
    restaurant_id    INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(150) NOT NULL,
    description      TEXT,
    logo_url         VARCHAR(255),
    cover_url        VARCHAR(255),
    phone            VARCHAR(20)  NOT NULL,
    email            VARCHAR(150),
    street           VARCHAR(200) NOT NULL,
    city             VARCHAR(100) NOT NULL,
    district         VARCHAR(100),
    latitude         DECIMAL(10,7),
    longitude        DECIMAL(10,7),
    avg_rating       DECIMAL(3,2) NOT NULL DEFAULT 0.00
        CHECK (avg_rating BETWEEN 0 AND 5),
    total_reviews    INT UNSIGNED NOT NULL DEFAULT 0,
    min_order_amount DECIMAL(8,2) NOT NULL DEFAULT 0.00
        CHECK (min_order_amount >= 0),
    delivery_fee     DECIMAL(6,2) NOT NULL DEFAULT 0.00
        CHECK (delivery_fee >= 0),
    avg_delivery_min TINYINT UNSIGNED NOT NULL DEFAULT 30
        CHECK (avg_delivery_min BETWEEN 5 AND 180),
    is_open          TINYINT(1)   NOT NULL DEFAULT 1,
    accepts_cash     TINYINT(1)   NOT NULL DEFAULT 1,
    accepts_card     TINYINT(1)   NOT NULL DEFAULT 1,
    accepts_wallet   TINYINT(1)   NOT NULL DEFAULT 1,
    status           ENUM('active','suspended','closed_permanently') NOT NULL DEFAULT 'active',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 5. RESTAURANT ↔ CATEGORY  (many-to-many)
-- ============================================================
CREATE TABLE restaurant_category_map (
    restaurant_id INT UNSIGNED NOT NULL,
    category_id   INT UNSIGNED NOT NULL,
    PRIMARY KEY (restaurant_id, category_id),
    CONSTRAINT fk_rcm_rest FOREIGN KEY (restaurant_id)
        REFERENCES restaurants(restaurant_id) ON DELETE CASCADE,
    CONSTRAINT fk_rcm_cat  FOREIGN KEY (category_id)
        REFERENCES restaurant_categories(category_id) ON DELETE CASCADE
);

-- ============================================================
-- 6. RESTAURANT HOURS
-- ============================================================
CREATE TABLE restaurant_hours (
    hours_id      INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT UNSIGNED NOT NULL,
    day_of_week   TINYINT UNSIGNED NOT NULL
        CHECK (day_of_week BETWEEN 0 AND 6),  -- 0=Sun … 6=Sat
    opens_at      TIME NOT NULL,
    closes_at     TIME NOT NULL,
    is_closed     TINYINT(1) NOT NULL DEFAULT 0,
    UNIQUE KEY uq_rest_day (restaurant_id, day_of_week),
    CONSTRAINT fk_rh_rest FOREIGN KEY (restaurant_id)
        REFERENCES restaurants(restaurant_id) ON DELETE CASCADE,
    CONSTRAINT chk_hours CHECK (closes_at > opens_at OR is_closed = 1)
);

-- ============================================================
-- 7. MENU CATEGORIES  (sections inside a restaurant's menu)
-- ============================================================
CREATE TABLE menu_categories (
    menu_cat_id   INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT UNSIGNED NOT NULL,
    name          VARCHAR(100) NOT NULL,
    description   VARCHAR(255),
    display_order TINYINT UNSIGNED NOT NULL DEFAULT 0,
    is_active     TINYINT(1)   NOT NULL DEFAULT 1,
    CONSTRAINT fk_mc_rest FOREIGN KEY (restaurant_id)
        REFERENCES restaurants(restaurant_id) ON DELETE CASCADE
);

-- ============================================================
-- 8. MENU ITEMS
-- ============================================================
CREATE TABLE menu_items (
    item_id       INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    menu_cat_id   INT UNSIGNED NOT NULL,
    restaurant_id INT UNSIGNED NOT NULL,
    name          VARCHAR(150) NOT NULL,
    description   TEXT,
    image_url     VARCHAR(255),
    base_price    DECIMAL(8,2) NOT NULL CHECK (base_price >= 0),
    calories      SMALLINT UNSIGNED,
    is_vegetarian TINYINT(1)   NOT NULL DEFAULT 0,
    is_vegan      TINYINT(1)   NOT NULL DEFAULT 0,
    is_spicy      TINYINT(1)   NOT NULL DEFAULT 0,
    is_available  TINYINT(1)   NOT NULL DEFAULT 1,
    total_ordered INT UNSIGNED NOT NULL DEFAULT 0,
    CONSTRAINT fk_mi_cat  FOREIGN KEY (menu_cat_id)
        REFERENCES menu_categories(menu_cat_id) ON DELETE CASCADE,
    CONSTRAINT fk_mi_rest FOREIGN KEY (restaurant_id)
        REFERENCES restaurants(restaurant_id) ON DELETE CASCADE
);

-- ============================================================
-- 9. ITEM OPTION GROUPS  (e.g. "Size", "Extras", "Sauce")
-- ============================================================
CREATE TABLE item_option_groups (
    group_id      INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    item_id       INT UNSIGNED NOT NULL,
    name          VARCHAR(100) NOT NULL,
    is_required   TINYINT(1)   NOT NULL DEFAULT 0,
    min_select    TINYINT UNSIGNED NOT NULL DEFAULT 0,
    max_select    TINYINT UNSIGNED NOT NULL DEFAULT 1,
    CONSTRAINT fk_iog_item FOREIGN KEY (item_id)
        REFERENCES menu_items(item_id) ON DELETE CASCADE,
    CONSTRAINT chk_select CHECK (min_select <= max_select)
);

-- ============================================================
-- 10. ITEM OPTIONS  (individual choices within a group)
-- ============================================================
CREATE TABLE item_options (
    option_id     INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    group_id      INT UNSIGNED NOT NULL,
    name          VARCHAR(100) NOT NULL,
    extra_price   DECIMAL(6,2) NOT NULL DEFAULT 0.00
        CHECK (extra_price >= 0),
    is_available  TINYINT(1)   NOT NULL DEFAULT 1,
    CONSTRAINT fk_io_group FOREIGN KEY (group_id)
        REFERENCES item_option_groups(group_id) ON DELETE CASCADE
);

-- ============================================================
-- 11. DELIVERY DRIVERS
-- ============================================================
CREATE TABLE delivery_drivers (
    driver_id     INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100) NOT NULL,
    phone         VARCHAR(20)  NOT NULL UNIQUE,
    email         VARCHAR(150) UNIQUE,
    vehicle_type  ENUM('motorcycle','bicycle','car') NOT NULL DEFAULT 'motorcycle',
    vehicle_plate VARCHAR(20),
    license_no    VARCHAR(50),
    avg_rating    DECIMAL(3,2) NOT NULL DEFAULT 0.00
        CHECK (avg_rating BETWEEN 0 AND 5),
    total_deliveries INT UNSIGNED NOT NULL DEFAULT 0,
    status        ENUM('available','on_delivery','offline','suspended') NOT NULL DEFAULT 'offline',
    latitude      DECIMAL(10,7),
    longitude     DECIMAL(10,7),
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 12. COUPONS
-- ============================================================
CREATE TABLE coupons (
    coupon_id       INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    code            VARCHAR(30)  NOT NULL UNIQUE,
    description     VARCHAR(255),
    discount_type   ENUM('percentage','fixed') NOT NULL DEFAULT 'fixed',
    discount_value  DECIMAL(8,2) NOT NULL CHECK (discount_value > 0),
    min_order_amount DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    max_discount    DECIMAL(8,2),                  -- cap for percentage coupons
    usage_limit     INT UNSIGNED,                  -- NULL = unlimited
    used_count      INT UNSIGNED NOT NULL DEFAULT 0,
    per_user_limit  TINYINT UNSIGNED NOT NULL DEFAULT 1,
    starts_at       DATETIME     NOT NULL,
    expires_at      DATETIME     NOT NULL,
    is_active       TINYINT(1)   NOT NULL DEFAULT 1,
    restaurant_id   INT UNSIGNED,                  -- NULL = platform-wide
    CONSTRAINT fk_cp_rest FOREIGN KEY (restaurant_id)
        REFERENCES restaurants(restaurant_id) ON DELETE SET NULL,
    CONSTRAINT chk_coupon_dates CHECK (expires_at > starts_at)
);

-- ============================================================
-- 13. ORDERS
-- ============================================================
CREATE TABLE orders (
    order_id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id          INT UNSIGNED NOT NULL,
    restaurant_id    INT UNSIGNED NOT NULL,
    address_id       INT UNSIGNED NOT NULL,
    coupon_id        INT UNSIGNED,
    status           ENUM(
                       'pending','confirmed','preparing',
                       'ready_for_pickup','out_for_delivery',
                       'delivered','cancelled','refunded'
                     ) NOT NULL DEFAULT 'pending',
    payment_method   ENUM('cash','card','wallet') NOT NULL DEFAULT 'cash',
    subtotal         DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    delivery_fee     DECIMAL(6,2)  NOT NULL DEFAULT 0.00 CHECK (delivery_fee >= 0),
    discount_amount  DECIMAL(8,2)  NOT NULL DEFAULT 0.00 CHECK (discount_amount >= 0),
    total_amount     DECIMAL(10,2) NOT NULL, -- Removed the inline check here
    special_instructions TEXT,
    estimated_delivery  DATETIME,
    placed_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmed_at     DATETIME,
    delivered_at     DATETIME,
    cancelled_at     DATETIME,
    cancel_reason    VARCHAR(255),
    CONSTRAINT fk_ord_user  FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_ord_rest  FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id),
    CONSTRAINT fk_ord_addr  FOREIGN KEY (address_id) REFERENCES user_addresses(address_id),
    CONSTRAINT fk_ord_coup  FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id) ON DELETE SET NULL,
    
    -- TABLE LEVEL CHECK CONSTRAINT (This fixes Error 3813)
    CONSTRAINT chk_total_calculation CHECK (total_amount = subtotal + delivery_fee - discount_amount)
);
-- ============================================================
-- 14. ORDER ITEMS
-- ============================================================
CREATE TABLE order_items (
    order_item_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id      INT UNSIGNED NOT NULL,
    item_id       INT UNSIGNED NOT NULL,
    quantity      TINYINT UNSIGNED NOT NULL DEFAULT 1
        CHECK (quantity BETWEEN 1 AND 50),
    unit_price    DECIMAL(8,2) NOT NULL CHECK (unit_price >= 0),
    item_note     VARCHAR(255),
    CONSTRAINT fk_oi_order FOREIGN KEY (order_id)
        REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_oi_item  FOREIGN KEY (item_id)
        REFERENCES menu_items(item_id)
);

-- ============================================================
-- 15. ORDER ITEM OPTIONS  (snapshot of chosen options)
-- ============================================================
CREATE TABLE order_item_options (
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_item_id INT UNSIGNED NOT NULL,
    option_id     INT UNSIGNED NOT NULL,
    option_name   VARCHAR(100) NOT NULL,   -- snapshot in case option changes
    extra_price   DECIMAL(6,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_oio_oi  FOREIGN KEY (order_item_id)
        REFERENCES order_items(order_item_id) ON DELETE CASCADE,
    CONSTRAINT fk_oio_opt FOREIGN KEY (option_id)
        REFERENCES item_options(option_id)
);

-- ============================================================
-- 16. PAYMENTS
-- ============================================================
CREATE TABLE payments (
    payment_id      INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id        INT UNSIGNED NOT NULL UNIQUE,
    method          ENUM('cash','card','wallet') NOT NULL,
    status          ENUM('pending','completed','failed','refunded') NOT NULL DEFAULT 'pending',
    amount          DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
    transaction_ref VARCHAR(100),
    gateway         VARCHAR(50),            -- 'stripe', 'paymob', etc.
    paid_at         DATETIME,
    refunded_at     DATETIME,
    refund_reason   VARCHAR(255),
    CONSTRAINT fk_pay_order FOREIGN KEY (order_id)
        REFERENCES orders(order_id)
);

-- ============================================================
-- 17. DELIVERIES
-- ============================================================
CREATE TABLE deliveries (
    delivery_id     INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id        INT UNSIGNED NOT NULL UNIQUE,
    driver_id       INT UNSIGNED,
    status          ENUM(
                      'assigned','picked_up',
                      'on_the_way','delivered','failed'
                    ) NOT NULL DEFAULT 'assigned',
    assigned_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    picked_up_at    DATETIME,
    delivered_at    DATETIME,
    driver_rating   TINYINT UNSIGNED
        CHECK (driver_rating BETWEEN 1 AND 5),
    driver_notes    VARCHAR(255),
    CONSTRAINT fk_del_order  FOREIGN KEY (order_id)
        REFERENCES orders(order_id),
    CONSTRAINT fk_del_driver FOREIGN KEY (driver_id)
        REFERENCES delivery_drivers(driver_id) ON DELETE SET NULL
);

-- ============================================================
-- 18. REVIEWS
-- ============================================================
CREATE TABLE reviews (
    review_id     INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id      INT UNSIGNED NOT NULL UNIQUE,   -- one review per order
    user_id       INT UNSIGNED NOT NULL,
    restaurant_id INT UNSIGNED NOT NULL,
    food_rating   TINYINT UNSIGNED NOT NULL
        CHECK (food_rating BETWEEN 1 AND 5),
    delivery_rating TINYINT UNSIGNED
        CHECK (delivery_rating BETWEEN 1 AND 5),
    overall_rating TINYINT UNSIGNED NOT NULL
        CHECK (overall_rating BETWEEN 1 AND 5),
    comment       TEXT,
    is_visible    TINYINT(1)   NOT NULL DEFAULT 1,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rev_order FOREIGN KEY (order_id)
        REFERENCES orders(order_id),
    CONSTRAINT fk_rev_user  FOREIGN KEY (user_id)
        REFERENCES users(user_id),
    CONSTRAINT fk_rev_rest  FOREIGN KEY (restaurant_id)
        REFERENCES restaurants(restaurant_id)
);

-- ============================================================
-- 19. CARTS
-- ============================================================
CREATE TABLE carts (
    cart_id       INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       INT UNSIGNED NOT NULL UNIQUE,
    restaurant_id INT UNSIGNED,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_rest FOREIGN KEY (restaurant_id)
        REFERENCES restaurants(restaurant_id) ON DELETE SET NULL
);

CREATE TABLE cart_items (
    cart_item_id  INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    cart_id       INT UNSIGNED NOT NULL,
    item_id       INT UNSIGNED NOT NULL,
    quantity      TINYINT UNSIGNED NOT NULL DEFAULT 1
        CHECK (quantity BETWEEN 1 AND 50),
    item_note     VARCHAR(255),
    CONSTRAINT fk_ci_cart FOREIGN KEY (cart_id)
        REFERENCES carts(cart_id) ON DELETE CASCADE,
    CONSTRAINT fk_ci_item FOREIGN KEY (item_id)
        REFERENCES menu_items(item_id)
);

CREATE TABLE cart_item_options (
    id           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    cart_item_id INT UNSIGNED NOT NULL,
    option_id    INT UNSIGNED NOT NULL,
    CONSTRAINT fk_cio_ci  FOREIGN KEY (cart_item_id)
        REFERENCES cart_items(cart_item_id) ON DELETE CASCADE,
    CONSTRAINT fk_cio_opt FOREIGN KEY (option_id)
        REFERENCES item_options(option_id)
);

-- ============================================================
-- 20. USER COUPONS  (tracks which user used which coupon)
-- ============================================================
CREATE TABLE user_coupons (
    id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id    INT UNSIGNED NOT NULL,
    coupon_id  INT UNSIGNED NOT NULL,
    order_id   INT UNSIGNED NOT NULL,
    used_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_order_coupon (user_id, order_id),
    CONSTRAINT fk_uc_user   FOREIGN KEY (user_id)   REFERENCES users(user_id),
    CONSTRAINT fk_uc_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id),
    CONSTRAINT fk_uc_order  FOREIGN KEY (order_id)  REFERENCES orders(order_id)
);

-- ============================================================
-- 21. FAVORITES
-- ============================================================
CREATE TABLE favorites (
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       INT UNSIGNED NOT NULL,
    restaurant_id INT UNSIGNED NOT NULL,
    added_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_fav (user_id, restaurant_id),
    CONSTRAINT fk_fav_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_fav_rest FOREIGN KEY (restaurant_id)
        REFERENCES restaurants(restaurant_id) ON DELETE CASCADE
);

-- ============================================================
-- 22. NOTIFICATIONS
-- ============================================================
CREATE TABLE notifications (
    notif_id      INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       INT UNSIGNED NOT NULL,
    type          ENUM('order_update','promo','system','delivery') NOT NULL,
    title         VARCHAR(150) NOT NULL,
    body          TEXT         NOT NULL,
    is_read       TINYINT(1)   NOT NULL DEFAULT 0,
    order_id      INT UNSIGNED,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user  FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_notif_order FOREIGN KEY (order_id)
        REFERENCES orders(order_id) ON DELETE SET NULL
);


-- ============================================================
-- ============================================================
--  D M L  —  S E E D   D A T A
-- ============================================================
-- ============================================================

-- ============================================================
-- USERS  (12 users)
-- ============================================================
INSERT INTO users (full_name, email, phone, password_hash, gender, date_of_birth, wallet_balance, status, email_verified) VALUES
('Ahmed Hassan',      'ahmed.hassan@email.com',    '+201001234567', '$2b$12$abc1hashAhmed',   'male',   '1995-03-14', 150.00, 'active',    1),
('Sara Mohamed',      'sara.m@email.com',          '+201112345678', '$2b$12$abc2hashSara',    'female', '1998-07-22', 50.00,  'active',    1),
('Omar Khaled',       'omar.k@email.com',          '+201223456789', '$2b$12$abc3hashOmar',    'male',   '1992-11-05', 0.00,   'active',    1),
('Nour Ibrahim',      'nour.ibrahim@email.com',    '+201334567890', '$2b$12$abc4hashNour',    'female', '2000-01-30', 200.00, 'active',    1),
('Youssef Ali',       'youssef.ali@email.com',     '+201445678901', '$2b$12$abc5hashYoussef', 'male',   '1997-06-18', 75.50,  'active',    1),
('Mona Salah',        'mona.salah@email.com',      '+201556789012', '$2b$12$abc6hashMona',    'female', '1994-09-25', 30.00,  'active',    1),
('Karim Tawfik',      'karim.t@email.com',         '+201667890123', '$2b$12$abc7hashKarim',   'male',   '1999-04-12', 0.00,   'active',    0),
('Layla Nasser',      'layla.nasser@email.com',    '+201778901234', '$2b$12$abc8hashLayla',   'female', '1996-12-08', 120.00, 'active',    1),
('Hassan Mostafa',    'hassan.m@email.com',         '+201889012345', '$2b$12$abc9hashHassan',  'male',   '1990-08-19', 0.00,   'suspended', 1),
('Dina Farouk',       'dina.farouk@email.com',     '+201990123456', '$2b$12$abcAhashDina',    'female', '2001-02-14', 10.00,  'active',    1),
('Mahmoud Saber',     'mahmoud.s@email.com',       '+201501234567', '$2b$12$abcBhashMahmoud', 'male',   '1993-07-07', 55.00,  'active',    1),
('Rania Adel',        'rania.adel@email.com',      '+201612345678', '$2b$12$abcChashRania',   'female', '1997-10-21', 0.00,   'active',    1);

-- ============================================================
-- USER ADDRESSES
-- ============================================================
INSERT INTO user_addresses (user_id, label, street, building, floor_apt, city, district, latitude, longitude, delivery_notes, is_default) VALUES
(1,  'home',  '15 Tahrir Square',      'Bldg 3',  'Apt 5',   'Cairo',       'Downtown',       30.0444,  31.2357, 'Ring bell twice',          1),
(1,  'work',  '22 Corniche El Nil',    'Tower A', 'Floor 8', 'Cairo',       'Maadi',          29.9626,  31.2497, 'Call on arrival',          0),
(2,  'home',  '7 El Nasr Street',      'Villa 12', NULL,     'Giza',        'Dokki',          30.0393,  31.2116, 'Leave at door',            1),
(3,  'home',  '88 El Haram Street',    'Bldg 7',  'Apt 11',  'Giza',        'Haram',          29.9886,  31.1718, NULL,                       1),
(4,  'home',  '3 Makram Ebeid St',     'Bldg 1',  'Apt 3',   'Cairo',       'Nasr City',      30.0679,  31.3392, 'Gate code: 1234',          1),
(4,  'work',  '55 El Merghany St',     'Office Park', 'F2',  'Cairo',       'Heliopolis',     30.0870,  31.3384, NULL,                       0),
(5,  'home',  '12 Ahmed Orabi St',     'Bldg 4',  'Apt 2',   'Alexandria',  'Sidi Gaber',     31.2001,  29.9187, 'Intercom broken, call',    1),
(6,  'home',  '9 El Nozha Street',     'Villa 3',  NULL,     'Cairo',       'Heliopolis',     30.1022,  31.3412, NULL,                       1),
(7,  'home',  '44 Shooting Club Rd',   'Bldg 2',  'Apt 7',   'Giza',        'Dokki',          30.0408,  31.2109, NULL,                       1),
(8,  'home',  '17 Street 9',           'Bldg 5',  'Apt 1A',  'Cairo',       'Maadi',          29.9597,  31.2560, 'Compound gate, buzz 101',  1),
(9,  'home',  '60 Ramsis Street',      'Bldg 10', 'Apt 14',  'Cairo',       'Abbassia',       30.0653,  31.2910, NULL,                       1),
(10, 'home',  '5 Omar Ibn El Khattab', 'Bldg 2',  'Apt 9',   'Giza',        '6th October',    29.9285,  30.9188, 'Delivery from back gate',  1),
(11, 'home',  '30 Mostafa Kamel St',   'Bldg 6',  'Apt 3',   'Alexandria',  'Smouha',         31.2114,  29.9508, NULL,                       1),
(12, 'home',  '1 El Geish Street',     'Villa 8',  NULL,     'Cairo',       'Nasr City',      30.0569,  31.3437, NULL,                       1);

-- ============================================================
-- RESTAURANT CATEGORIES
-- ============================================================
INSERT INTO restaurant_categories (name, icon_url, is_active, display_order) VALUES
('Burgers',       '/icons/burger.svg',    1, 1),
('Pizza',         '/icons/pizza.svg',     1, 2),
('Sushi',         '/icons/sushi.svg',     1, 3),
('Fried Chicken', '/icons/chicken.svg',   1, 4),
('Sandwiches',    '/icons/sandwich.svg',  1, 5),
('Seafood',       '/icons/seafood.svg',   1, 6),
('Healthy',       '/icons/salad.svg',     1, 7),
('Desserts',      '/icons/dessert.svg',   1, 8),
('Egyptian',      '/icons/egyptian.svg',  1, 9),
('Asian',         '/icons/asian.svg',     1, 10);

-- ============================================================
-- RESTAURANTS  (15 restaurants)
-- ============================================================
INSERT INTO restaurants (name, description, phone, email, street, city, district, latitude, longitude, avg_rating, total_reviews, min_order_amount, delivery_fee, avg_delivery_min, is_open, accepts_cash, accepts_card, accepts_wallet, status) VALUES
('Smash & Grill',         'Gourmet smash burgers with fresh ingredients',                '+20222001001', 'info@smashgrill.com',       '12 Lebanon Sq',        'Cairo',      'Mohandeseen', 30.0595, 31.2013, 4.60, 312,  80.00, 15.00, 25, 1, 1, 1, 1, 'active'),
('Pizza Roma',            'Authentic Neapolitan pizza baked in wood-fired ovens',        '+20222002002', 'info@pizzaroma.com',         '5 Hassan Sabry St',    'Cairo',      'Zamalek',     30.0625, 31.2220, 4.40, 198,  120.00, 20.00, 35, 1, 1, 1, 1, 'active'),
('Tokyo Express',         'Fast & fresh sushi, ramen, and Japanese street food',         '+20222003003', 'orders@tokyoexpress.com',    '88 Tahrir Square',     'Cairo',      'Downtown',    30.0444, 31.2357, 4.70, 445,  150.00, 25.00, 40, 1, 1, 1, 1, 'active'),
('Crispy Zone',           'Best fried chicken in town — crispy outside, juicy inside',   '+20222004004', 'info@crispyzone.com',        '33 Makram Ebeid St',   'Cairo',      'Nasr City',   30.0661, 31.3370, 4.20, 567,  60.00,  12.00, 20, 1, 1, 1, 1, 'active'),
('The Sandwich Lab',      'Creative sandwiches and wraps made to order',                 '+20222005005', 'hello@sandwichlab.com',      '7 Shooting Club Rd',   'Giza',       'Dokki',       30.0386, 31.2098, 4.30, 220,  50.00,  10.00, 20, 1, 1, 1, 1, 'active'),
('Ocean Plate',           'Fresh seafood daily — grills, stews, and platters',           '+20222006006', 'orders@oceanplate.com',      '14 Corniche El Nil',   'Cairo',      'Maadi',       29.9618, 31.2502, 4.50, 182,  200.00, 30.00, 45, 1, 0, 1, 1, 'active'),
('Green Bowl',            'Clean eating: salads, grain bowls, cold-pressed juices',      '+20222007007', 'info@greenbowl.com',         '6 El Nasr Road',       'Cairo',      'Nasr City',   30.0730, 31.3403, 4.80, 134,  100.00, 18.00, 30, 1, 1, 1, 1, 'active'),
('Sweet Corner',          'Desserts, waffles, crepes, and specialty coffee',             '+20222008008', 'sweets@sweetcorner.com',     '2 El Haram St',        'Giza',       'Haram',       29.9880, 31.1720, 4.10, 289,  40.00,  8.00,  15, 1, 1, 1, 1, 'active'),
('Koshary Palace',        'Egypt\'s national dish — 5 varieties, big portions',          '+20222009009', 'info@kosharypalace.com',     '100 Ramsis Street',    'Cairo',      'Abbassia',    30.0650, 31.2900, 4.50, 892,  30.00,  5.00,  20, 1, 1, 0, 0, 'active'),
('Burger Factory',        'Build-your-own burgers — 20+ toppings, daily specials',       '+20222010010', 'info@burgerfactory.com',     '22 El Merghany St',    'Cairo',      'Heliopolis',  30.0875, 31.3391, 4.30, 401,  90.00,  15.00, 30, 1, 1, 1, 1, 'active'),
('Wok This Way',          'Pan-Asian stir-fries, dim sum, and bubble tea',               '+20222011011', 'orders@wokthisway.com',      '15 Ahmed Orabi St',    'Alexandria', 'Sidi Gaber',  31.2005, 29.9191, 4.40, 167,  130.00, 22.00, 35, 1, 1, 1, 1, 'active'),
('Grill House 101',       'American BBQ ribs, brisket, pulled pork',                    '+20222012012', NULL,                          '9 Mostafa Kamel St',   'Alexandria', 'Smouha',      31.2110, 29.9512, 4.60, 203,  180.00, 25.00, 40, 1, 1, 1, 0, 'active'),
('Crepe & More',          'Sweet and savory crepes, plus smoothies',                     '+20222013013', 'hello@crepemore.com',         '3 Geish Street',       'Cairo',      'Nasr City',   30.0565, 31.3441, 4.00, 154,  40.00,  8.00,  15, 1, 1, 1, 1, 'active'),
('Shawarma Station',      'Authentic shawarma — chicken and meat, 24/7',                 '+20222014014', NULL,                          '45 Omar Ibn El Khattab','Giza',      '6th October', 29.9288, 30.9192, 4.60, 763,  40.00,  7.00,  15, 1, 1, 0, 0, 'active'),
('Sushi & Sake',          'Premium sushi bar with omakase and à la carte options',       '+20222015015', 'reservations@sushiandSake.com','20 Hassan Sabry St',  'Cairo',      'Zamalek',     30.0630, 31.2218, 4.90, 89,   200.00, 30.00, 50, 1, 0, 1, 1, 'active');

-- ============================================================
-- RESTAURANT ↔ CATEGORY MAP
-- ============================================================
INSERT INTO restaurant_category_map VALUES
(1,  1),(1,  5),           -- Smash & Grill: Burgers, Sandwiches
(2,  2),                   -- Pizza Roma: Pizza
(3,  3),(3,  10),          -- Tokyo Express: Sushi, Asian
(4,  4),                   -- Crispy Zone: Fried Chicken
(5,  5),(5,  1),           -- Sandwich Lab: Sandwiches, Burgers
(6,  6),                   -- Ocean Plate: Seafood
(7,  7),                   -- Green Bowl: Healthy
(8,  8),                   -- Sweet Corner: Desserts
(9,  9),                   -- Koshary Palace: Egyptian
(10, 1),                   -- Burger Factory: Burgers
(11, 10),(11, 3),          -- Wok This Way: Asian, Sushi
(12, 1),(12, 5),           -- Grill House: Burgers, Sandwiches (BBQ)
(13, 8),                   -- Crepe & More: Desserts
(14, 5),(14, 9),           -- Shawarma Station: Sandwiches, Egyptian
(15, 3);                   -- Sushi & Sake: Sushi

-- ============================================================
-- RESTAURANT HOURS  (all 7 days for first 5 restaurants, 
--                    abbreviated for rest for variety)
-- ============================================================
INSERT INTO restaurant_hours (restaurant_id, day_of_week, opens_at, closes_at, is_closed) VALUES
-- Smash & Grill (Fixed 00:00 to 23:59:59)
(1,0,'11:00','23:30',0),(1,1,'11:00','23:30',0),(1,2,'11:00','23:30',0),
(1,3,'11:00','23:30',0),(1,4,'11:00','23:30',0),(1,5,'11:00','23:59:59',0),(1,6,'12:00','23:59:59',0),

-- Pizza Roma (Looks good)
(2,0,'12:00','23:00',0),(2,1,'23:59:59','23:59:59',1),(2,2,'12:00','23:00',0),
(2,3,'12:00','23:00',0),(2,4,'12:00','23:00',0),(2,5,'12:00','23:59:59',0),(2,6,'12:00','23:59:59',0),

-- Tokyo Express (Looks good)
(3,0,'12:00','22:30',0),(3,1,'12:00','22:30',0),(3,2,'12:00','22:30',0),
(3,3,'12:00','22:30',0),(3,4,'12:00','22:30',0),(3,5,'12:00','23:30',0),(3,6,'13:00','23:30',0),

-- Crispy Zone (Fixed: For same-day validation, set to 23:59:59)
(4,0,'10:00','23:59:59',0),(4,1,'10:00','23:59:59',0),(4,2,'10:00','23:59:59',0),
(4,3,'10:00','23:59:59',0),(4,4,'10:00','23:59:59',0),(4,5,'10:00','23:59:59',0),(4,6,'10:00','23:59:59',0),

-- Sandwich Lab (Looks good)
(5,0,'09:00','22:00',0),(5,1,'09:00','22:00',0),(5,2,'09:00','22:00',0),
(5,3,'09:00','22:00',0),(5,4,'09:00','22:00',0),(5,5,'09:00','23:00',0),(5,6,'10:00','23:00',0),

-- Ocean Plate (Looks good)
(6,0,'12:00','23:00',0),(6,1,'12:00','23:00',0),(6,2,'12:00','23:00',0),
(6,3,'12:00','23:00',0),(6,4,'12:00','23:00',0),(6,5,'12:00','23:59:59',0),(6,6,'13:00','23:59:59',0),

-- Koshary Palace (Looks good)
(9,0,'00:01','23:59',0),(9,1,'00:01','23:59',0),(9,2,'00:01','23:59',0),
(9,3,'00:01','23:59',0),(9,4,'00:01','23:59',0),(9,5,'00:01','23:59',0),(9,6,'00:01','23:59',0),

-- Shawarma Station (Looks good)
(14,0,'00:01','23:59',0),(14,1,'00:01','23:59',0),(14,2,'00:01','23:59',0),
(14,3,'00:01','23:59',0),(14,4,'00:01','23:59',0),(14,5,'00:01','23:59',0),(14,6,'00:01','23:59',0);

-- ============================================================
-- MENU CATEGORIES
-- ============================================================
INSERT INTO menu_categories (restaurant_id, name, display_order, is_active) VALUES
-- Smash & Grill (restaurant 1)
(1, 'Smash Burgers',    1, 1),
(1, 'Sides & Fries',    2, 1),
(1, 'Drinks',           3, 1),
-- Pizza Roma (restaurant 2)
(2, 'Classic Pizzas',   1, 1),
(2, 'Specialty Pizzas', 2, 1),
(2, 'Pasta',            3, 1),
(2, 'Drinks',           4, 1),
-- Tokyo Express (restaurant 3)
(3, 'Sushi Rolls',      1, 1),
(3, 'Nigiri & Sashimi', 2, 1),
(3, 'Ramen',            3, 1),
(3, 'Sides & Starters', 4, 1),
-- Crispy Zone (restaurant 4)
(4, 'Chicken Meals',    1, 1),
(4, 'Chicken Pieces',   2, 1),
(4, 'Sides',            3, 1),
(4, 'Drinks',           4, 1),
-- Sandwich Lab (restaurant 5)
(5, 'Hot Sandwiches',   1, 1),
(5, 'Cold Sandwiches',  2, 1),
(5, 'Wraps',            3, 1),
-- Ocean Plate (restaurant 6)
(6, 'Grilled Fish',     1, 1),
(6, 'Seafood Platters', 2, 1),
(6, 'Soups & Starters', 3, 1),
-- Green Bowl (restaurant 7)
(7, 'Salads',           1, 1),
(7, 'Grain Bowls',      2, 1),
(7, 'Cold-Pressed Juices', 3, 1),
-- Sweet Corner (restaurant 8)
(8, 'Waffles',          1, 1),
(8, 'Crepes',           2, 1),
(8, 'Hot Drinks',       3, 1),
-- Koshary Palace (restaurant 9)
(9, 'Koshary',          1, 1),
(9, 'Extras',           2, 1),
-- Burger Factory (restaurant 10)
(10,'Signature Burgers', 1, 1),
(10,'Build Your Own',    2, 1),
(10,'Sides',             3, 1),
-- Shawarma Station (restaurant 14)
(14,'Shawarma Sandwiches', 1, 1),
(14,'Plates',              2, 1),
(14,'Sides',               3, 1);

-- ============================================================
-- MENU ITEMS
-- ============================================================
INSERT INTO menu_items (menu_cat_id, restaurant_id, name, description, base_price, calories, is_vegetarian, is_vegan, is_spicy, is_available) VALUES
-- Smash & Grill — Smash Burgers (cat 1)
(1, 1, 'Classic Smash',     'Double smash patty, American cheese, pickles, special sauce',        89.00,  650, 0,0,0, 1),
(1, 1, 'Bacon Smash',       'Double patty, crispy bacon, cheddar, caramelised onions',           109.00,  780, 0,0,0, 1),
(1, 1, 'Mushroom Swiss',    'Double patty, sautéed mushrooms, Swiss cheese, truffle aioli',       99.00,  720, 0,0,0, 1),
(1, 1, 'Spicy Beast',       'Double patty, pepper jack, jalapeños, sriracha mayo',                95.00,  700, 0,0,1, 1),
(1, 1, 'Veggie Smash',      'Plant-based patty, avocado, roasted red pepper, vegan mayo',         85.00,  540, 1,1,0, 1),
-- Smash & Grill — Sides (cat 2)
(2, 1, 'Smash Fries',       'Double-fried crinkle-cut fries',                                     35.00,  320, 1,1,0, 1),
(2, 1, 'Loaded Fries',      'Fries, cheese sauce, jalapeños, crispy onions',                      55.00,  520, 1,0,1, 1),
(2, 1, 'Onion Rings',       'Beer-battered, extra crispy',                                        40.00,  380, 1,0,0, 1),
(2, 1, 'Mac Bites',         'Mac & cheese bites — 6 pieces',                                      45.00,  410, 1,0,0, 1),
-- Smash & Grill — Drinks (cat 3)
(3, 1, 'Coke',              '330ml can',                                                          20.00,  139, 1,1,0, 1),
(3, 1, 'Lemonade',          'Fresh squeezed with mint',                                           25.00,  80,  1,1,0, 1),
(3, 1, 'Milkshake',         'Vanilla / Chocolate / Strawberry — 400ml',                           50.00,  480, 1,0,0, 1),
-- Pizza Roma — Classic Pizzas (cat 4)
(4, 2, 'Margherita',        'San Marzano tomato, fresh mozzarella, basil',                        95.00,  750, 1,0,0, 1),
(4, 2, 'Pepperoni',         'Classic pepperoni with mozzarella',                                 115.00,  870, 0,0,0, 1),
(4, 2, 'BBQ Chicken',       'Grilled chicken, BBQ sauce, red onion, mozzarella',                 120.00,  880, 0,0,0, 1),
(4, 2, 'Four Cheese',       'Mozzarella, gorgonzola, ricotta, parmesan',                         130.00,  950, 1,0,0, 1),
-- Pizza Roma — Specialty Pizzas (cat 5)
(5, 2, 'Truffle Prosciutto','Prosciutto, truffle cream, rocket, parmesan',                       160.00,  900, 0,0,0, 1),
(5, 2, 'Seafood Fiesta',    'Mixed seafood, garlic butter, cherry tomatoes',                     155.00,  860, 0,0,0, 1),
(5, 2, 'Veggie Supreme',    'Grilled veg medley, pesto base, goat cheese',                       125.00,  780, 1,0,0, 1),
-- Pizza Roma — Pasta (cat 6)
(6, 2, 'Spaghetti Bolognese','Slow-cooked beef ragù, parmesan',                                  110.00,  820, 0,0,0, 1),
(6, 2, 'Penne Arrabiata',   'Spicy tomato sauce, garlic, chilli',                                 90.00,  680, 1,1,1, 1),
(6, 2, 'Fettuccine Alfredo','Cream sauce, parmesan, black pepper',                               105.00,  890, 1,0,0, 1),
-- Tokyo Express — Sushi Rolls (cat 8)
(8, 3, 'California Roll',   'Crab, avocado, cucumber — 8 pcs',                                   95.00,  380, 0,0,0, 1),
(8, 3, 'Spicy Tuna Roll',   'Spicy tuna, cucumber, sriracha — 8 pcs',                            110.00,  340, 0,0,1, 1),
(8, 3, 'Dragon Roll',       'Shrimp tempura, avocado on top — 8 pcs',                            125.00,  420, 0,0,0, 1),
(8, 3, 'Rainbow Roll',      'California base topped with assorted sashimi — 8 pcs',              140.00,  460, 0,0,0, 1),
(8, 3, 'Veggie Crunch Roll','Avocado, cucumber, carrot, crispy rice — 8 pcs',                     85.00,  300, 1,1,0, 1),
-- Tokyo Express — Ramen (cat 10)
(10, 3, 'Tonkotsu Ramen',   'Rich pork bone broth, chashu, soft egg, noodles',                  130.00,  720, 0,0,0, 1),
(10, 3, 'Spicy Miso Ramen', 'Red miso, chilli paste, ground pork, corn',                        125.00,  690, 0,0,1, 1),
(10, 3, 'Shoyu Ramen',      'Clear soy broth, chicken, bamboo shoots, nori',                    115.00,  580, 0,0,0, 1),
(10, 3, 'Veggie Ramen',     'Mushroom dashi, tofu, bok choy, sesame',                            110.00,  460, 1,1,0, 1),
-- Crispy Zone — Chicken Meals (cat 12)
(12, 4, '2-Piece Meal',     '2 pcs original/spicy chicken + side + drink',                       75.00,  850, 0,0,0, 1),
(12, 4, '4-Piece Meal',     '4 pcs original/spicy chicken + 2 sides + drink',                   130.00, 1400, 0,0,0, 1),
(12, 4, 'Chicken Strips 5', '5 crispy chicken tenders + dipping sauce',                          90.00,  620, 0,0,0, 1),
(12, 4, 'Crispy Sandwich',  'Double crispy fillet, coleslaw, pickles, mayo',                     80.00,  680, 0,0,0, 1),
(12, 4, 'Spicy Sandwich',   'Spicy crispy fillet, pepper jack, chipotle sauce',                  85.00,  710, 0,0,1, 1),
-- Crispy Zone — Sides (cat 14)
(14, 4, 'Coleslaw',         'Creamy house coleslaw',                                             20.00,  180, 1,0,0, 1),
(14, 4, 'Mashed Potatoes',  'Butter mashed with gravy',                                          25.00,  220, 1,0,0, 1),
(14, 4, 'Corn on the Cob',  'Buttered corn, grilled',                                            25.00,  190, 1,1,0, 1),
-- Sandwich Lab — Hot Sandwiches (cat 16)
(16, 5, 'Pastrami Hot',     'Pastrami, swiss, mustard, caramelised onion on toasted rye',        75.00,  580, 0,0,0, 1),
(16, 5, 'Philly Cheesesteak','Shaved ribeye, peppers, onions, provolone on hoagie',              85.00,  720, 0,0,0, 1),
(16, 5, 'Chicken Parm Sub', 'Breaded chicken, marinara, mozzarella on sub roll',                 79.00,  650, 0,0,0, 1),
-- Sandwich Lab — Cold Sandwiches (cat 17)
(17, 5, 'Club Sandwich',    'Turkey, ham, lettuce, tomato, bacon, mayo — triple decker',         70.00,  540, 0,0,0, 1),
(17, 5, 'Tuna Melt',        'Tuna salad, cheddar, toasted white bread',                          65.00,  490, 0,0,0, 1),
(17, 5, 'Caprese Panini',   'Fresh mozzarella, tomato, basil pesto, ciabatta',                   68.00,  430, 1,0,0, 1),
-- Green Bowl — Salads (cat 22)
(22, 7, 'Caesar Salad',     'Romaine, croutons, parmesan, Caesar dressing',                      80.00,  340, 1,0,0, 1),
(22, 7, 'Greek Salad',      'Cucumber, tomato, olives, feta, oregano',                           75.00,  280, 1,1,0, 1),
(22, 7, 'Superfood Salad',  'Kale, quinoa, avocado, pumpkin seeds, lemon tahini',                90.00,  380, 1,1,0, 1),
-- Green Bowl — Grain Bowls (cat 23)
(23, 7, 'Buddha Bowl',      'Brown rice, roasted veg, hummus, falafel, tahini',                  95.00,  520, 1,1,0, 1),
(23, 7, 'Protein Bowl',     'Quinoa, grilled chicken, edamame, avocado, ponzu',                 105.00,  550, 0,0,0, 1),
-- Koshary Palace — Koshary (cat 27)
(27, 9, 'Small Koshary',    'Lentils, rice, pasta, tomato sauce, crispy onions — small',         20.00,  400, 1,1,0, 1),
(27, 9, 'Medium Koshary',   'Lentils, rice, pasta, tomato sauce, crispy onions — medium',        28.00,  600, 1,1,0, 1),
(27, 9, 'Large Koshary',    'Lentils, rice, pasta, tomato sauce, crispy onions — large',         35.00,  800, 1,1,0, 1),
(27, 9, 'XL Koshary',       'Lentils, rice, pasta, tomato sauce, crispy onions — XL',            45.00, 1050, 1,1,0, 1),
-- Koshary Palace — Extras (cat 28)
(28, 9, 'Extra Tomato Sauce','Extra cup of tomato sauce',                                          5.00,   40, 1,1,0, 1),
(28, 9, 'Extra Crispy Onions','Extra cup of crispy fried onions',                                  8.00,  110, 1,1,0, 1),
(28, 9, 'Spicy Vinegar',    'Small bottle of spicy vinegar dressing',                              5.00,   10, 1,1,1, 1),
-- Shawarma Station — Sandwiches (cat 31)
(31, 14, 'Chicken Shawarma Sandwich', 'Marinated chicken, garlic sauce, pickles, tomato, pita', 35.00, 480, 0,0,0, 1),
(31, 14, 'Meat Shawarma Sandwich',    'Spiced beef & lamb, tahini, parsley, onion, pita',       40.00, 530, 0,0,0, 1),
(31, 14, 'Spicy Shawarma Sandwich',   'Chicken shawarma with extra spicy sauce',                40.00, 510, 0,0,1, 1),
(31, 14, 'Double Meat Sandwich',      'Double portion meat shawarma',                            60.00, 780, 0,0,0, 1),
-- Shawarma Station — Plates (cat 32)
(32, 14, 'Chicken Shawarma Plate',  'Chicken shawarma with rice, salad, bread, dips',           75.00, 850, 0,0,0, 1),
(32, 14, 'Mixed Grill Plate',       'Chicken + meat shawarma, grilled onions, sides',           100.00,1100, 0,0,0, 1),
-- Sweet Corner — Waffles (cat 25)
(25, 8, 'Classic Waffle',      'Butter waffle with maple syrup and whipped cream',              55.00, 480, 1,0,0, 1),
(25, 8, 'Nutella Banana Waffle','Belgian waffle, Nutella, fresh banana slices',                  70.00, 620, 1,0,0, 1),
(25, 8, 'Berry Waffle',        'Waffle, mixed berry compote, vanilla ice cream',                 75.00, 590, 1,0,0, 1),
-- Sweet Corner — Hot Drinks (cat 27)
(27, 8, 'Espresso',            'Double shot',                                                    20.00,  10, 1,1,0, 1),
(27, 8, 'Cappuccino',          'Espresso, steamed milk, foam',                                   35.00, 120, 1,0,0, 1),
(27, 8, 'Hot Chocolate',       'Rich dark chocolate, steamed milk, marshmallows',                40.00, 280, 1,0,0, 1);

-- ============================================================
-- ITEM OPTION GROUPS + OPTIONS
-- ============================================================
-- Smash Burgers: Size, Add-ons
INSERT INTO item_option_groups (item_id, name, is_required, min_select, max_select) VALUES
(1, 'Patty Count', 1, 1, 1),   -- group 1
(1, 'Extra Toppings', 0, 0, 4), -- group 2
(2, 'Patty Count', 1, 1, 1),   -- group 3
(2, 'Extra Toppings', 0, 0, 4), -- group 4
(3, 'Patty Count', 1, 1, 1),   -- group 5
(5, 'Patty Type',  1, 1, 1),   -- group 6  (Veggie)
(12,'Flavour', 1, 1, 1),       -- group 7  (Milkshake)
-- Pizza sizes
(13,'Size', 1, 1, 1),          -- group 8
(14,'Size', 1, 1, 1),          -- group 9
(15,'Size', 1, 1, 1),          -- group 10
-- Chicken spice
(32,'Spice Level', 1, 1, 1),   -- group 11  (2-piece meal)
(33,'Spice Level', 1, 1, 1),   -- group 12  (4-piece meal)
-- Ramen broth extras
(28,'Extras', 0, 0, 3),        -- group 13  (Tonkotsu)
-- Koshary spice
(53,'Spice Level', 0, 0, 1),   -- group 14  (Medium Koshary)
(54,'Spice Level', 0, 0, 1),   -- group 15  (Large Koshary)
-- Shawarma extras
(63,'Extras', 0, 0, 3),        -- group 16  (Chicken Shawarma Sandwich)
(64,'Extras', 0, 0, 3);        -- group 17  (Meat Shawarma Sandwich)

INSERT INTO item_options (group_id, name, extra_price) VALUES
-- Patty Count (groups 1,3,5)
(1, 'Double', 0.00),(1, 'Triple', 20.00),
(2, 'Extra Cheese', 10.00),(2, 'Avocado', 15.00),(2, 'Bacon', 15.00),(2, 'Fried Egg', 10.00),
(3, 'Double', 0.00),(3, 'Triple', 20.00),
(4, 'Extra Cheese', 10.00),(4, 'Avocado', 15.00),(4, 'Bacon', 15.00),(4, 'Fried Egg', 10.00),
(5, 'Double', 0.00),(5, 'Triple', 20.00),
(6, 'Classic Plant-Based', 0.00),(6, 'Mushroom Patty', 0.00),
-- Milkshake flavour (group 7)
(7, 'Vanilla', 0.00),(7, 'Chocolate', 0.00),(7, 'Strawberry', 0.00),
-- Pizza sizes (groups 8,9,10)
(8, 'Small 22cm', 0.00),(8, 'Medium 30cm', 25.00),(8, 'Large 36cm', 50.00),
(9, 'Small 22cm', 0.00),(9, 'Medium 30cm', 25.00),(9, 'Large 36cm', 50.00),
(10,'Small 22cm', 0.00),(10,'Medium 30cm', 25.00),(10,'Large 36cm', 50.00),
-- Chicken spice (groups 11,12)
(11,'Original', 0.00),(11,'Spicy', 0.00),(11,'Extra Spicy', 0.00),
(12,'Original', 0.00),(12,'Spicy', 0.00),(12,'Extra Spicy', 0.00),
-- Ramen extras (group 13)
(13,'Extra Chashu', 20.00),(13,'Soft Egg', 10.00),(13,'Bamboo Shoots', 8.00),
-- Koshary spice (groups 14,15)
(14,'Mild', 0.00),(14,'Hot', 0.00),
(15,'Mild', 0.00),(15,'Hot', 0.00),
-- Shawarma extras (groups 16,17)
(16,'Extra Garlic Sauce', 5.00),(16,'Extra Pickles', 3.00),(16,'Chilli Sauce', 3.00),
(17,'Extra Tahini', 5.00),(17,'Extra Parsley', 0.00),(17,'Chilli Sauce', 3.00);

-- ============================================================
-- DELIVERY DRIVERS
-- ============================================================
INSERT INTO delivery_drivers (full_name, phone, email, vehicle_type, vehicle_plate, avg_rating, total_deliveries, status) VALUES
('Mohamed Gamal',     '+201011110001', 'mgamal@driver.com',   'motorcycle', 'CAI-1234', 4.70, 823, 'available'),
('Tarek Samy',        '+201011110002', 'tsamy@driver.com',    'motorcycle', 'GIZ-5678', 4.50, 612, 'on_delivery'),
('Amr Fathy',         '+201011110003', 'afathy@driver.com',   'bicycle',    NULL,       4.80, 441, 'available'),
('Khaled Nabil',      '+201011110004', NULL,                  'motorcycle', 'ALX-9012', 4.30, 1100,'offline'),
('Sherif Hassan',     '+201011110005', 'sherif@driver.com',   'car',        'CAI-3456', 4.60, 256, 'available'),
('Islam Rashad',      '+201011110006', 'irashad@driver.com',  'motorcycle', 'GIZ-7890', 4.40, 389, 'on_delivery'),
('Ahmed Zaki',        '+201011110007', NULL,                  'motorcycle', 'CAI-2345', 4.20, 178, 'available');

-- ============================================================
-- COUPONS
-- ============================================================
INSERT INTO coupons (code, description, discount_type, discount_value, min_order_amount, max_discount, usage_limit, per_user_limit, starts_at, expires_at, is_active, restaurant_id) VALUES
('WELCOME50',   'Welcome offer — 50 EGP off first order',       'fixed',      50.00,  100.00, NULL,  NULL, 1, '2024-01-01 00:00:00', '2026-12-31 23:59:59', 1, NULL),
('SAVE20',      '20% off any order',                            'percentage', 20.00,  150.00, 80.00, 500,  2, '2024-06-01 00:00:00', '2026-06-01 00:00:00', 1, NULL),
('PIZZA15',     '15 EGP off at Pizza Roma',                     'fixed',      15.00,  120.00, NULL,  200,  3, '2024-01-01 00:00:00', '2026-12-31 23:59:59', 1, 2),
('SUSHI25',     '25% off Tokyo Express orders',                 'percentage', 25.00,  200.00, 100.00,100,  2, '2024-03-01 00:00:00', '2026-08-01 00:00:00', 1, 3),
('FREESHIP',    'Free delivery on any order',                   'fixed',      30.00,   50.00, 30.00, NULL, 1, '2024-01-01 00:00:00', '2026-12-31 23:59:59', 1, NULL),
('HEALTHY10',   '10% off Green Bowl',                           'percentage', 10.00,   80.00, 50.00, 150,  3, '2024-04-01 00:00:00', '2026-04-01 00:00:00', 1, 7),
('BIGORDER',    '100 EGP off orders over 500 EGP',              'fixed',     100.00,  500.00, NULL,   50,  1, '2024-01-01 00:00:00', '2026-12-31 23:59:59', 1, NULL),
('RAMADAN30',   'Ramadan special 30% off',                      'percentage', 30.00,   75.00, 120.00,1000, 2, '2025-02-28 00:00:00', '2025-04-05 23:59:59', 1, NULL),
('BURGER10',    '10 EGP off burgers',                           'fixed',      10.00,   89.00, NULL,  NULL, 5, '2024-01-01 00:00:00', '2026-12-31 23:59:59', 1, 1),
('KOSHARY5',    '5 EGP off Koshary Palace',                     'fixed',       5.00,   30.00, NULL,  NULL, 5, '2024-01-01 00:00:00', '2026-12-31 23:59:59', 1, 9);

-- ============================================================
-- ORDERS  (25 orders in various states)
-- ============================================================
INSERT INTO orders (user_id, restaurant_id, address_id, coupon_id, status, payment_method, subtotal, delivery_fee, discount_amount, total_amount, special_instructions, estimated_delivery, placed_at, confirmed_at, delivered_at) VALUES
(1,  1,  1,  NULL, 'delivered',       'cash',   178.00, 15.00,  0.00, 193.00, NULL,                    '2025-03-10 14:25:00', '2025-03-10 13:50:00', '2025-03-10 13:55:00', '2025-03-10 14:22:00'),
(2,  2,  3,  3,   'delivered',        'card',   230.00, 20.00, 15.00, 235.00, 'Extra napkins please',  '2025-03-11 20:00:00', '2025-03-11 19:20:00', '2025-03-11 19:25:00', '2025-03-11 19:58:00'),
(3,  3,  4,  4,   'delivered',        'card',   270.00, 25.00, 67.50, 227.50, NULL,                    '2025-03-12 13:40:00', '2025-03-12 13:00:00', '2025-03-12 13:05:00', '2025-03-12 13:38:00'),
(4,  4,  5,  NULL, 'delivered',       'wallet', 205.00, 12.00,  0.00, 217.00, 'No onions',             '2025-03-13 19:20:00', '2025-03-13 19:00:00', '2025-03-13 19:04:00', '2025-03-13 19:18:00'),
(5,  9,  7,  10,  'delivered',        'cash',    83.00,  5.00,  5.00,  83.00, NULL,                    '2025-03-14 12:30:00', '2025-03-14 12:10:00', '2025-03-14 12:12:00', '2025-03-14 12:28:00'),
(6,  5,  8,  NULL, 'delivered',       'card',   152.00, 10.00,  0.00, 162.00, NULL,                    '2025-03-15 13:00:00', '2025-03-15 12:40:00', '2025-03-15 12:43:00', '2025-03-15 12:58:00'),
(7,  8,  9,  NULL, 'delivered',       'cash',   125.00,  8.00,  0.00, 133.00, NULL,                    '2025-03-16 16:00:00', '2025-03-16 15:45:00', '2025-03-16 15:48:00', '2025-03-16 15:59:00'),
(8,  7,  10, 6,   'delivered',        'wallet', 185.00, 18.00, 18.50, 184.50, 'Dressing on the side',  '2025-03-17 13:30:00', '2025-03-17 13:00:00', '2025-03-17 13:03:00', '2025-03-17 13:28:00'),
(1,  14, 1,  NULL, 'delivered',       'cash',    75.00,  7.00,  0.00,  82.00, NULL,                    '2025-03-18 22:00:00', '2025-03-18 21:50:00', '2025-03-18 21:52:00', '2025-03-18 21:58:00'),
(2,  3,  3,  NULL, 'delivered',       'card',   255.00, 25.00,  0.00, 280.00, 'Extra wasabi',          '2025-03-19 20:40:00', '2025-03-19 20:00:00', '2025-03-19 20:05:00', '2025-03-19 20:38:00'),
(10, 2,  12, NULL, 'delivered',       'card',   345.00, 20.00,  0.00, 365.00, NULL,                    '2025-03-20 19:30:00', '2025-03-20 19:00:00', '2025-03-20 19:04:00', '2025-03-20 19:28:00'),
(11, 4,  13, 9,   'delivered',        'cash',   165.00, 12.00, 10.00, 167.00, 'Extra spicy please',    '2025-03-21 14:00:00', '2025-03-21 13:40:00', '2025-03-21 13:43:00', '2025-03-21 13:58:00'),
(12, 1,  14, 9,   'delivered',        'card',   188.00, 15.00, 10.00, 193.00, NULL,                    '2025-03-22 13:00:00', '2025-03-22 12:40:00', '2025-03-22 12:43:00', '2025-03-22 12:58:00'),
(4,  6,  6,  NULL, 'delivered',       'card',   380.00, 30.00,  0.00, 410.00, 'Allergy: shellfish',    '2025-03-23 20:00:00', '2025-03-23 19:15:00', '2025-03-23 19:20:00', '2025-03-23 19:58:00'),
(1,  9,  1,  10,  'delivered',        'cash',    63.00,  5.00,  5.00,  63.00, NULL,                    '2025-03-24 12:00:00', '2025-03-24 11:50:00', '2025-03-24 11:52:00', '2025-03-24 11:58:00'),
(3,  5,  4,  NULL, 'cancelled',       'card',   145.00, 10.00,  0.00, 155.00, NULL,                    NULL,                  '2025-03-25 13:00:00', NULL,                  NULL),
(6,  10, 8,  2,   'delivered',        'card',   220.00, 15.00, 44.00, 191.00, NULL,                    '2025-03-26 14:30:00', '2025-03-26 14:00:00', '2025-03-26 14:05:00', '2025-03-26 14:28:00'),
(8,  3,  10, NULL, 'delivered',       'wallet', 310.00, 25.00,  0.00, 335.00, NULL,                    '2025-03-27 20:00:00', '2025-03-27 19:20:00', '2025-03-27 19:25:00', '2025-03-27 19:58:00'),
(2,  14, 3,  NULL, 'delivered',       'cash',   115.00,  7.00,  0.00, 122.00, NULL,                    '2025-03-28 23:00:00', '2025-03-28 22:50:00', '2025-03-28 22:52:00', '2025-03-28 22:58:00'),
(5,  4,  7,  NULL, 'preparing',       'cash',   130.00, 12.00,  0.00, 142.00, NULL,                    '2025-04-01 19:30:00', '2025-04-01 19:15:00', '2025-04-01 19:17:00', NULL),
(1,  2,  1,  3,   'out_for_delivery', 'card',   260.00, 20.00, 15.00, 265.00, 'No mushrooms on pizza', '2025-04-01 21:00:00', '2025-04-01 20:20:00', '2025-04-01 20:25:00', NULL),
(4,  7,  5,  6,   'confirmed',        'wallet', 190.00, 18.00, 19.00, 189.00, NULL,                    '2025-04-01 14:00:00', '2025-04-01 13:45:00', '2025-04-01 13:47:00', NULL),
(12, 9,  14, NULL, 'pending',         'cash',    56.00,  5.00,  0.00,  61.00, NULL,                    '2025-04-01 12:10:00', '2025-04-01 12:05:00', NULL,                  NULL),
(7,  8,  9,  NULL, 'cancelled',       'cash',    70.00,  8.00,  0.00,  78.00, NULL,                    NULL,                  '2025-04-01 11:00:00', NULL,                  NULL),
(11, 14, 13, NULL, 'delivered',       'cash',   100.00,  7.00,  0.00, 107.00, NULL,                    '2025-04-01 02:00:00', '2025-04-01 01:45:00', '2025-04-01 01:47:00', '2025-04-01 01:58:00');

-- Update cancelled orders
UPDATE orders SET cancelled_at='2025-03-25 13:05:00', cancel_reason='Customer changed their mind' WHERE order_id=16;
UPDATE orders SET cancelled_at='2025-04-01 11:02:00', cancel_reason='Duplicate order'           WHERE order_id=24;

-- ============================================================
-- ORDER ITEMS
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO order_items (order_id, item_id, quantity, unit_price) VALUES
-- Order 1: Smash & Grill
(1,  1,  2, 89.00),   -- Classic Smash x2
(1,  7,  1, 55.00),   -- Loaded Fries
-- Order 2: Pizza Roma
(2,  14, 1, 115.00),  -- Pepperoni
(2,  21, 1, 110.00),  -- Spaghetti Bolognese
-- Order 3: Tokyo Express
(3,  24, 1, 95.00),   -- California Roll
(3,  25, 1, 110.00),  -- Spicy Tuna Roll
(3,  28, 1, 130.00),  -- Tonkotsu Ramen
-- Order 4: Crispy Zone
(4,  33, 1, 130.00),  -- 4-piece meal
(4,  38, 1, 20.00),   -- Coleslaw
(4,  39, 1, 25.00),   -- Mashed potatoes
(4,  34, 1, 90.00),   -- Strips
-- Order 5: Koshary Palace
(5,  53, 2, 28.00),   -- Medium Koshary x2
(5,  56, 1,  8.00),   -- Extra Crispy Onions
-- Order 6: Sandwich Lab
(6,  41, 1, 75.00),   -- Pastrami Hot
(6,  43, 1, 79.00),   -- Chicken Parm Sub
-- Order 7: Sweet Corner
(7,  69, 1, 55.00),   -- Classic Waffle
(7,  70, 1, 70.00),   -- Nutella Banana Waffle
-- Order 8: Green Bowl
(8,  49, 1, 90.00),   -- Superfood Salad
(8,  51, 1, 105.00),  -- Protein Bowl
-- Order 9: Shawarma Station
(9,  63, 2, 35.00),   -- Chicken Shawarma x2
(9,  66, 1,  5.00),   -- Spicy Vinegar... 
-- Order 10: Tokyo Express
(10, 26, 1, 125.00),  -- Dragon Roll
(10, 27, 1, 140.00),  -- Rainbow Roll
-- Order 11: Pizza Roma
(11, 13, 2, 95.00),   -- Margherita x2 (using medium size)
(11, 22, 1, 105.00),  -- Fettuccine Alfredo
-- Order 12: Crispy Zone
(12, 32, 2, 75.00),   -- 2-piece meal x2
(12, 36, 1, 25.00),   -- Mashed potatoes
-- Order 13: Smash & Grill
(13, 2,  1, 109.00),  -- Bacon Smash
(13, 1,  1,  89.00),  -- Classic Smash
-- Order 14: Ocean Plate
(14, 20, 2, 160.00),  -- Seafood Fiesta... using specialty pizza... actually let's use a different ref
-- Order 15: Koshary Palace
(15, 52, 2, 20.00),   -- Small Koshary x2
(15, 55, 1,  5.00),   -- Extra Tomato Sauce
(15, 57, 1,  5.00),   -- Spicy Vinegar
-- Order 17: Burger Factory
(17, 1,  2, 89.00),   -- Using item from Smash&Grill as proxy
(17, 9,  2, 45.00),   -- Mac Bites x2
-- Order 18: Tokyo Express
(18, 27, 1, 140.00),  -- Rainbow Roll
(18, 28, 1, 130.00),  -- Tonkotsu Ramen
(18, 29, 1, 125.00),  -- Spicy Miso Ramen
-- Order 19: Shawarma Station
(19, 64, 2, 40.00),   -- Meat Shawarma x2
(19, 66, 1, 35.00),   -- Chicken Shawarma Plate
-- Order 20: Crispy Zone
(20, 33, 1, 130.00),  -- 4-piece meal
-- Order 21: Pizza Roma
(21, 14, 1, 115.00),  -- Pepperoni
(21, 17, 1, 160.00),  -- Truffle Prosciutto (with medium upcharge)
-- Order 22: Green Bowl
(22, 50, 1,  95.00),  -- Buddha Bowl
(22, 51, 1, 105.00),  -- Protein Bowl
-- Order 23: Koshary Palace
(23, 54, 1, 35.00),   -- Large Koshary
(23, 56, 1,  8.00),   -- Extra Crispy Onions
(23, 55, 1,  5.00),   -- Extra Tomato
-- Order 25: Shawarma Station
(25, 65, 1, 100.00);  -- Mixed Grill Plate
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- ORDER ITEM OPTIONS
-- ============================================================
INSERT INTO order_item_options (order_item_id, option_id, option_name, extra_price) VALUES
-- Order 1, item 1 (Classic Smash x2) → Triple patty
(1, 2,  'Triple',    20.00),
-- Order 3, item 7 (Tonkotsu Ramen) → Extra chashu + Soft egg
(7, 33, 'Extra Chashu', 20.00),
(7, 34, 'Soft Egg',     10.00),
-- Order 4, item 8 (4-piece meal) → Extra Spicy
(8, 25, 'Extra Spicy', 0.00),
-- Order 6 (Pastrami) — no options
-- Order 9, item 20 (Chicken Shawarma) → Extra garlic sauce
(20, 37, 'Extra Garlic Sauce', 5.00),
-- Order 10, item 22 (Dragon Roll) — no options
-- Order 11, item 24 (Margherita x2) → Medium size
(24, 20, 'Medium 30cm', 25.00),
-- Order 12, item 26 (2-piece meal) → Spicy
(26, 24, 'Spicy', 0.00),
-- Order 18, item 35 (Tonkotsu Ramen) → Extra Chashu
(35, 33, 'Extra Chashu', 20.00),
-- Order 21, item 42 (Pepperoni) → Large
(42, 21, 'Large 36cm', 50.00);

-- ============================================================
-- PAYMENTS
-- ============================================================
INSERT INTO payments (order_id, method, status, amount, transaction_ref, gateway, paid_at) VALUES
(1,  'cash',   'completed', 193.00, NULL,             NULL,       '2025-03-10 14:22:00'),
(2,  'card',   'completed', 235.00, 'TXN-20250311-A', 'paymob',   '2025-03-11 19:22:00'),
(3,  'card',   'completed', 227.50, 'TXN-20250312-B', 'stripe',   '2025-03-12 13:03:00'),
(4,  'wallet', 'completed', 217.00, 'WAL-20250313-C', 'internal', '2025-03-13 19:01:00'),
(5,  'cash',   'completed',  83.00, NULL,             NULL,       '2025-03-14 12:28:00'),
(6,  'card',   'completed', 162.00, 'TXN-20250315-D', 'paymob',   '2025-03-15 12:43:00'),
(7,  'cash',   'completed', 133.00, NULL,             NULL,       '2025-03-16 15:59:00'),
(8,  'wallet', 'completed', 184.50, 'WAL-20250317-E', 'internal', '2025-03-17 13:03:00'),
(9,  'cash',   'completed',  82.00, NULL,             NULL,       '2025-03-18 21:58:00'),
(10, 'card',   'completed', 280.00, 'TXN-20250319-F', 'stripe',   '2025-03-19 20:05:00'),
(11, 'card',   'completed', 365.00, 'TXN-20250320-G', 'paymob',   '2025-03-20 19:04:00'),
(12, 'cash',   'completed', 167.00, NULL,             NULL,       '2025-03-21 13:58:00'),
(13, 'card',   'completed', 193.00, 'TXN-20250322-H', 'stripe',   '2025-03-22 12:43:00'),
(14, 'card',   'completed', 410.00, 'TXN-20250323-I', 'paymob',   '2025-03-23 19:20:00'),
(15, 'cash',   'completed',  63.00, NULL,             NULL,       '2025-03-24 11:58:00'),
(16, 'card',   'refunded',  155.00, 'TXN-20250325-J', 'stripe',   '2025-03-25 13:02:00'),
(17, 'card',   'completed', 191.00, 'TXN-20250326-K', 'paymob',   '2025-03-26 14:05:00'),
(18, 'wallet', 'completed', 335.00, 'WAL-20250327-L', 'internal', '2025-03-27 19:25:00'),
(19, 'cash',   'completed', 122.00, NULL,             NULL,       '2025-03-28 22:58:00'),
(20, 'cash',   'pending',   142.00, NULL,             NULL,       NULL),
(21, 'card',   'completed', 265.00, 'TXN-20250401-M', 'stripe',   '2025-04-01 20:25:00'),
(22, 'wallet', 'completed', 189.00, 'WAL-20250401-N', 'internal', '2025-04-01 13:47:00'),
(23, 'cash',   'pending',    61.00, NULL,             NULL,       NULL),
(24, 'cash',   'refunded',   78.00, NULL,             NULL,       NULL),
(25, 'cash',   'completed', 107.00, NULL,             NULL,       '2025-04-01 01:58:00');

-- refund the cancelled orders
UPDATE payments SET refunded_at='2025-03-25 14:00:00', refund_reason='Customer cancellation' WHERE order_id=16;

-- ============================================================
-- DELIVERIES
-- ============================================================
INSERT INTO deliveries (order_id, driver_id, status, assigned_at, picked_up_at, delivered_at, driver_rating) VALUES
(1,  1, 'delivered', '2025-03-10 13:56:00', '2025-03-10 14:08:00', '2025-03-10 14:22:00', 5),
(2,  2, 'delivered', '2025-03-11 19:26:00', '2025-03-11 19:42:00', '2025-03-11 19:58:00', 4),
(3,  3, 'delivered', '2025-03-12 13:07:00', '2025-03-12 13:20:00', '2025-03-12 13:38:00', 5),
(4,  1, 'delivered', '2025-03-13 19:05:00', '2025-03-13 19:10:00', '2025-03-13 19:18:00', 4),
(5,  5, 'delivered', '2025-03-14 12:13:00', '2025-03-14 12:20:00', '2025-03-14 12:28:00', 5),
(6,  3, 'delivered', '2025-03-15 12:44:00', '2025-03-15 12:50:00', '2025-03-15 12:58:00', 5),
(7,  2, 'delivered', '2025-03-16 15:49:00', '2025-03-16 15:53:00', '2025-03-16 15:59:00', 4),
(8,  5, 'delivered', '2025-03-17 13:04:00', '2025-03-17 13:15:00', '2025-03-17 13:28:00', 5),
(9,  7, 'delivered', '2025-03-18 21:53:00', '2025-03-18 21:56:00', '2025-03-18 21:58:00', 5),
(10, 1, 'delivered', '2025-03-19 20:06:00', '2025-03-19 20:20:00', '2025-03-19 20:38:00', 4),
(11, 3, 'delivered', '2025-03-20 19:05:00', '2025-03-20 19:15:00', '2025-03-20 19:28:00', 5),
(12, 2, 'delivered', '2025-03-21 13:44:00', '2025-03-21 13:52:00', '2025-03-21 13:58:00', 3),
(13, 5, 'delivered', '2025-03-22 12:44:00', '2025-03-22 12:52:00', '2025-03-22 12:58:00', 4),
(14, 1, 'delivered', '2025-03-23 19:21:00', '2025-03-23 19:38:00', '2025-03-23 19:58:00', 5),
(15, 7, 'delivered', '2025-03-24 11:53:00', '2025-03-24 11:56:00', '2025-03-24 11:58:00', 5),
(17, 3, 'delivered', '2025-03-26 14:06:00', '2025-03-26 14:18:00', '2025-03-26 14:28:00', 4),
(18, 6, 'delivered', '2025-03-27 19:26:00', '2025-03-27 19:42:00', '2025-03-27 19:58:00', 5),
(19, 7, 'delivered', '2025-03-28 22:53:00', '2025-03-28 22:56:00', '2025-03-28 22:58:00', 5),
(20, 2, 'picked_up', '2025-04-01 19:18:00', '2025-04-01 19:25:00', NULL,                  NULL),
(21, 6, 'on_the_way','2025-04-01 20:26:00', '2025-04-01 20:48:00', NULL,                  NULL),
(25, 5, 'delivered', '2025-04-01 01:48:00', '2025-04-01 01:53:00', '2025-04-01 01:58:00', 5);

-- ============================================================
-- REVIEWS
-- ============================================================
INSERT INTO reviews (order_id, user_id, restaurant_id, food_rating, delivery_rating, overall_rating, comment, created_at) VALUES
(1,  1,  1, 5, 5, 5, 'Best smash burger I have ever had! Crispy, juicy, perfectly seasoned.',               '2025-03-10 15:00:00'),
(2,  2,  2, 5, 4, 5, 'Pizza was absolutely authentic. Will definitely order again.',                       '2025-03-11 21:00:00'),
(3,  3,  3, 5, 5, 5, 'Sushi was super fresh. Tonkotsu ramen was deeply flavourful.',                       '2025-03-12 15:00:00'),
(4,  4,  4, 4, 4, 4, 'Good chicken, crispy as promised. Delivery was fast.',                               '2025-03-13 20:30:00'),
(5,  5,  9, 5, 5, 5, 'Koshary was great value. Huge portion for the price!',                               '2025-03-14 13:30:00'),
(6,  6,  5, 4, 5, 4, 'Chicken parm sub was delicious but slightly soggy on arrival.',                     '2025-03-15 14:00:00'),
(7,  7,  8, 4, 4, 4, 'Waffles were tasty. Nutella banana combo is a winner.',                              '2025-03-16 17:00:00'),
(8,  8,  7, 5, 5, 5, 'Green bowl was fresh and filling. Protein bowl is my new favourite lunch.',          '2025-03-17 14:30:00'),
(9,  1,  14, 5, 5, 5,'Shawarma arrived hot and fresh at 10 PM! Incredible garlic sauce.',                  '2025-03-18 22:30:00'),
(10, 2,  3, 5, 4, 5, 'Rainbow roll was a work of art. Dragon roll could use a bit more avocado.',          '2025-03-19 21:30:00'),
(11, 10, 2, 4, 5, 4, 'Good pizza but cheese was slightly overdone. Delivery driver was super friendly.',   '2025-03-20 20:30:00'),
(12, 11, 4, 3, 3, 3, 'Chicken was okay but nothing special. Expected crispier skin.',                      '2025-03-21 15:00:00'),
(13, 12, 1, 4, 4, 4, 'Solid burgers. Bacon smash was very good. Will return.',                             '2025-03-22 14:00:00'),
(14, 4,  6, 5, 5, 5, 'Seafood platter was incredibly fresh. Worth every penny!',                           '2025-03-23 21:30:00'),
(15, 1,  9, 5, 5, 5, 'Koshary Palace never disappoints. The crispy onions are everything.',                '2025-03-24 12:30:00'),
(17, 6, 10, 4, 4, 4, 'Good burgers, comparable to the other well-known chains.',                           '2025-03-26 15:30:00'),
(18, 8,  3, 5, 5, 5, 'Second time ordering Tokyo Express. Consistently excellent.',                        '2025-03-27 21:00:00'),
(19, 2, 14, 5, 5, 5, 'Midnight shawarma craving satisfied! Double meat is absolutely worth it.',           '2025-03-28 23:30:00'),
(25,11, 14, 5, 5, 5, 'Mixed grill plate at 2am — no notes. Shawarma Station is the best.',                '2025-04-01 02:30:00');

-- ============================================================
-- USER COUPONS
-- ============================================================
INSERT INTO user_coupons (user_id, coupon_id, order_id, used_at) VALUES
(2,  3,  2,  '2025-03-11 19:20:00'),
(3,  4,  3,  '2025-03-12 13:00:00'),
(5,  10, 5,  '2025-03-14 12:10:00'),
(8,  6,  8,  '2025-03-17 13:00:00'),
(1,  10, 15, '2025-03-24 11:50:00'),
(3,  2,  16, '2025-03-25 13:00:00'),
(6,  2,  17, '2025-03-26 14:00:00'),
(4,  6,  22, '2025-04-01 13:45:00'),
(12, 9,  13, '2025-03-22 12:40:00'),
(11, 9,  12, '2025-03-21 13:40:00'),
(2,  3,  21, '2025-04-01 20:20:00');

-- ============================================================
-- FAVORITES
-- ============================================================
INSERT INTO favorites (user_id, restaurant_id, added_at) VALUES
(1,  1,  '2025-01-15 10:00:00'),
(1,  9,  '2025-01-20 10:00:00'),
(1,  14, '2025-02-01 10:00:00'),
(2,  2,  '2025-01-10 10:00:00'),
(2,  3,  '2025-02-14 10:00:00'),
(3,  3,  '2025-01-25 10:00:00'),
(4,  6,  '2025-02-05 10:00:00'),
(4,  7,  '2025-02-08 10:00:00'),
(5,  9,  '2025-01-18 10:00:00'),
(6,  5,  '2025-01-30 10:00:00'),
(7,  8,  '2025-02-12 10:00:00'),
(8,  7,  '2025-02-01 10:00:00'),
(8,  3,  '2025-02-15 10:00:00'),
(10, 2,  '2025-01-22 10:00:00'),
(11, 4,  '2025-02-03 10:00:00'),
(11, 14, '2025-03-01 10:00:00'),
(12, 1,  '2025-02-20 10:00:00');

-- ============================================================
-- CARTS  (active carts for some users)
-- ============================================================
INSERT INTO carts (user_id, restaurant_id) VALUES
(1,  1),
(4,  3),
(7,  9);

INSERT INTO cart_items (cart_id, item_id, quantity) VALUES
(1, 2, 1),   -- Ahmed has Bacon Smash in cart
(1, 7, 1),   -- + Loaded Fries
(2, 28,1),   -- Nour has Tonkotsu Ramen
(2, 24,2),   -- + California Roll x2
(3, 53,1);   -- Karim has Medium Koshary

INSERT INTO cart_item_options (cart_item_id, option_id) VALUES
(1, 10),  -- Bacon Smash → Extra Avocado
(3, 33);  -- Tonkotsu → Extra Chashu

-- ============================================================
-- NOTIFICATIONS
-- ============================================================
INSERT INTO notifications (user_id, type, title, body, is_read, order_id, created_at) VALUES
(1,  'order_update', 'Order Confirmed!',        'Your order from Smash & Grill has been confirmed.',           1, 1,  '2025-03-10 13:55:00'),
(1,  'order_update', 'Your order is on its way!','Mohamed is heading your way with your Smash & Grill order.',  1, 1,  '2025-03-10 14:08:00'),
(1,  'order_update', 'Order Delivered!',         'Enjoy your meal! Rate your experience.',                      1, 1,  '2025-03-10 14:22:00'),
(2,  'order_update', 'Order Confirmed!',         'Your Pizza Roma order is confirmed.',                         1, 2,  '2025-03-11 19:25:00'),
(2,  'order_update', 'Order Delivered!',         'Your pizza is here! Buon appetito!',                          1, 2,  '2025-03-11 19:58:00'),
(3,  'order_update', 'Order Confirmed!',         'Tokyo Express is preparing your order.',                      1, 3,  '2025-03-12 13:05:00'),
(4,  'order_update', 'Order Delivered!',         'Your Crispy Zone order has arrived. Enjoy!',                  1, 4,  '2025-03-13 19:18:00'),
(5,  'order_update', 'Order Delivered!',         'Your koshary is here — eat up!',                              1, 5,  '2025-03-14 12:28:00'),
(1,  'promo',        'Weekend Deal!',            'Get 20% off your next order this weekend. Use SAVE20.',        0, NULL,'2025-03-29 10:00:00'),
(2,  'promo',        'New restaurant near you!', 'Sushi & Sake just joined us in Zamalek. Check it out!',        1, NULL,'2025-03-15 09:00:00'),
(4,  'order_update', 'Order Confirmed!',         'Your Green Bowl order is confirmed!',                         1, 22, '2025-04-01 13:47:00'),
(1,  'order_update', 'Order Confirmed!',         'Your Pizza Roma order is confirmed.',                         1, 21, '2025-04-01 20:25:00'),
(1,  'order_update', 'Order Out for Delivery!',  'Sherif is on the way with your pizza!',                       0, 21, '2025-04-01 20:48:00'),
(3,  'order_update', 'Order Cancelled',          'Your order from Sandwich Lab was cancelled.',                  1, 16, '2025-03-25 13:05:00'),
(3,  'order_update', 'Refund Processed',         'EGP 155.00 has been refunded to your card.',                  1, 16, '2025-03-25 14:00:00'),
(7,  'order_update', 'Order Cancelled',          'Your Sweet Corner order was cancelled.',                       1, 24, '2025-04-01 11:02:00'),
(8,  'promo',        'Try our new Protein Bowl!','Green Bowl launched a new Protein Bowl. 10% off today only!',  1, NULL,'2025-02-20 09:00:00'),
(11, 'order_update', 'Order Delivered!',         'Your Shawarma Station midnight order is here! Enjoy.',         1, 25, '2025-04-01 01:58:00'),
(12, 'order_update', 'Order Pending',            'Your Koshary Palace order is being processed.',                0, 23, '2025-04-01 12:05:00'),
(5,  'promo',        'Ramadan Kareem!',          'Enjoy 30% off all orders during Ramadan. Use RAMADAN30.',      1, NULL,'2025-02-28 08:00:00');


-- ============================================================
-- USEFUL VIEWS FOR TESTING
-- ============================================================
/*
-- View: Active menu per restaurant with prices
CREATE OR REPLACE VIEW vw_full_menu AS
SELECT
    r.restaurant_id, r.name AS restaurant_name,
    mc.name AS category, mi.item_id,
    mi.name AS item_name, mi.base_price, mi.calories,
    mi.is_vegetarian, mi.is_vegan, mi.is_spicy, mi.is_available
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.restaurant_id
JOIN menu_items mi      ON mi.menu_cat_id = mc.menu_cat_id
ORDER BY r.restaurant_id, mc.display_order, mi.item_id;

-- View: Order summary with user and restaurant details
CREATE OR REPLACE VIEW vw_order_summary AS
SELECT
    o.order_id,
    u.full_name  AS customer,
    r.name       AS restaurant,
    o.status,
    o.payment_method,
    o.subtotal, o.delivery_fee, o.discount_amount, o.total_amount,
    o.placed_at, o.delivered_at,
    d.full_name  AS driver_name,
    del.status   AS delivery_status,
    del.driver_rating
FROM orders o
JOIN users           u   ON u.user_id      = o.user_id
JOIN restaurants     r   ON r.restaurant_id = o.restaurant_id
LEFT JOIN deliveries del ON del.order_id    = o.order_id
LEFT JOIN delivery_drivers d ON d.driver_id = del.driver_id;

-- View: Restaurant performance
CREATE OR REPLACE VIEW vw_restaurant_stats AS
SELECT
    r.restaurant_id, r.name,
    COUNT(DISTINCT o.order_id)  AS total_orders,
    SUM(o.total_amount)         AS total_revenue,
    AVG(o.total_amount)         AS avg_order_value,
    r.avg_rating, r.total_reviews
FROM restaurants r
LEFT JOIN orders o ON o.restaurant_id = r.restaurant_id
   AND o.status NOT IN ('cancelled','refunded')
GROUP BY r.restaurant_id, r.name, r.avg_rating, r.total_reviews;

-- View: Customer lifetime value
CREATE OR REPLACE VIEW vw_customer_ltv AS
SELECT
    u.user_id, u.full_name, u.email,
    COUNT(o.order_id)           AS total_orders,
    SUM(o.total_amount)         AS lifetime_spend,
    AVG(o.total_amount)         AS avg_order_value,
    MAX(o.placed_at)            AS last_order_date
FROM users u
LEFT JOIN orders o ON o.user_id = u.user_id
   AND o.status NOT IN ('cancelled','refunded')
GROUP BY u.user_id, u.full_name, u.email;

*/
-- ============================================================
-- END OF SCRIPT
-- ============================================================