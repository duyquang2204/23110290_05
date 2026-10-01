package vn.edu.hcmute.de05.controller;

import java.io.IOException;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.edu.hcmute.de05.dto.CategoryForm_23110290;
import vn.edu.hcmute.de05.entity.Category_23110290;
import vn.edu.hcmute.de05.exception.ValidationException_23110290;
import vn.edu.hcmute.de05.service.CategoryService_23110290;
import vn.edu.hcmute.de05.service.impl.CategoryServiceImpl_23110290;

@WebServlet(
        name = "CategoryController_23110290",
        urlPatterns = "/admin/categories"
)
public class CategoryController_23110290 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 5;

    private final CategoryService_23110290 service =
            new CategoryServiceImpl_23110290();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ensureCsrfToken(request);

        String action = request.getParameter("action");

        try {
            if ("new".equals(action)) {
                showForm(
                        request,
                        response,
                        null,
                        new CategoryForm_23110290());

            } else if ("edit".equals(action)) {
                Integer id = readId(request);

                Category_23110290 category = service.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Không tìm thấy danh mục."));

                CategoryForm_23110290 form =
                        new CategoryForm_23110290();

                form.setCategoryName(category.getCategoryName());
                form.setImages(
                        category.getImages() == null
                                ? ""
                                : category.getImages());
                form.setStatus(String.valueOf(category.getStatus()));

                showForm(request, response, id, form);

            } else if (action == null
                    || action.isBlank()
                    || "list".equals(action)) {

                showList(request, response);

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Thao tác không hợp lệ.");
            }

        } catch (NumberFormatException exception) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID hoặc số trang không hợp lệ.");

        } catch (EntityNotFoundException exception) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    exception.getMessage());
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        String expectedToken = session == null
                ? null
                : (String) session.getAttribute("csrfToken");

        String submittedToken = request.getParameter("csrfToken");

        if (expectedToken == null
                || !expectedToken.equals(submittedToken)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Phiên biểu mẫu hết hạn. Hãy tải lại trang.");
            return;
        }

        String action = request.getParameter("action");

        if ("save".equals(action)) {
            saveCategory(request, response);

        } else if ("delete".equals(action)) {
            deleteCategory(request, response);

        } else {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Thao tác không hợp lệ.");
        }
    }

    private void showList(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String pageValue = request.getParameter("page");

        int page = pageValue == null
                ? 1
                : Integer.parseInt(pageValue);

        if (page < 1) {
            throw new NumberFormatException();
        }

        long totalItems = service.count();

        int totalPages = (int) Math.max(
                1,
                (totalItems + PAGE_SIZE - 1) / PAGE_SIZE);

        page = Math.min(page, totalPages);

        request.setAttribute(
                "categories",
                service.findPage(page, PAGE_SIZE));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);

        HttpSession session = request.getSession();

        request.setAttribute(
                "successMessage",
                session.getAttribute("categorySuccess"));
        request.setAttribute(
                "errorMessage",
                session.getAttribute("categoryError"));

        session.removeAttribute("categorySuccess");
        session.removeAttribute("categoryError");

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/categories/list.jsp"
        ).forward(request, response);
    }

    private void showForm(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer categoryId,
            CategoryForm_23110290 form)
            throws ServletException, IOException {

        request.setAttribute("categoryId", categoryId);
        request.setAttribute("form", form);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/categories/form.jsp"
        ).forward(request, response);
    }

    private void saveCategory(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer categoryId = null;
        String idValue = request.getParameter("id");

        if (idValue != null && !idValue.isBlank()) {
            try {
                categoryId = readId(request);
            } catch (NumberFormatException exception) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "ID danh mục không hợp lệ.");
                return;
            }
        }

        CategoryForm_23110290 form = new CategoryForm_23110290();

        form.setCategoryName(request.getParameter("categoryName"));
        form.setImages(request.getParameter("images"));
        form.setStatus(request.getParameter("status"));

        try {
            service.save(categoryId, form);

            request.getSession().setAttribute(
                    "categorySuccess",
                    categoryId == null
                            ? "Đã thêm danh mục."
                            : "Đã cập nhật danh mục.");

            redirectToList(request, response);

        } catch (ValidationException_23110290 exception) {
            request.setAttribute("errors", exception.getErrors());
            showForm(request, response, categoryId, form);

        } catch (EntityNotFoundException exception) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    exception.getMessage());

        } catch (PersistenceException exception) {
            getServletContext().log("Loi luu danh muc", exception);

            request.setAttribute(
                    "errorMessage",
                    "Không thể lưu dữ liệu. Vui lòng thử lại.");

            showForm(request, response, categoryId, form);
        }
    }

    private void deleteCategory(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        Integer categoryId;

        try {
            categoryId = readId(request);
        } catch (NumberFormatException exception) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID danh mục không hợp lệ.");
            return;
        }

        try {
            service.delete(categoryId);

            request.getSession().setAttribute(
                    "categorySuccess",
                    "Đã xóa danh mục.");

        } catch (EntityNotFoundException exception) {
            request.getSession().setAttribute(
                    "categoryError",
                    exception.getMessage());

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(
                    "categoryError",
                    exception.getMessage());

        } catch (PersistenceException exception) {
            getServletContext().log("Loi xoa danh muc", exception);

            request.getSession().setAttribute(
                    "categoryError",
                    "Không thể xóa danh mục. "
                            + "Dữ liệu có thể đang được sử dụng.");
        }

        redirectToList(request, response);
    }

    private Integer readId(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));

        if (id <= 0) {
            throw new NumberFormatException();
        }

        return id;
    }

    private void ensureCsrfToken(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (session.getAttribute("csrfToken") == null) {
            session.setAttribute(
                    "csrfToken",
                    UUID.randomUUID().toString());
        }
    }

    private void redirectToList(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath() + "/admin/categories");
    }
}