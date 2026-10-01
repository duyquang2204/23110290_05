package vn.edu.hcmute.de05.repository.impl;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import vn.edu.hcmute.de05.entity.Product_23110290;
import vn.edu.hcmute.de05.repository.ProductRepository_23110290;
import vn.edu.hcmute.de05.util.JpaUtil_23110290;

public class ProductRepositoryImpl_23110290
        implements ProductRepository_23110290 {

    @Override
    public List<Product_23110290> findAll() {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            return em.createQuery(
                    """
                    SELECT p
                    FROM Product_23110290 p
                    JOIN FETCH p.category
                    JOIN FETCH p.seller
                    ORDER BY p.seller.sellerId, p.productId
                    """,
                    Product_23110290.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Product_23110290> findById(Integer productId) {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            List<Product_23110290> products = em.createQuery(
                    """
                    SELECT p
                    FROM Product_23110290 p
                    JOIN FETCH p.category
                    JOIN FETCH p.seller
                    WHERE p.productId = :id
                    """,
                    Product_23110290.class)
                    .setParameter("id", productId)
                    .getResultList();

            return products.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product_23110290> findPage(int page, int pageSize) {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            return em.createQuery(
                    """
                    SELECT p
                    FROM Product_23110290 p
                    JOIN FETCH p.category
                    JOIN FETCH p.seller
                    ORDER BY p.productId DESC
                    """,
                    Product_23110290.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT COUNT(p) FROM Product_23110290 p",
                    Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByProductCode(
            Long productCode,
            Integer excludedId) {

        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            String jpql =
                    "SELECT COUNT(p) FROM Product_23110290 p "
                            + "WHERE p.productCode = :code";

            if (excludedId != null) {
                jpql += " AND p.productId <> :excludedId";
            }

            TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                    .setParameter("code", productCode);

            if (excludedId != null) {
                query.setParameter("excludedId", excludedId);
            }

            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean hasCartItems(Integer productId) {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            Number total = (Number) em.createNativeQuery(
                    "SELECT COUNT(*) FROM CartItem WHERE productId = ?1")
                    .setParameter(1, productId)
                    .getSingleResult();

            return total.longValue() > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Product_23110290 product) {
        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (product.getProductId() == null) {
                em.persist(product);
            } else {
                Product_23110290 existing = em.find(
                        Product_23110290.class,
                        product.getProductId());

                if (existing == null) {
                    throw new EntityNotFoundException(
                            "Sản phẩm không còn tồn tại.");
                }

                em.merge(product);
            }

            transaction.commit();

        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Integer productId) {
        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Product_23110290 product = em.find(
                    Product_23110290.class,
                    productId);

            if (product == null) {
                throw new EntityNotFoundException(
                        "Sản phẩm không còn tồn tại.");
            }

            em.remove(product);
            transaction.commit();

        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        } finally {
            em.close();
        }
    }
}