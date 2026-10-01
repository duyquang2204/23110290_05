package vn.edu.hcmute.de05.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.edu.hcmute.de05.entity.Product_23110290;
import vn.edu.hcmute.de05.service.ProductService_23110290;
import vn.edu.hcmute.de05.service.impl.ProductServiceImpl_23110290;

@WebServlet(
        name = "ProductController_23110290",
        urlPatterns = {"/products", "/product-detail"}
)
public class ProductController_23110290 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService_23110290 productService =
            new ProductServiceImpl_23110290();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("studentId", "23110290");
        request.setAttribute("examCode", "05");

        if ("/products".equals(request.getServletPath())) {
            showProductList(request, response);
        } else {
            showProductDetail(request, response);
        }
    }

    private void showProductList(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Map<Integer, List<Product_23110290>> productGroups =
                productService.findProductsGroupedBySeller();

        request.setAttribute("productGroups", productGroups);

        request.getRequestDispatcher(
                "/WEB-INF/views/user/products.jsp"
        ).forward(request, response);
    }

    private void showProductDetail(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String idValue = request.getParameter("id");
        int productId;

        try {
            productId = Integer.parseInt(idValue);

            if (productId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException exception) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID sản phẩm phải là số nguyên dương.");
            return;
        }

        Optional<Product_23110290> product =
                productService.findById(productId);

        if (product.isEmpty()) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy sản phẩm.");
            return;
        }

        request.setAttribute("product", product.get());

        request.getRequestDispatcher(
                "/WEB-INF/views/user/product-detail.jsp"
        ).forward(request, response);
    }
}