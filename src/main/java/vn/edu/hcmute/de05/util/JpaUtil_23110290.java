package vn.edu.hcmute.de05.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JpaUtil_23110290 {

    private static EntityManagerFactory entityManagerFactory;

    private JpaUtil_23110290() {
    }

    public static synchronized void initialize() {
        if (entityManagerFactory == null
                || !entityManagerFactory.isOpen()) {

            entityManagerFactory =
                    Persistence.createEntityManagerFactory(
                            "de05_23110290PU");
        }
    }

    public static synchronized EntityManager createEntityManager() {
        initialize();
        return entityManagerFactory.createEntityManager();
    }

    public static synchronized void close() {
        if (entityManagerFactory != null) {
            if (entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
            }

            entityManagerFactory = null;
        }
    }
}