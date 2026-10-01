package vn.edu.hcmute.de05.repository;

import java.util.List;
import java.util.Optional;

import vn.edu.hcmute.de05.entity.Category_23110290;

public interface CategoryRepository_23110290 {

    List<Category_23110290> findPage(int page, int pageSize);

    List<Category_23110290> findAll();

    long count();

    Optional<Category_23110290> findById(Integer categoryId);

    void save(Category_23110290 category);

    boolean hasProducts(Integer categoryId);

    void delete(Integer categoryId);
}