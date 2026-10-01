package vn.edu.hcmute.de05.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import vn.edu.hcmute.de05.entity.Category_23110290;
import vn.edu.hcmute.de05.entity.Product_23110290;
import vn.edu.hcmute.de05.entity.Seller_23110290;

public interface ProductAdminService_23110290 {

    List<Product_23110290> findPage(int page, int pageSize);

    long count();

    Optional<Product_23110290> findById(Integer productId);

    List<Category_23110290> findCategories();

    List<Seller_23110290> findSellers();

    void save(Integer productId, Map<String, String> form);

    void delete(Integer productId);
}