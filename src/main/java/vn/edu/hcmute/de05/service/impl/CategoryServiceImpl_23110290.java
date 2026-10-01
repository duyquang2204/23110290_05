package vn.edu.hcmute.de05.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import vn.edu.hcmute.de05.dto.CategoryForm_23110290;
import vn.edu.hcmute.de05.entity.Category_23110290;
import vn.edu.hcmute.de05.exception.ValidationException_23110290;
import vn.edu.hcmute.de05.repository.CategoryRepository_23110290;
import vn.edu.hcmute.de05.repository.impl.CategoryRepositoryImpl_23110290;
import vn.edu.hcmute.de05.service.CategoryService_23110290;

public class CategoryServiceImpl_23110290
        implements CategoryService_23110290 {

    private final CategoryRepository_23110290 repository =
            new CategoryRepositoryImpl_23110290();

    @Override
    public List<Category_23110290> findPage(int page, int pageSize) {
        return repository.findPage(
                Math.max(1, page),
                Math.max(1, Math.min(pageSize, 100)));
    }

    @Override
    public List<Category_23110290> findAll() {
        return repository.findAll();
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Optional<Category_23110290> findById(Integer categoryId) {
        if (categoryId == null || categoryId <= 0) {
            return Optional.empty();
        }

        return repository.findById(categoryId);
    }

    @Override
    public void save(Integer categoryId, CategoryForm_23110290 form) {
        Map<String, String> errors = new LinkedHashMap<>();

        String categoryName = normalize(form.getCategoryName());
        String images = normalize(form.getImages());
        String status = normalize(form.getStatus());

        if (categoryName.isEmpty()) {
            errors.put("categoryName", "Vui lòng nhập tên danh mục.");
        } else if (categoryName.length() > 200) {
            errors.put("categoryName", "Tên danh mục tối đa 200 ký tự.");
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

        if (!"0".equals(status) && !"1".equals(status)) {
            errors.put("status", "Vui lòng chọn trạng thái hợp lệ.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException_23110290(errors);
        }

        Category_23110290 category;

        if (categoryId == null) {
            category = new Category_23110290();
        } else {
            category = repository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Danh mục không còn tồn tại."));
        }

        category.setCategoryName(categoryName);
        category.setImages(images.isEmpty() ? null : images);
        category.setStatus(Integer.parseInt(status));

        repository.save(category);
    }

    @Override
    public void delete(Integer categoryId) {
        if (categoryId == null
                || categoryId <= 0
                || repository.findById(categoryId).isEmpty()) {

            throw new EntityNotFoundException(
                    "Danh mục không còn tồn tại.");
        }

        if (repository.hasProducts(categoryId)) {
            throw new IllegalArgumentException(
                    "Không thể xóa danh mục đang có sản phẩm. "
                            + "Hãy chuyển sản phẩm sang danh mục khác trước.");
        }

        repository.delete(categoryId);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}