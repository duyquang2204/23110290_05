package vn.edu.hcmute.de05.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import vn.edu.hcmute.de05.dto.OrderHistoryItem_23110290;
import vn.edu.hcmute.de05.dto.OrderHistory_23110290;
import vn.edu.hcmute.de05.service.OrderHistoryService_23110290;
import vn.edu.hcmute.de05.util.JpaUtil_23110290;

public class OrderHistoryServiceImpl_23110290 implements OrderHistoryService_23110290 {

    @Override
    public List<OrderHistory_23110290> findByUser(int userId, String status) {
        EntityManager em = JpaUtil_23110290.createEntityManager();
        try {
            String sql = """
                SELECT orderId, orderCode, customerName, phone, shippingAddress,
                       paymentMethod, orderStatus, total, createdAt
                FROM Orders
                WHERE userId=?1
                """ + (status == null ? "" : " AND orderStatus=?2")
                    + " ORDER BY createdAt DESC, orderId DESC";

            Query query = em.createNativeQuery(sql).setParameter(1, userId);
            if (status != null) query.setParameter(2, status);

            List<?> rows = query.getResultList();
            List<OrderHistory_23110290> orders = new ArrayList<>();
            for (Object raw : rows) {
                Object[] r = (Object[]) raw;
                int orderId = ((Number) r[0]).intValue();
                String code = (String) r[1];
                String orderStatus = (String) r[6];
                List<OrderHistoryItem_23110290> items = findItems(em, orderId);
                Date createdAt = r[8] instanceof Timestamp timestamp
                        ? new Date(timestamp.getTime()) : (Date) r[8];

                orders.add(new OrderHistory_23110290(
                        orderId, code, (String) r[2], (String) r[3], (String) r[4],
                        (String) r[5], orderStatus, statusLabel(orderStatus),
                        (BigDecimal) r[7], createdAt, items));
            }
            return orders;
        } finally {
            em.close();
        }
    }

    private List<OrderHistoryItem_23110290> findItems(EntityManager em, int orderId) {
        List<?> rows = em.createNativeQuery("""
                SELECT productName, unitPrice, quantity
                FROM OrderItem WHERE orderId=?1 ORDER BY orderItemId
                """).setParameter(1, orderId).getResultList();
        List<OrderHistoryItem_23110290> items = new ArrayList<>();
        for (Object raw : rows) {
            Object[] r = (Object[]) raw;
            items.add(new OrderHistoryItem_23110290(
                    (String) r[0], (BigDecimal) r[1], ((Number) r[2]).intValue()));
        }
        return items;
    }

    private String statusLabel(String status) {
        return switch (status) {
            case "NEW" -> "Đơn hàng mới";
            case "CONFIRMED" -> "Đã xác nhận";
            case "PREPARING" -> "Chuẩn bị hàng";
            case "SHIPPING" -> "Vận chuyển";
            case "DELIVERING" -> "Đang giao hàng";
            case "DELIVERED" -> "Đã giao";
            case "CANCELLED" -> "Đơn hàng hủy";
            case "RETURNED" -> "Đơn hàng hoàn";
            default -> "Không xác định";
        };
    }
}
