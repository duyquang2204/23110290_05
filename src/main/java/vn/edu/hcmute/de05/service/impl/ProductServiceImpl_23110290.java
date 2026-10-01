package vn.edu.hcmute.de05.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import vn.edu.hcmute.de05.entity.Product_23110290;
import vn.edu.hcmute.de05.repository.ProductRepository_23110290;
import vn.edu.hcmute.de05.repository.impl.ProductRepositoryImpl_23110290;
import vn.edu.hcmute.de05.service.ProductService_23110290;

public class ProductServiceImpl_23110290
        implements ProductService_23110290 {

    private final ProductRepository_23110290 productRepository =
            new ProductRepositoryImpl_23110290();

    @Override
    public Map<Integer, List<Product_23110290>>
            findProductsGroupedBySeller() {

        List<Product_23110290> products = productRepository.findAll();

        Map<Integer, List<Product_23110290>> groups =
                new LinkedHashMap<>();

        for (Product_23110290 product : products) {
            Integer sellerId = product.getSeller().getSellerId();

            groups.computeIfAbsent(
                    sellerId,
                    key -> new ArrayList<>()
            ).add(product);
        }

        return groups;
    }

    @Override
    public Optional<Product_23110290> findById(Integer productId) {
        if (productId == null || productId <= 0) {
            return Optional.empty();
        }

        return productRepository.findById(productId);
    }
}