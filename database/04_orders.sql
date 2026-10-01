USE de05_23110290;

-- Chạy một lần trên database hiện có. Cart và CartItem đã có trong 01_schema.sql.
CREATE TABLE IF NOT EXISTS Orders (
    orderId INT NOT NULL AUTO_INCREMENT,
    userId INT NOT NULL,
    orderCode VARCHAR(32) NOT NULL,
    customerName VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    shippingAddress VARCHAR(300) NOT NULL,
    paymentMethod VARCHAR(20) NOT NULL DEFAULT 'COD',
    orderStatus VARCHAR(30) NOT NULL DEFAULT 'NEW',
    total DECIMAL(15,2) NOT NULL,
    createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (orderId),
    UNIQUE KEY uq_orders_code (orderCode),
    KEY ix_orders_user_created (userId, createdAt),
    CONSTRAINT fk_orders_user FOREIGN KEY (userId)
        REFERENCES Users(userId) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_orders_total CHECK (total >= 0),
    CONSTRAINT chk_orders_payment CHECK (paymentMethod IN ('COD')),
    CONSTRAINT chk_orders_status CHECK (orderStatus IN
        ('NEW','CONFIRMED','PREPARING','SHIPPING','DELIVERING','DELIVERED','CANCELLED','RETURNED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS OrderItem (
    orderItemId INT NOT NULL AUTO_INCREMENT,
    orderId INT NOT NULL,
    productId INT NOT NULL,
    productName VARCHAR(200) NOT NULL,
    unitPrice DECIMAL(15,2) NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (orderItemId),
    KEY ix_order_item_order (orderId),
    CONSTRAINT fk_order_item_order FOREIGN KEY (orderId)
        REFERENCES Orders(orderId) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_order_item_product FOREIGN KEY (productId)
        REFERENCES Product(productId) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_order_item_price CHECK (unitPrice >= 0),
    CONSTRAINT chk_order_item_quantity CHECK (quantity > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
