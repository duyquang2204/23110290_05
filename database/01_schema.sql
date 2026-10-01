CREATE DATABASE IF NOT EXISTS de05_23110290
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE de05_23110290;

CREATE TABLE UserRoles (
    roleId INT NOT NULL AUTO_INCREMENT,
    roleName VARCHAR(50) NOT NULL,

    CONSTRAINT pk_user_roles PRIMARY KEY (roleId),
    CONSTRAINT uq_user_roles_name UNIQUE (roleName)
) ENGINE = InnoDB;

CREATE TABLE Seller (
    sellerId INT NOT NULL AUTO_INCREMENT,
    sellername VARCHAR(50) NOT NULL,
    images VARCHAR(500),
    status INT NOT NULL DEFAULT 1,

    CONSTRAINT pk_seller PRIMARY KEY (sellerId)
) ENGINE = InnoDB;

CREATE TABLE Category (
    categoryId INT NOT NULL AUTO_INCREMENT,
    categoryName VARCHAR(200) NOT NULL,
    images VARCHAR(500),
    status INT NOT NULL DEFAULT 1,

    CONSTRAINT pk_category PRIMARY KEY (categoryId)
) ENGINE = InnoDB;

CREATE TABLE Users (
    userId INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    fullname VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    images VARCHAR(500),
    phone VARCHAR(20),
    status INT NOT NULL DEFAULT 0,
    code VARCHAR(50),
    roleId INT NOT NULL,
    sellerId INT,

    CONSTRAINT pk_users PRIMARY KEY (userId),
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email UNIQUE (email),

    CONSTRAINT fk_users_role
        FOREIGN KEY (roleId)
        REFERENCES UserRoles (roleId)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,

    CONSTRAINT fk_users_seller
        FOREIGN KEY (sellerId)
        REFERENCES Seller (sellerId)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE Product (
    productId INT NOT NULL AUTO_INCREMENT,
    productName VARCHAR(200) NOT NULL,
    productCode BIGINT NOT NULL,
    categoryId INT NOT NULL,
    description VARCHAR(500),
    price DECIMAL(15,2) NOT NULL,
    amount INT NOT NULL DEFAULT 0,
    stock INT NOT NULL DEFAULT 0,
    images VARCHAR(500),
    wishlist INT NOT NULL DEFAULT 0,
    status INT NOT NULL DEFAULT 1,
    createDate DATE NOT NULL,
    sellerId INT NOT NULL,

    CONSTRAINT pk_product PRIMARY KEY (productId),
    CONSTRAINT uq_product_code UNIQUE (productCode),

    CONSTRAINT chk_product_price CHECK (price >= 0),
    CONSTRAINT chk_product_amount CHECK (amount >= 0),
    CONSTRAINT chk_product_stock CHECK (stock >= 0),
    CONSTRAINT chk_product_wishlist CHECK (wishlist >= 0),

    CONSTRAINT fk_product_category
        FOREIGN KEY (categoryId)
        REFERENCES Category (categoryId)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,

    CONSTRAINT fk_product_seller
        FOREIGN KEY (sellerId)
        REFERENCES Seller (sellerId)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE Cart (
    cartId INT NOT NULL AUTO_INCREMENT,
    userId INT NOT NULL,
    buyDate DATETIME,
    status INT NOT NULL DEFAULT 0,

    CONSTRAINT pk_cart PRIMARY KEY (cartId),

    CONSTRAINT fk_cart_user
        FOREIGN KEY (userId)
        REFERENCES Users (userId)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE CartItem (
    cartItemId INT NOT NULL AUTO_INCREMENT,
    quantity INT NOT NULL,
    unitPrice DECIMAL(15,2) NOT NULL,
    productId INT NOT NULL,
    cartId INT NOT NULL,

    CONSTRAINT pk_cart_item PRIMARY KEY (cartItemId),
    CONSTRAINT chk_cart_item_quantity CHECK (quantity > 0),
    CONSTRAINT chk_cart_item_price CHECK (unitPrice >= 0),

    CONSTRAINT fk_cart_item_product
        FOREIGN KEY (productId)
        REFERENCES Product (productId)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,

    CONSTRAINT fk_cart_item_cart
        FOREIGN KEY (cartId)
        REFERENCES Cart (cartId)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE = InnoDB;