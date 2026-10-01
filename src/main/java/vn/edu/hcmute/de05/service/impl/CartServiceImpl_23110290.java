package vn.edu.hcmute.de05.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.de05.dto.CartLine_23110290;
import vn.edu.hcmute.de05.service.CartService_23110290;
import vn.edu.hcmute.de05.util.JpaUtil_23110290;

public class CartServiceImpl_23110290 implements CartService_23110290 {
    @Override
    public List<CartLine_23110290> getItems(int userId) {
        EntityManager em = JpaUtil_23110290.createEntityManager();
        try {
            List<?> rows = em.createNativeQuery("""
                SELECT ci.cartItemId, p.productId, p.productName, p.images,
                       ci.unitPrice, ci.quantity, p.stock
                FROM Cart c JOIN CartItem ci ON ci.cartId=c.cartId
                JOIN Product p ON p.productId=ci.productId
                WHERE c.userId=?1 AND c.status=0
                ORDER BY ci.cartItemId
                """).setParameter(1, userId).getResultList();
            List<CartLine_23110290> result = new ArrayList<>();
            for (Object rowObject : rows) {
                Object[] r = (Object[]) rowObject;
                result.add(new CartLine_23110290(num(r[0]), num(r[1]),
                    (String) r[2], (String) r[3], (BigDecimal) r[4],
                    num(r[5]), num(r[6])));
            }
            return result;
        } finally { em.close(); }
    }

    @Override
    public BigDecimal getTotal(int userId) {
        return getItems(userId).stream().map(CartLine_23110290::getLineTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void add(int userId, int productId, int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Số lượng phải từ 1 trở lên.");
        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Object[] p = lockProduct(em, productId);
            int stock = num(p[1]);
            if (stock < 1 || num(p[2]) != 1) throw new IllegalArgumentException("Sản phẩm hiện không bán hoặc đã hết hàng.");
            int cartId = activeCart(em, userId, true);
            List<?> found = em.createNativeQuery("SELECT cartItemId, quantity FROM CartItem WHERE cartId=?1 AND productId=?2 FOR UPDATE")
                .setParameter(1, cartId).setParameter(2, productId).getResultList();
            if (found.isEmpty()) {
                if (quantity > stock) throw new IllegalArgumentException("Số lượng vượt quá tồn kho.");
                em.createNativeQuery("INSERT INTO CartItem(quantity,unitPrice,productId,cartId) VALUES(?1,?2,?3,?4)")
                    .setParameter(1, quantity).setParameter(2, (BigDecimal) p[0]).setParameter(3, productId).setParameter(4, cartId).executeUpdate();
            } else {
                Object[] line = (Object[]) found.get(0);
                int next = num(line[1]) + quantity;
                if (next > stock) throw new IllegalArgumentException("Tổng số lượng vượt quá tồn kho (" + stock + ").");
                em.createNativeQuery("UPDATE CartItem SET quantity=?1, unitPrice=?2 WHERE cartItemId=?3")
                    .setParameter(1, next).setParameter(2, (BigDecimal) p[0]).setParameter(3, num(line[0])).executeUpdate();
            }
            tx.commit();
        } catch (RuntimeException e) { rollback(tx); throw e; }
        finally { em.close(); }
    }

    @Override
    public void update(int userId, int cartItemId, int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Số lượng phải từ 1 trở lên; chọn Xóa để bỏ sản phẩm.");
        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<?> rows = em.createNativeQuery("""
                SELECT ci.productId FROM CartItem ci JOIN Cart c ON c.cartId=ci.cartId
                WHERE ci.cartItemId=?1 AND c.userId=?2 AND c.status=0 FOR UPDATE
                """).setParameter(1, cartItemId).setParameter(2, userId).getResultList();
            if (rows.isEmpty()) throw new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ.");
            int productId = num(rows.get(0));
            Object[] p = lockProduct(em, productId);
            if (num(p[2]) != 1 || quantity > num(p[1])) throw new IllegalArgumentException("Số lượng vượt quá tồn kho hiện tại.");
            em.createNativeQuery("UPDATE CartItem SET quantity=?1, unitPrice=?2 WHERE cartItemId=?3")
                .setParameter(1, quantity).setParameter(2, (BigDecimal) p[0]).setParameter(3, cartItemId).executeUpdate();
            tx.commit();
        } catch (RuntimeException e) { rollback(tx); throw e; }
        finally { em.close(); }
    }

    @Override
    public void remove(int userId, int cartItemId) {
        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            int changed = em.createNativeQuery("""
                DELETE ci FROM CartItem ci JOIN Cart c ON c.cartId=ci.cartId
                WHERE ci.cartItemId=?1 AND c.userId=?2 AND c.status=0
                """).setParameter(1, cartItemId).setParameter(2, userId).executeUpdate();
            if (changed == 0) throw new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ.");
            tx.commit();
        } catch (RuntimeException e) { rollback(tx); throw e; }
        finally { em.close(); }
    }

    @Override
    public String checkout(int userId, String customerName, String phone, String address) {
        customerName = clean(customerName); phone = clean(phone); address = clean(address);
        if (customerName.isBlank() || customerName.length() > 100) throw new IllegalArgumentException("Nhập họ tên người nhận (tối đa 100 ký tự).");
        if (!phone.matches("[0-9+() .-]{9,20}")) throw new IllegalArgumentException("Số điện thoại không hợp lệ.");
        if (address.isBlank() || address.length() > 300) throw new IllegalArgumentException("Nhập địa chỉ nhận hàng (tối đa 300 ký tự).");
        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            int cartId = activeCart(em, userId, true);
            List<?> rows = em.createNativeQuery("SELECT cartItemId,productId,quantity,unitPrice FROM CartItem WHERE cartId=?1 FOR UPDATE")
                .setParameter(1, cartId).getResultList();
            if (rows.isEmpty()) throw new IllegalArgumentException("Giỏ hàng đang trống.");
            BigDecimal total = BigDecimal.ZERO;
            List<Object[]> products = new ArrayList<>();
            for (Object row : rows) {
                Object[] line = (Object[]) row;
                int productId = num(line[1]), quantity = num(line[2]);
                Object[] p = lockProduct(em, productId);
                if (num(p[2]) != 1 || num(p[1]) < quantity) throw new IllegalArgumentException("Tồn kho vừa thay đổi. Hãy kiểm tra lại giỏ hàng.");
                BigDecimal price = (BigDecimal) line[3];
                total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
                products.add(new Object[]{productId, quantity, price, (String) p[3]});
            }
            String code = "DH" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
            em.createNativeQuery("""
                INSERT INTO Orders(userId,orderCode,customerName,phone,shippingAddress,paymentMethod,orderStatus,total,createdAt)
                VALUES(?1,?2,?3,?4,?5,'COD','NEW',?6,NOW())
                """).setParameter(1, userId).setParameter(2, code).setParameter(3, customerName)
                .setParameter(4, phone).setParameter(5, address).setParameter(6, total).executeUpdate();
            int orderId = num(em.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult());
            for (Object[] p : products) {
                em.createNativeQuery("INSERT INTO OrderItem(orderId,productId,productName,unitPrice,quantity) VALUES(?1,?2,?3,?4,?5)")
                    .setParameter(1, orderId).setParameter(2, p[0]).setParameter(3, p[3]).setParameter(4, p[2]).setParameter(5, p[1]).executeUpdate();
                em.createNativeQuery("UPDATE Product SET stock=stock-?1 WHERE productId=?2")
                    .setParameter(1, p[1]).setParameter(2, p[0]).executeUpdate();
            }
            em.createNativeQuery("UPDATE Cart SET status=1,buyDate=NOW() WHERE cartId=?1").setParameter(1, cartId).executeUpdate();
            tx.commit();
            return code;
        } catch (RuntimeException e) { rollback(tx); throw e; }
        finally { em.close(); }
    }

    private static Object[] lockProduct(EntityManager em, int id) {
        List<?> rows = em.createNativeQuery("SELECT price,stock,status,productName FROM Product WHERE productId=?1 FOR UPDATE")
            .setParameter(1, id).getResultList();
        if (rows.isEmpty()) throw new IllegalArgumentException("Sản phẩm không còn tồn tại.");
        return (Object[]) rows.get(0);
    }

    private static int activeCart(EntityManager em, int userId, boolean create) {
        List<?> rows = em.createNativeQuery("SELECT cartId FROM Cart WHERE userId=?1 AND status=0 ORDER BY cartId DESC LIMIT 1 FOR UPDATE")
            .setParameter(1, userId).getResultList();
        if (!rows.isEmpty()) return num(rows.get(0));
        if (!create) return 0;
        em.createNativeQuery("INSERT INTO Cart(userId,buyDate,status) VALUES(?1,NULL,0)").setParameter(1, userId).executeUpdate();
        return num(em.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult());
    }
    private static int num(Object value) { return ((Number) value).intValue(); }
    private static String clean(String value) { return value == null ? "" : value.trim(); }
    private static void rollback(EntityTransaction tx) { if (tx.isActive()) tx.rollback(); }
}
