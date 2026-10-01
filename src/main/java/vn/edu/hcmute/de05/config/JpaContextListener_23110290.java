package vn.edu.hcmute.de05.config;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import vn.edu.hcmute.de05.util.JpaUtil_23110290;

@WebListener
public class JpaContextListener_23110290
        implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        EntityManager entityManager = null;

        try {
            JpaUtil_23110290.initialize();

            entityManager = JpaUtil_23110290.createEntityManager();

            String databaseName = (String) entityManager
                    .createNativeQuery("SELECT DATABASE()")
                    .getSingleResult();

            Long categoryCount = entityManager
                    .createQuery(
                            "SELECT COUNT(c) FROM Category_23110290 c",
                            Long.class)
                    .getSingleResult();

            context.log("[23110290] Database: " + databaseName);
            context.log("[23110290] So danh muc: " + categoryCount);
            context.log("[23110290] Ket noi JPA thanh cong.");

        } catch (RuntimeException exception) {
            context.log(
                    "[23110290] Khoi tao JPA that bai. "
                            + "Kiem tra phan Caused by trong log.",
                    exception);

            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
                entityManager = null;
            }

            JpaUtil_23110290.close();

            throw new IllegalStateException(
                    "Khong the khoi tao JPA cho de05_23110290.",
                    exception);

        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        JpaUtil_23110290.close();

        event.getServletContext().log(
                "[23110290] Da dong EntityManagerFactory.");
    }
}