package vn.edu.hcmute.de05.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import vn.edu.hcmute.de05.entity.Category_23110290;
import vn.edu.hcmute.de05.entity.Product_23110290;
import vn.edu.hcmute.de05.entity.Seller_23110290;
import vn.edu.hcmute.de05.exception.ValidationException_23110290;
import vn.edu.hcmute.de05.repository.CategoryRepository_23110290;
import vn.edu.hcmute.de05.repository.ProductRepository_23110290;
import vn.edu.hcmute.de05.repository.SellerRepository_23110290;
import vn.edu.hcmute.de05.repository.impl.CategoryRepositoryImpl_23110290;
import vn.edu.hcmute.de05.repository.impl.ProductRepositoryImpl_23110290;
import vn.edu.hcmute.de05.repository.impl.SellerRepositoryImpl_23110290;
import vn.edu.hcmute.de05.service.ProductAdminService_23110290;

public class ProductAdminServiceImpl_23110290
        implements ProductAdminService_23110290 {

    private final ProductRepository_23110290 productRepository =
            new ProductRepositoryImpl_23110290();

    private final CategoryRepository_23110290 categoryRepository =
            new CategoryRepositoryImpl_23110290();

    private final SellerRepository_23110290 sellerRepository =
            new SellerRepositoryImpl_23110290();

    @Override
    public List<Product_23110290> findPage(int page, int pageSize) {
        return productRepository.findPage(
                Math.max(1, page),
                Math.max(1, Math.min(pageSize, 100)));
    }

    @Override
    public long count() {
        return productRepository.count();
    }

    @Override
    public Optional<Product_23110290> findById(Integer productId) {
        if (productId == null || productId <= 0) {
            return Optional.empty();
        }

        return productRepository.findById(productId);
    }

    @Override
    public List<Category_23110290> findCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Seller_23110290> findSellers() {
        return sellerRepository.findAll();
    }

    @Override
    public void save(Integer productId, Map<String, String> form) {
        Map<String, String> errors = new LinkedHashMap<>();

        String productName = text(form, "productName");
        String description = text(form, "description");
        String images = text(form, "images");
        String statusValue = text(form, "status");

        if (productName.isEmpty()) {
            errors.put("productName", "Vui lòng nhập tên sản phẩm.");
        } else if (productName.length() > 200) {
            errors.put("productName", "Tên sản phẩm tối đa 200 ký tự.");
        }

        if (description.length() > 500) {
            errors.put("description", "Mô tả tối đa 500 ký tự.");
        }

        if (images.length() > 500) {
            errors.put("images", "Đường dẫn ảnh tối đa 500 ký tự.");
        } else if (!images.isEmpty()
                && !images.matches(
                        "(?i)^/assets/images/[a-z0-9_-]+\\.(jpg|jpeg)$")) {
            errors.put(
                    "images",
                    "Dùng đường dẫn như /assets/images/book-default.jpg");
        }

        if (!"0".equals(statusValue) && !"1".equals(statusValue)) {
            errors.put("status", "Trạng thái không hợp lệ.");
        }

        Long productCode = null;

        try {
            productCode = Long.parseLong(text(form, "productCode"));

            if (productCode <= 0) {
                throw new NumberFormatException();
            }

            if (productRepository.existsByProductCode(
                    productCode,
                    productId)) {
                errors.put("productCode", "Mã sản phẩm đã được sử dụng.");
            }

        } catch (NumberFormatException exception) {
            errors.put(
                    "productCode",
                    "Mã sản phẩm phải là số nguyên dương hợp lệ.");
        }

        BigDecimal price = null;

        try {
            price = new BigDecimal(text(form, "price"));

            if (price.signum() < 0
                    || price.compareTo(
                            new BigDecimal("9999999999999.99")) > 0
                    || price.stripTrailingZeros().scale() > 2) {

                errors.put(
                        "price",
                        "Giá từ 0 đến 9999999999999.99, "
                                + "tối đa 2 chữ số thập phân.");
            }

        } catch (NumberFormatException exception) {
            errors.put("price", "Nhập giá hợp lệ, ví dụ 125000.");
        }

        Integer amount = readInteger(
                form, "amount", 0, errors);

        Integer stock = readInteger(
                form, "stock", 0, errors);

        Integer wishlist = readInteger(
                form, "wishlist", 0, errors);

        Integer categoryId = readInteger(
                form, "categoryId", 1, errors);

        Integer sellerId = readInteger(
                form, "sellerId", 1, errors);

        Category_23110290 category = null;
        Seller_23110290 seller = null;

        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElse(null);

            if (category == null) {
                errors.put("categoryId", "Danh mục không tồn tại.");
            }
        }

        if (sellerId != null) {
            seller = sellerRepository.findById(sellerId)
                    .orElse(null);

            if (seller == null) {
                errors.put("sellerId", "Cửa hàng không tồn tại.");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException_23110290(errors);
        }

        Product_23110290 product;

        if (productId == null) {
            product = new Product_23110290();
            product.setCreateDate(LocalDate.now());
        } else {
            product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Sản phẩm không còn tồn tại."));
        }

        product.setProductName(productName);
        product.setProductCode(productCode);
        product.setCategory(category);
        product.setSeller(seller);
        product.setDescription(
                description.isEmpty() ? null : description);
        product.setPrice(price);
        product.setAmount(amount);
        product.setStock(stock);
        product.setWishlist(wishlist);
        product.setImages(images.isEmpty() ? null : images);
        product.setStatus(Integer.parseInt(statusValue));

        productRepository.save(product);
    }

    @Override
    public void delete(Integer productId) {
        if (productId == null
                || productId <= 0
                || productRepository.findById(productId).isEmpty()) {

            throw new EntityNotFoundException(
                    "Sản phẩm không còn tồn tại.");
        }

        if (productRepository.hasCartItems(productId)) {
            throw new IllegalArgumentException(
                    "Không thể xóa sản phẩm đang được sử dụng "
                            + "trong chi tiết giỏ hàng.");
        }

        productRepository.delete(productId);
    }

    private String text(Map<String, String> form, String field) {
        String value = form.get(field);
        return value == null ? "" : value.trim();
    }

    private Integer readInteger(
            Map<String, String> form,
            String field,
            int minimum,
            Map<String, String> errors) {

        try {
            int value = Integer.parseInt(text(form, field));

            if (value < minimum) {
                throw new NumberFormatException();
            }

            return value;

        } catch (NumberFormatException exception) {
            errors.put(
                    field,
                    minimum == 0
                            ? "Nhập số nguyên không âm hợp lệ."
                            : "Vui lòng chọn một giá trị hợp lệ.");

            return null;
        }
    }
}