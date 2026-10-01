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
        ${empty categoryId ? 'Thêm danh mục' : 'Sửa danh mục'}
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
            <a class="nav-link active"
               href="${pageContext.request.contextPath}/admin/categories">
                Danh mục
            </a>
            <a class="nav-link"
  				href="${pageContext.request.contextPath}/admin/products">
    			Quản lý sản phẩm
			</a>
        </nav>
    </div>
</header>

<main class="container main-content">

    <a class="back-link"
       href="${pageContext.request.contextPath}/admin/categories">
        ← Quay lại danh sách danh mục
    </a>

    <div class="page-heading">
        <p class="eyebrow">QUẢN TRỊ CỬA HÀNG</p>

        <h1>
            ${empty categoryId ? 'Thêm danh mục' : 'Sửa danh mục'}
        </h1>

        <p class="muted">
            Các trường có dấu * là bắt buộc.
        </p>
    </div>

    <div class="panel form-panel">

        <c:if test="${not empty errorMessage}">
            <p class="notice notice-error" role="alert">
                <c:out value="${errorMessage}" />
            </p>
        </c:if>

        <form method="post"
              action="${pageContext.request.contextPath}/admin/categories"
              novalidate>

            <input type="hidden"
                   name="action"
                   value="save">

            <input type="hidden"
                   name="id"
                   value="<c:out value='${categoryId}' />">

            <input type="hidden"
                   name="csrfToken"
                   value="<c:out value='${sessionScope.csrfToken}' />">

            <div class="form-field">
                <label for="categoryName">Tên danh mục *</label>

                <input type="text"
                       id="categoryName"
                       name="categoryName"
                       maxlength="200"
                       required
                       aria-describedby="categoryName-error"
                       aria-invalid="${not empty errors.categoryName}"
                       value="<c:out value='${form.categoryName}' />">

                <span id="categoryName-error" class="field-error">
                    <c:out value="${errors.categoryName}" />
                </span>
            </div>

            <div class="form-field">
                <label for="images">Đường dẫn ảnh JPG</label>

                <input type="text"
                       id="images"
                       name="images"
                       maxlength="500"
                       aria-describedby="images-help images-error"
                       aria-invalid="${not empty errors.images}"
                       placeholder="/assets/images/book-default.jpg"
                       value="<c:out value='${form.images}' />">

                <small id="images-help" class="muted">
                    Không bắt buộc. Ví dụ:
                    /assets/images/book-default.jpg
                </small>

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

            <div class="form-actions">
                <button type="submit" class="button">
                    ${empty categoryId ? 'Tạo danh mục' : 'Lưu thay đổi'}
                </button>

                <a class="button button-secondary"
                   href="${pageContext.request.contextPath}/admin/categories">
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