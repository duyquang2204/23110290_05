<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        ${empty productId ? 'Thêm sản phẩm' : 'Sửa sản phẩm'}
    </title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>

<header class="site-header">
    <div class="container header-inner">

        <a class="brand"
           href="${pageContext.request.contextPath}/home">
            Sổ cửa hàng
        </a>

        <nav aria-label="Điều hướng quản trị">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/admin/categories">
                Danh mục
            </a>

            <a class="nav-link active"
               href="${pageContext.request.contextPath}/admin/products">
                Quản lý sản phẩm
            </a>
        </nav>
    </div>
</header>

<main class="container main-content">

    <a class="back-link"
       href="${pageContext.request.contextPath}/admin/products">
        ← Quay lại danh sách sản phẩm
    </a>

    <div class="page-heading">
        <p class="eyebrow">QUẢN TRỊ CỬA HÀNG</p>

        <h1>
            ${empty productId ? 'Thêm sản phẩm' : 'Sửa sản phẩm'}
        </h1>

        <p class="muted">
            Trường có dấu * là bắt buộc.
            Nhập giá như 125000 hoặc 125000.50.
        </p>
    </div>

    <div class="panel product-form-panel">

        <c:if test="${not empty errorMessage}">
            <p class="notice notice-error" role="alert">
                <c:out value="${errorMessage}" />
            </p>
        </c:if>

        <form method="post"
              action="${pageContext.request.contextPath}/admin/products"
              novalidate>

            <input type="hidden"
                   name="action"
                   value="save">

            <input type="hidden"
                   name="id"
                   value="<c:out value='${productId}' />">

            <input type="hidden"
                   name="csrfToken"
                   value="<c:out value='${sessionScope.csrfToken}' />">

            <div class="form-grid">

                <c:forEach var="field" items="${textFields}">
                    <div class="form-field">
                        <label for="${field}">
                            <c:out value="${fieldLabels[field]}" /> *
                        </label>

                        <input type="text"
                               id="${field}"
                               name="${field}"
                               required
                               aria-describedby="${field}-error"
                               aria-invalid="${not empty errors[field]}"
                               value="<c:out value='${form[field]}' />">

                        <span id="${field}-error" class="field-error">
                            <c:out value="${errors[field]}" />
                        </span>
                    </div>
                </c:forEach>

                <div class="form-field">
                    <label for="categoryId">Danh mục *</label>

                    <select id="categoryId"
                            name="categoryId"
                            required
                            aria-describedby="categoryId-error"
                            aria-invalid="${not empty errors.categoryId}">

                        <option value="">Chọn danh mục</option>

                        <c:forEach var="category" items="${categories}">
                            <option value="${category.categoryId}"
                                    ${form.categoryId == category.categoryId.toString() ? 'selected' : ''}>
                                <c:out value="${category.categoryName}" />
                            </option>
                        </c:forEach>
                    </select>

                    <span id="categoryId-error" class="field-error">
                        <c:out value="${errors.categoryId}" />
                    </span>
                </div>

                <div class="form-field">
                    <label for="sellerId">Cửa hàng *</label>

                    <select id="sellerId"
                            name="sellerId"
                            required
                            aria-describedby="sellerId-error"
                            aria-invalid="${not empty errors.sellerId}">

                        <option value="">Chọn cửa hàng</option>

                        <c:forEach var="seller" items="${sellers}">
                            <option value="${seller.sellerId}"
                                    ${form.sellerId == seller.sellerId.toString() ? 'selected' : ''}>
                                <c:out value="${seller.sellername}" />
                            </option>
                        </c:forEach>
                    </select>

                    <span id="sellerId-error" class="field-error">
                        <c:out value="${errors.sellerId}" />
                    </span>
                </div>

                <div class="form-field">
                    <label for="images">Đường dẫn ảnh JPG</label>

                    <input type="text"
                           id="images"
                           name="images"
                           maxlength="500"
                           placeholder="/assets/images/book-default.jpg"
                           aria-describedby="images-error"
                           aria-invalid="${not empty errors.images}"
                           value="<c:out value='${form.images}' />">

                    <span id="images-error" class="field-error">
                        <c:out value="${errors.images}" />
                    </span>
                </div>

                <div class="form-field">
                    <label for="status">Trạng thái *</label>

                    <select id="status"
                            name="status"
                            required
                            aria-describedby="status-error"
                            aria-invalid="${not empty errors.status}">

                        <option value="1"
                                ${form.status == '1' ? 'selected' : ''}>
                            Hoạt động
                        </option>

                        <option value="0"
                                ${form.status == '0' ? 'selected' : ''}>
                            Ngừng hoạt động
                        </option>
                    </select>

                    <span id="status-error" class="field-error">
                        <c:out value="${errors.status}" />
                    </span>
                </div>

            </div>

            <div class="form-field">
                <label for="description">Mô tả</label>

                <textarea id="description"
                          name="description"
                          maxlength="500"
                          rows="5"
                          aria-describedby="description-error"
                          aria-invalid="${not empty errors.description}"><c:out value="${form.description}" /></textarea>

                <span id="description-error" class="field-error">
                    <c:out value="${errors.description}" />
                </span>
            </div>

            <div class="form-actions">
                <button type="submit" class="button">
                    ${empty productId ? 'Tạo sản phẩm' : 'Lưu thay đổi'}
                </button>

                <a class="button button-secondary"
                   href="${pageContext.request.contextPath}/admin/products">
                    Hủy
                </a>
            </div>

        </form>
    </div>

</main>

<footer class="site-footer">
    <div class="container">
        Họ tên: NGUYỄN DUY QUANG · MSSV: 23110290 · Mã đề: 05
    </div>
</footer>

</body>
</html>