package vn.edu.hcmute.de05.repository;

import java.util.List;
import java.util.Optional;

import vn.edu.hcmute.de05.entity.Seller_23110290;

public interface SellerRepository_23110290 {

    List<Seller_23110290> findAll();

    Optional<Seller_23110290> findById(Integer sellerId);
}