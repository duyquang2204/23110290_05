package vn.edu.hcmute.de05.repository;

import java.util.Optional;

import jakarta.persistence.EntityManager;

import vn.edu.hcmute.de05.entity.UserRole_23110290;
import vn.edu.hcmute.de05.entity.User_23110290;

public interface UserRepository_23110290 {

    Optional<User_23110290> findByUsername(
            EntityManager em,
            String username);

    Optional<User_23110290> findByEmail(
            EntityManager em,
            String email,
            boolean lock);

    UserRole_23110290 findRole(
            EntityManager em,
            String roleName);

    void persist(EntityManager em, User_23110290 user);
}