package vn.edu.hcmute.de05.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.de05.dto.CartLine_23110290;
import vn.edu.hcmute.de05.service.CartService_23110290;
import vn.edu.hcmute.de05.service.impl.CartServiceImpl_23110290;

@WebServlet(name="CartController_23110290", urlPatterns={"/cart", "/checkout"})
public class CartController_23110290 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CartService_23110290 service = new CartServiceImpl_23110290();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = requireUser(request, response);
        if (userId == null) return;
        showCart(request, response, userId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Integer userId = requireUser(request, response);
        if (userId == null) return;
        String path = request.getServletPath();
        try {
            if ("/checkout".equals(path)) {
                String code = service.checkout(userId, request.getParameter("customerName"),
                        request.getParameter("phone"), request.getParameter("address"));
                request.getSession().setAttribute("cartNotice", "Đặt hàng thành công. Mã đơn: " + code + ". Thanh toán COD khi nhận hàng.");
            } else {
                String action = request.getParameter("action");
                int itemId;
                switch (action == null ? "" : action) {
                    case "add" -> service.add(userId, readPositive(request, "productId"), readPositive(request, "quantity"));
                    case "update" -> service.update(userId, readPositive(request, "cartItemId"), readPositive(request, "quantity"));
                    case "remove" -> service.remove(userId, readPositive(request, "cartItemId"));
                    default -> throw new IllegalArgumentException("Thao tác giỏ hàng không hợp lệ.");
                }
                request.getSession().setAttribute("cartNotice", "Đã cập nhật giỏ hàng.");
            }
        } catch (IllegalArgumentException ex) {
            request.getSession().setAttribute("cartError", ex.getMessage());
        } catch (RuntimeException ex) {
            getServletContext().log("Lỗi xử lý giỏ hàng/đặt COD", ex);
            request.getSession().setAttribute("cartError", "Không xử lý được yêu cầu. Kiểm tra database và thử lại.");
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void showCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setAttribute("cartNotice", session.getAttribute("cartNotice"));
        request.setAttribute("cartError", session.getAttribute("cartError"));
        session.removeAttribute("cartNotice");
        session.removeAttribute("cartError");
        List<CartLine_23110290> items = service.getItems(userId);
        BigDecimal total = service.getTotal(userId);
        request.setAttribute("cartItems", items);
        request.setAttribute("cartTotal", total);
        request.getRequestDispatcher("/WEB-INF/views/user/cart.jsp").forward(request, response);
    }

    private Integer requireUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object value = request.getSession().getAttribute("currentUser");
        if (!(value instanceof Map<?, ?> user) || !"USER".equals(String.valueOf(user.get("role")))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        Object id = user.get("userId");
        if (!(id instanceof Number number)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        return number.intValue();
    }

    private static int readPositive(HttpServletRequest request, String name) {
        try {
            int value = Integer.parseInt(request.getParameter(name));
            if (value < 1) throw new NumberFormatException();
            return value;
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Dữ liệu số lượng hoặc sản phẩm không hợp lệ.");
        }
    }
}
