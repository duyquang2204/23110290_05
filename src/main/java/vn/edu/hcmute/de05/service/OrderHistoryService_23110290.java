package vn.edu.hcmute.de05.service;

import java.util.List;
import vn.edu.hcmute.de05.dto.OrderHistory_23110290;

public interface OrderHistoryService_23110290 {
    List<OrderHistory_23110290> findByUser(int userId, String status);
}
