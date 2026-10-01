USE de05_23110290;

UPDATE Product
SET images = '/assets/images/book-default.jpg'
WHERE productCode BETWEEN 100001 AND 100012;

SELECT
    productId,
    productCode,
    productName,
    images
FROM Product
ORDER BY productId;