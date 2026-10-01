package vn.edu.hcmute.de05.repository.impl;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;

import vn.edu.hcmute.de05.entity.Seller_23110290;
import vn.edu.hcmute.de05.repository.SellerRepository_23110290;
import vn.edu.hcmute.de05.util.JpaUtil_23110290;

public class SellerRepositoryImpl_23110290
        implements SellerRepository_23110290 {

    @Override
    public List<Seller_23110290> findAll() {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT s FROM Seller_23110290 s "
                            + "ORDER BY s.sellerId",
                    Seller_23110290.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Seller_23110290> findById(Integer sellerId) {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            return Optional.ofNullable(
                    em.find(Seller_23110290.class, sellerId));
        } finally {
            em.close();
        }
    }
}