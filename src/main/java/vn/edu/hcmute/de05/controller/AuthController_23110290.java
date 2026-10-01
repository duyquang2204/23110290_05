package vn.edu.hcmute.de05.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.edu.hcmute.de05.exception.ValidationException_23110290;
import vn.edu.hcmute.de05.service.AuthService_23110290;
import vn.edu.hcmute.de05.service.impl.AuthServiceImpl_23110290;

@WebServlet(
        name = "AuthController_23110290",
        urlPatterns = {
                "/register",
                "/verify",
                "/resend-otp",
                "/login",
                "/logout"
        }
)
public class AuthController_23110290 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AuthService_23110290 service =
            new AuthServiceImpl_23110290();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/logout".equals(path) || "/resend-otp".equals(path)) {
            response.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "Thao tác này yêu cầu POST.");
            return;
        }

        HttpSession session = request.getSession();

        request.setAttribute("notice", session.getAttribute("authNotice"));
        session.removeAttribute("authNotice");

        Map<String, String> form = new LinkedHashMap<>();

        form.put("email", value(request.getParameter("email")));

        if ("/login".equals(path)) {
            form.put(
                    "username",
                    value((String) session.getAttribute("loginUsername")));

            request.setAttribute(
                    "errorMessage",
                    session.getAttribute("loginError"));

            session.removeAttribute("loginUsername");
            session.removeAttribute("loginError");
        }

        show(request, response, path.substring(1), form);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("/login".equals(path)) {
            login(request, response);
            return;
        }

        Map<String, String> form = new LinkedHashMap<>();

        for (String field : List.of(
                "username",
                "fullname",
                "email",
                "password",
                "confirmPassword",
                "otp")) {

            form.put(field, value(request.getParameter(field)));
        }

        String mode = "/register".equals(path) ? "register" : "verify";

        try {
            if ("/register".equals(path)) {
                service.register(form);

                request.getSession().setAttribute(
                        "authNotice",
                        "Đã gửi OTP qua email. Kiểm tra cả thư mục Spam.");

                redirectVerify(request, response, form.get("email"));

            } else if ("/verify".equals(path)) {
                service.activate(form.get("email"), form.get("otp"));

                request.getSession().setAttribute(
                        "authNotice",
                        "Kích hoạt thành công. Bạn có thể đăng nhập.");

                response.sendRedirect(
                        request.getContextPath() + "/login");

            } else if ("/resend-otp".equals(path)) {
                service.resendOtp(form.get("email"));

                request.getSession().setAttribute(
                        "authNotice",
                        "Đã gửi OTP mới. Mã cũ không còn hiệu lực.");

                redirectVerify(request, response, form.get("email"));

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (ValidationException_23110290 exception) {
            request.setAttribute("errors", exception.getErrors());

            form.remove("password");
            form.remove("confirmPassword");

            show(request, response, mode, form);

        } catch (RuntimeException exception) {
            getServletContext().log("Loi chuc nang tai khoan", exception);

            request.setAttribute(
                    "errorMessage",
                    "Chưa hoàn tất thao tác. Nếu vừa đăng ký, kiểm tra "
                            + "email/tên đăng nhập bị trùng và cấu hình gửi mail. "
                            + "Xem Console để biết nguyên nhân.");

            form.remove("password");
            form.remove("confirmPassword");

            show(request, response, mode, form);
        }
    }

    private void login(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String username = value(request.getParameter("username"));

        try {
            Map<String, Object> user = service.login(
                    username,
                    request.getParameter("password"));

            HttpSession session = request.getSession();

            request.changeSessionId();

            session.setAttribute("currentUser", user);
            session.setAttribute(
                    "csrfToken",
                    UUID.randomUUID().toString());

            String role = String.valueOf(user.get("role"));

            String destination = switch (role) {
                case "ADMIN" -> "/admin/home";
                case "SELLER" -> "/seller/home";
                default -> "/home";
            };

            response.sendRedirect(
                    request.getContextPath() + destination);

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(
                    "loginError",
                    exception.getMessage());

            request.getSession().setAttribute(
                    "loginUsername",
                    username);

            response.sendRedirect(
                    request.getContextPath() + "/login");

        } catch (RuntimeException exception) {
            getServletContext().log("Loi dang nhap", exception);

            request.getSession().setAttribute(
                    "loginError",
                    "Không thể đăng nhập lúc này. Vui lòng thử lại.");

            response.sendRedirect(
                    request.getContextPath() + "/login");
        }
    }

    private void show(
            HttpServletRequest request,
            HttpServletResponse response,
            String mode,
            Map<String, String> form)
            throws ServletException, IOException {

        request.setAttribute("mode", mode);
        request.setAttribute("form", form);

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/auth.jsp"
        ).forward(request, response);
    }

    private void redirectVerify(
            HttpServletRequest request,
            HttpServletResponse response,
            String email)
            throws IOException {

        String normalizedEmail = value(email)
                .trim()
                .toLowerCase(Locale.ROOT);

        response.sendRedirect(
                request.getContextPath()
                        + "/verify?email="
                        + URLEncoder.encode(
                                normalizedEmail,
                                StandardCharsets.UTF_8));
    }

    private String value(String value) {
        return value == null ? "" : value;
    }
}