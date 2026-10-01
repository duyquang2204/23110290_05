package vn.edu.hcmute.de05.repository;

import java.util.List;
import java.util.Optional;

import vn.edu.hcmute.de05.entity.Product_23110290;

public interface ProductRepository_23110290 {

    List<Product_23110290> findAll();

    Optional<Product_23110290> findById(Integer productId);

    List<Product_23110290> findPage(int page, int pageSize);

    long count();

    boolean existsByProductCode(Long productCode, Integer excludedId);

    boolean hasCartItems(Integer productId);

    void save(Product_23110290 product);

    void delete(Integer productId);
}