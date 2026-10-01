package vn.edu.hcmute.de05.repository.impl;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;

import vn.edu.hcmute.de05.entity.Category_23110290;
import vn.edu.hcmute.de05.repository.CategoryRepository_23110290;
import vn.edu.hcmute.de05.util.JpaUtil_23110290;

public class CategoryRepositoryImpl_23110290
        implements CategoryRepository_23110290 {

    @Override
    public List<Category_23110290> findPage(int page, int pageSize) {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Category_23110290 c "
                            + "ORDER BY c.categoryId DESC",
                    Category_23110290.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category_23110290> findAll() {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Category_23110290 c "
                            + "ORDER BY c.categoryName, c.categoryId",
                    Category_23110290.class)
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
                    "SELECT COUNT(c) FROM Category_23110290 c",
                    Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Category_23110290> findById(Integer categoryId) {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            return Optional.ofNullable(
                    em.find(Category_23110290.class, categoryId));
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Category_23110290 category) {
        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (category.getCategoryId() == null) {
                em.persist(category);
            } else {
                Category_23110290 existing = em.find(
                        Category_23110290.class,
                        category.getCategoryId());

                if (existing == null) {
                    throw new EntityNotFoundException(
                            "Danh mục không còn tồn tại.");
                }

                existing.setCategoryName(category.getCategoryName());
                existing.setImages(category.getImages());
                existing.setStatus(category.getStatus());
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
    public boolean hasProducts(Integer categoryId) {
        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            Long total = em.createQuery(
                    "SELECT COUNT(p) FROM Product_23110290 p "
                            + "WHERE p.category.categoryId = :categoryId",
                    Long.class)
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();

            return total > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Integer categoryId) {
        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Category_23110290 category = em.find(
                    Category_23110290.class,
                    categoryId);

            if (category == null) {
                throw new EntityNotFoundException(
                        "Danh mục không còn tồn tại.");
            }

            em.remove(category);
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