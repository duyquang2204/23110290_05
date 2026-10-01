package vn.edu.hcmute.de05.repository.impl;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;

import vn.edu.hcmute.de05.entity.UserRole_23110290;
import vn.edu.hcmute.de05.entity.User_23110290;
import vn.edu.hcmute.de05.repository.UserRepository_23110290;

public class UserRepositoryImpl_23110290
        implements UserRepository_23110290 {

    @Override
    public Optional<User_23110290> findByUsername(
            EntityManager em,
            String username) {

        return em.createQuery(
                "SELECT u FROM User_23110290 u "
                        + "WHERE u.username = :username",
                User_23110290.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<User_23110290> findByEmail(
            EntityManager em,
            String email,
            boolean lock) {

        TypedQuery<User_23110290> query = em.createQuery(
                "SELECT u FROM User_23110290 u "
                        + "WHERE u.email = :email",
                User_23110290.class)
                .setParameter("email", email);

        if (lock) {
            query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        }

        return query.getResultList().stream().findFirst();
    }

    @Override
    public UserRole_23110290 findRole(
            EntityManager em,
            String roleName) {

        return em.createQuery(
                "SELECT r FROM UserRole_23110290 r "
                        + "WHERE r.roleName = :roleName",
                UserRole_23110290.class)
                .setParameter("roleName", roleName)
                .getSingleResult();
    }

    @Override
    public void persist(EntityManager em, User_23110290 user) {
        em.persist(user);
    }
}