<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Quản lý danh mục</title>

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
               href="${pageContext.request.contextPath}/products">
                Sản phẩm
            </a>

            <a class="nav-link active"
               aria-current="page"
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

    <div class="page-heading page-heading-actions">
        <div>
            <p class="eyebrow">QUẢN TRỊ CỬA HÀNG</p>
            <h1>Danh mục sản phẩm</h1>

            <p class="muted">
                Tổng số:
                <c:out value="${totalItems}" />
                danh mục.
            </p>
        </div>

        <a class="button"
           href="${pageContext.request.contextPath}/admin/categories?action=new">
            Thêm danh mục
        </a>
    </div>

    <c:if test="${not empty successMessage}">
        <p class="notice notice-success" role="status">
            <c:out value="${successMessage}" />
        </p>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <p class="notice notice-error" role="alert">
            <c:out value="${errorMessage}" />
        </p>
    </c:if>

    <div class="panel table-panel">
        <div class="table-scroll">
            <table class="data-table">
                <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Tên danh mục</th>
                        <th scope="col">Trạng thái</th>
                        <th scope="col">Thao tác</th>
                    </tr>
                </thead>

                <tbody>
                    <c:choose>
                        <c:when test="${empty categories}">
                            <tr>
                                <td colspan="4" class="empty-cell">
                                    Chưa có danh mục nào.
                                </td>
                            </tr>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="item" items="${categories}">
                                <tr>
                                    <td class="record-code">
                                        <c:out value="${item.categoryId}" />
                                    </td>

                                    <td>
                                        <c:out value="${item.categoryName}" />
                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${item.status == 1}">
                                                <span class="status-label">
                                                    Hoạt động
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="muted">
                                                    Ngừng hoạt động
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <div class="row-actions">
                                            <a class="button button-secondary button-small"
                                               href="${pageContext.request.contextPath}/admin/categories?action=edit&amp;id=${item.categoryId}">
                                                Sửa
                                            </a>

                                            <form method="post"
                                                  action="${pageContext.request.contextPath}/admin/categories"
                                                  onsubmit="return confirm('Bạn có chắc muốn xóa danh mục này?');">

                                                <input type="hidden"
                                                       name="action"
                                                       value="delete">

                                                <input type="hidden"
                                                       name="id"
                                                       value="${item.categoryId}">

                                                <input type="hidden"
                                                       name="csrfToken"
                                                       value="<c:out value='${sessionScope.csrfToken}' />">

                                                <button class="button button-danger button-small"
                                                        type="submit">
                                                    Xóa
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <div class="pagination-bar">
            <span class="muted">
                Trang <c:out value="${currentPage}" />
                / <c:out value="${totalPages}" />
            </span>

            <nav class="pagination" aria-label="Phân trang danh mục">

                <c:if test="${currentPage > 1}">
                    <a class="button button-secondary button-small"
                       href="${pageContext.request.contextPath}/admin/categories?page=${currentPage - 1}">
                        Trang trước
                    </a>
                </c:if>

                <c:if test="${currentPage < totalPages}">
                    <a class="button button-secondary button-small"
                       href="${pageContext.request.contextPath}/admin/categories?page=${currentPage + 1}">
                        Trang sau
                    </a>
                </c:if>

            </nav>
        </div>
    </div>

</main>

<footer class="site-footer">
    <div class="container">
        Họ tên: NGUYỄN DUY QUANG · MSSV: 23110290 · Mã đề: 05
    </div>
</footer>

</body>
</html>