package vn.edu.hcmute.de05.service;

import java.util.List;
import java.util.Optional;

import vn.edu.hcmute.de05.dto.CategoryForm_23110290;
import vn.edu.hcmute.de05.entity.Category_23110290;

public interface CategoryService_23110290 {

    List<Category_23110290> findPage(int page, int pageSize);

    List<Category_23110290> findAll();

    long count();

    Optional<Category_23110290> findById(Integer categoryId);

    void save(Integer categoryId, CategoryForm_23110290 form);

    void delete(Integer categoryId);
}