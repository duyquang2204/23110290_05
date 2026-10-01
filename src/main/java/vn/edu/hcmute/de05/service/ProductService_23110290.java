package vn.edu.hcmute.de05.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import vn.edu.hcmute.de05.entity.Product_23110290;

public interface ProductService_23110290 {

    Map<Integer, List<Product_23110290>> findProductsGroupedBySeller();

    Optional<Product_23110290> findById(Integer productId);
}