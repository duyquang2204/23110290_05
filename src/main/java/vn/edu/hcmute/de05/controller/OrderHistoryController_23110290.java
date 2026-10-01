package vn.edu.hcmute.de05.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.de05.dto.OrderStatusOption_23110290;
import vn.edu.hcmute.de05.service.OrderHistoryService_23110290;
import vn.edu.hcmute.de05.service.impl.OrderHistoryServiceImpl_23110290;

@WebServlet(name="OrderHistoryController_23110290", urlPatterns="/orders")
public class OrderHistoryController_23110290 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Set<String> VALID_STATUSES = Set.of(
            "NEW", "CONFIRMED", "PREPARING", "SHIPPING",
            "DELIVERING", "DELIVERED", "CANCELLED", "RETURNED");
    private static final List<OrderStatusOption_23110290> STATUSES = List.of(
            new OrderStatusOption_23110290("NEW", "Đơn hàng mới"),
            new OrderStatusOption_23110290("CONFIRMED", "Đã xác nhận"),
            new OrderStatusOption_23110290("PREPARING", "Chuẩn bị hàng"),
            new OrderStatusOption_23110290("SHIPPING", "Vận chuyển"),
            new OrderStatusOption_23110290("DELIVERING", "Đang giao hàng"),
            new OrderStatusOption_23110290("DELIVERED", "Đã giao"),
            new OrderStatusOption_23110290("CANCELLED", "Đơn hàng hủy"),
            new OrderStatusOption_23110290("RETURNED", "Đơn hàng hoàn"));

    private final OrderHistoryService_23110290 service = new OrderHistoryServiceImpl_23110290();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object principal = request.getSession().getAttribute("currentUser");
        if (!(principal instanceof Map<?, ?> user)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!"USER".equals(String.valueOf(user.get("role")))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Trang này dành cho User.");
            return;
        }
        Object rawId = user.get("userId");
        if (!(rawId instanceof Number userId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String selected = request.getParameter("status");
        if (selected == null || selected.isBlank() || "ALL".equals(selected)) selected = "ALL";
        else if (!VALID_STATUSES.contains(selected)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Trạng thái lọc không hợp lệ.");
            return;
        }

        request.setAttribute("statuses", STATUSES);
        request.setAttribute("selectedStatus", selected);
        request.setAttribute("orders", service.findByUser(
                userId.intValue(), "ALL".equals(selected) ? null : selected));
        request.getRequestDispatcher("/WEB-INF/views/user/orders.jsp").forward(request, response);
    }
}
