package vn.edu.hcmute.de05.service;

import java.math.BigDecimal;
import java.util.List;
import vn.edu.hcmute.de05.dto.CartLine_23110290;

public interface CartService_23110290 {
    List<CartLine_23110290> getItems(int userId);
    BigDecimal getTotal(int userId);
    void add(int userId, int productId, int quantity);
    void update(int userId, int cartItemId, int quantity);
    void remove(int userId, int cartItemId);
    String checkout(int userId, String customerName, String phone, String address);
}
