USE de05_23110290;

SET NAMES utf8mb4;

START TRANSACTION;

INSERT INTO UserRoles (roleName)
VALUES
    ('USER'),
    ('SELLER'),
    ('ADMIN');

INSERT INTO Seller (sellername, images, status)
VALUES
    ('Nhà sách Bình Minh', NULL, 1);

SET @seller1 = LAST_INSERT_ID();

INSERT INTO Seller (sellername, images, status)
VALUES
    ('Nhà sách Góc Nhỏ', NULL, 1);

SET @seller2 = LAST_INSERT_ID();

INSERT INTO Category (categoryName, images, status)
VALUES ('Lập trình', NULL, 1);

SET @category1 = LAST_INSERT_ID();

INSERT INTO Category (categoryName, images, status)
VALUES ('Cơ sở dữ liệu', NULL, 1);

SET @category2 = LAST_INSERT_ID();

INSERT INTO Category (categoryName, images, status)
VALUES ('Ngoại ngữ', NULL, 1);

SET @category3 = LAST_INSERT_ID();

INSERT INTO Category (categoryName, images, status)
VALUES ('Kỹ năng học tập', NULL, 1);

SET @category4 = LAST_INSERT_ID();

INSERT INTO Category (categoryName, images, status)
VALUES ('Văn học', NULL, 1);

SET @category5 = LAST_INSERT_ID();

INSERT INTO Category (categoryName, images, status)
VALUES ('Khoa học', NULL, 1);

SET @category6 = LAST_INSERT_ID();

INSERT INTO Product (
    productName,
    productCode,
    categoryId,
    description,
    price,
    amount,
    stock,
    images,
    wishlist,
    status,
    createDate,
    sellerId
)
VALUES
(
    'Java căn bản',
    100001,
    @category1,
    'Tài liệu nhập môn Java, biến, vòng lặp và lập trình hướng đối tượng.',
    125000.00,
    20,
    20,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller1
),
(
    'Lập trình Web với Servlet',
    100002,
    @category1,
    'Tìm hiểu request, response, Servlet, JSP và quản lý Session.',
    165000.00,
    15,
    15,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller1
),
(
    'MySQL thực hành',
    100003,
    @category2,
    'Hướng dẫn tạo bảng, truy vấn SQL và thiết kế khóa ngoại.',
    135000.00,
    18,
    18,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller1
),
(
    'Tiếng Anh mỗi ngày',
    100004,
    @category3,
    'Bài học từ vựng và mẫu câu giao tiếp thường gặp.',
    89000.00,
    30,
    30,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller1
),
(
    'Học tập có kế hoạch',
    100005,
    @category4,
    'Phương pháp chia nhỏ mục tiêu và theo dõi tiến độ học tập.',
    75000.00,
    12,
    12,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller1
),
(
    'Những ngày bình yên',
    100006,
    @category5,
    'Tập truyện ngắn về cuộc sống và những điều giản dị.',
    98000.00,
    16,
    16,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller1
),
(
    'Java hướng đối tượng',
    100007,
    @category1,
    'Thực hành class, interface, kế thừa và đa hình trong Java.',
    145000.00,
    22,
    22,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller2
),
(
    'Thiết kế cơ sở dữ liệu',
    100008,
    @category2,
    'Mô hình quan hệ, chuẩn hóa dữ liệu và thiết kế bảng.',
    155000.00,
    14,
    14,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller2
),
(
    'Tiếng Anh cho sinh viên',
    100009,
    @category3,
    'Từ vựng và bài đọc dành cho môi trường đại học.',
    109000.00,
    25,
    25,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller2
),
(
    'Ghi chép hiệu quả',
    100010,
    @category4,
    'Các cách tổ chức nội dung và ôn tập từ ghi chép.',
    69000.00,
    19,
    19,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller2
),
(
    'Chuyện trong thành phố',
    100011,
    @category5,
    'Những câu chuyện nhỏ về con người và đời sống đô thị.',
    92000.00,
    17,
    17,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller2
),
(
    'Khoa học quanh ta',
    100012,
    @category6,
    'Giải thích các hiện tượng khoa học gần gũi trong cuộc sống.',
    119000.00,
    21,
    21,
    NULL,
    0,
    1,
    CURRENT_DATE(),
    @seller2
);

COMMIT;