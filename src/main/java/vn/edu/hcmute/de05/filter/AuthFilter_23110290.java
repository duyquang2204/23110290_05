package vn.edu.hcmute.de05.filter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/*")
public class AuthFilter_23110290 implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) servletRequest;

        HttpServletResponse response =
                (HttpServletResponse) servletResponse;

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getServletPath();

        if (path.startsWith("/assets/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession();

        if (session.getAttribute("csrfToken") == null) {
            session.setAttribute(
                    "csrfToken",
                    UUID.randomUUID().toString());
        }

        Object principal = session.getAttribute("currentUser");

        String role = null;

        if (principal instanceof Map<?, ?> user) {
            role = String.valueOf(user.get("role"));
        }

        boolean adminPath = path.equals("/admin")
                || path.startsWith("/admin/");

        boolean sellerPath = path.equals("/seller")
                || path.startsWith("/seller/");

        if (adminPath || sellerPath) {
            response.setHeader("Cache-Control", "no-store");

            if (role == null) {
                response.sendRedirect(
                        request.getContextPath() + "/login");
                return;
            }

            if (adminPath && !"ADMIN".equals(role)) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Bạn không có quyền quản trị.");
                return;
            }

            if (sellerPath && !"SELLER".equals(role)) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Trang này dành cho Seller.");
                return;
            }
        }

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String expected =
                    (String) session.getAttribute("csrfToken");

            String submitted = request.getParameter("csrfToken");

            if (!expected.equals(submitted)) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Phiên biểu mẫu hết hạn. Hãy tải lại trang.");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}