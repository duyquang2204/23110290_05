package vn.edu.hcmute.de05.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        name = "HomeController_23110290",
        urlPatterns = {"/home", "/admin/home", "/seller/home"}
)
public class HomeController_23110290 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Cache-Control", "no-store");

        request.setAttribute("studentId", "23110290");
        request.setAttribute("examCode", "05");

        String path = request.getServletPath();

        String title = switch (path) {
            case "/admin/home" -> "Trang quản trị";
            case "/seller/home" -> "Trang chủ người bán";
            default -> "Sổ cửa hàng";
        };

        request.setAttribute("pageTitle", title);

        request.getRequestDispatcher(
                "/WEB-INF/views/user/home.jsp"
        ).forward(request, response);
    }
}