package vn.edu.hcmute.de05.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.edu.hcmute.de05.entity.Product_23110290;
import vn.edu.hcmute.de05.exception.ValidationException_23110290;
import vn.edu.hcmute.de05.service.ProductAdminService_23110290;
import vn.edu.hcmute.de05.service.impl.ProductAdminServiceImpl_23110290;

@WebServlet(
        name = "ProductAdminController_23110290",
        urlPatterns = "/admin/products"
)
public class ProductAdminController_23110290 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 5;

    private static final List<String> FIELDS = List.of(
            "productName",
            "productCode",
            "categoryId",
            "sellerId",
            "price",
            "amount",
            "stock",
            "wishlist",
            "images",
            "description",
            "status"
    );

    private final ProductAdminService_23110290 service =
            new ProductAdminServiceImpl_23110290();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ensureCsrfToken(request);

        String action = request.getParameter("action");

        try {
            if ("new".equals(action)) {
                Map<String, String> form = new LinkedHashMap<>();

                for (String field : FIELDS) {
                    form.put(field, "");
                }

                form.put("price", "0");
                form.put("amount", "0");
                form.put("stock", "0");
                form.put("wishlist", "0");
                form.put("status", "1");
                form.put("images", "/assets/images/book-default.jpg");

                showForm(request, response, null, form);

            } else if ("edit".equals(action)) {
                int id = readId(request);

                Product_23110290 product = service.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Không tìm thấy sản phẩm."));

                showForm(request, response, id, toForm(product));

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

        String expected = session == null
                ? null
                : (String) session.getAttribute("csrfToken");

        if (expected == null
                || !expected.equals(request.getParameter("csrfToken"))) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Phiên biểu mẫu hết hạn. Hãy tải lại trang.");
            return;
        }

        String action = request.getParameter("action");

        if ("save".equals(action)) {
            saveProduct(request, response);

        } else if ("delete".equals(action)) {
            deleteProduct(request, response);

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
                "products",
                service.findPage(page, PAGE_SIZE));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);

        HttpSession session = request.getSession();

        request.setAttribute(
                "successMessage",
                session.getAttribute("productSuccess"));
        request.setAttribute(
                "errorMessage",
                session.getAttribute("productError"));

        session.removeAttribute("productSuccess");
        session.removeAttribute("productError");

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/products/list.jsp"
        ).forward(request, response);
    }

    private void showForm(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer productId,
            Map<String, String> form)
            throws ServletException, IOException {

        request.setAttribute("productId", productId);
        request.setAttribute("form", form);
        request.setAttribute("categories", service.findCategories());
        request.setAttribute("sellers", service.findSellers());

        request.setAttribute("textFields", List.of(
                "productName",
                "productCode",
                "price",
                "amount",
                "stock",
                "wishlist"
        ));

        request.setAttribute("fieldLabels", Map.of(
                "productName", "Tên sản phẩm",
                "productCode", "Mã sản phẩm",
                "price", "Giá",
                "amount", "Amount",
                "stock", "Stock",
                "wishlist", "Wishlist"
        ));

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/products/form.jsp"
        ).forward(request, response);
    }

    private void saveProduct(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer productId = null;
        String idValue = request.getParameter("id");

        if (idValue != null && !idValue.isBlank()) {
            try {
                productId = readId(request);
            } catch (NumberFormatException exception) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "ID sản phẩm không hợp lệ.");
                return;
            }
        }

        Map<String, String> form = new LinkedHashMap<>();

        for (String field : FIELDS) {
            String value = request.getParameter(field);
            form.put(field, value == null ? "" : value);
        }

        try {
            service.save(productId, form);

            request.getSession().setAttribute(
                    "productSuccess",
                    productId == null
                            ? "Đã thêm sản phẩm."
                            : "Đã cập nhật sản phẩm.");

            redirectToList(request, response);

        } catch (ValidationException_23110290 exception) {
            request.setAttribute("errors", exception.getErrors());
            showForm(request, response, productId, form);

        } catch (EntityNotFoundException exception) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    exception.getMessage());

        } catch (PersistenceException exception) {
            getServletContext().log("Loi luu san pham", exception);

            request.setAttribute(
                    "errorMessage",
                    "Không thể lưu sản phẩm. Kiểm tra mã sản phẩm "
                            + "có bị trùng hoặc danh mục/cửa hàng "
                            + "đã thay đổi hay không.");

            showForm(request, response, productId, form);
        }
    }

    private void deleteProduct(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int productId;

        try {
            productId = readId(request);
        } catch (NumberFormatException exception) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID sản phẩm không hợp lệ.");
            return;
        }

        try {
            service.delete(productId);

            request.getSession().setAttribute(
                    "productSuccess",
                    "Đã xóa sản phẩm.");

        } catch (EntityNotFoundException exception) {
            request.getSession().setAttribute(
                    "productError",
                    exception.getMessage());

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(
                    "productError",
                    exception.getMessage());

        } catch (PersistenceException exception) {
            getServletContext().log("Loi xoa san pham", exception);

            request.getSession().setAttribute(
                    "productError",
                    "Không thể xóa sản phẩm đang được sử dụng.");
        }

        redirectToList(request, response);
    }

    private Map<String, String> toForm(Product_23110290 product) {
        Map<String, String> form = new LinkedHashMap<>();

        form.put("productName", product.getProductName());
        form.put("productCode", product.getProductCode().toString());
        form.put(
                "categoryId",
                product.getCategory().getCategoryId().toString());
        form.put(
                "sellerId",
                product.getSeller().getSellerId().toString());
        form.put("price", product.getPrice().toPlainString());
        form.put("amount", product.getAmount().toString());
        form.put("stock", product.getStock().toString());
        form.put("wishlist", product.getWishlist().toString());
        form.put("status", product.getStatus().toString());

        form.put(
                "images",
                product.getImages() == null ? "" : product.getImages());

        form.put(
                "description",
                product.getDescription() == null
                        ? ""
                        : product.getDescription());

        return form;
    }

    private int readId(HttpServletRequest request) {
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
                request.getContextPath() + "/admin/products");
    }
}