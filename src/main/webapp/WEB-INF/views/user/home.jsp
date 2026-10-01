<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title><c:out value="${pageTitle}" /></title>

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

        <nav aria-label="Điều hướng chính">

            <a class="nav-link"
               href="${pageContext.request.contextPath}/home">
                Trang chủ
            </a>

            <a class="nav-link"
               href="${pageContext.request.contextPath}/products">
                Sản phẩm
            </a>

            <c:if test="${sessionScope.currentUser.role == 'USER'}">
                <a class="nav-link" href="${pageContext.request.contextPath}/cart">Giỏ hàng</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/orders">Lịch sử đơn hàng</a>
            </c:if>

            <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                <a class="nav-link"
                   href="${pageContext.request.contextPath}/admin/home">
                    Trang quản trị
                </a>
            </c:if>

            <c:choose>
                <c:when test="${empty sessionScope.currentUser}">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/login">
                        Đăng nhập
                    </a>
                </c:when>

                <c:otherwise>
                    <form method="post"
                          action="${pageContext.request.contextPath}/logout">

                        <input type="hidden"
                               name="csrfToken"
                               value="<c:out value='${sessionScope.csrfToken}' />">

                        <button type="submit"
                                class="button button-secondary button-small">
                            Đăng xuất
                        </button>
                    </form>
                </c:otherwise>
            </c:choose>

        </nav>
    </div>
</header>

<main class="container main-content">

    <div class="page-heading">
        <p class="eyebrow">SỔ CỬA HÀNG · ĐỀ 05</p>
        <h1><c:out value="${pageTitle}" /></h1>
    </div>

    <section class="panel">

        <c:choose>
            <c:when test="${empty sessionScope.currentUser}">
                <h2>Danh mục và sản phẩm của các cửa hàng</h2>

                <p class="muted">
                    Xem sản phẩm hoặc đăng ký tài khoản để bắt đầu.
                </p>

                <div class="form-actions">
                    <a class="button"
                       href="${pageContext.request.contextPath}/products">
                        Xem sản phẩm
                    </a>

                    <a class="button button-secondary"
                       href="${pageContext.request.contextPath}/register">
                        Đăng ký
                    </a>
                </div>
            </c:when>

            <c:otherwise>
                <h2>
                    Xin chào,
                    <c:out value="${sessionScope.currentUser.fullname}" />
                </h2>

                <p>
                    Tài khoản:
                    <span class="record-code">
                        <c:out value="${sessionScope.currentUser.username}" />
                    </span>
                </p>

                <c:choose>
                    <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
                        <p class="muted">
                            Quản lý danh mục và sản phẩm của hệ thống.
                        </p>

                        <div class="form-actions">
                            <a class="button"
                               href="${pageContext.request.contextPath}/admin/categories">
                                Quản lý danh mục
                            </a>

                            <a class="button button-secondary"
                               href="${pageContext.request.contextPath}/admin/products">
                                Quản lý sản phẩm
                            </a>
                        </div>
                    </c:when>

                    <c:when test="${sessionScope.currentUser.role == 'SELLER'}">
                        <p>
                            Mã cửa hàng:
                            <span class="record-code">
                                <c:out value="${sessionScope.currentUser.sellerId}" />
                            </span>
                        </p>

                        <a class="button"
                           href="${pageContext.request.contextPath}/products">
                            Xem sản phẩm các cửa hàng
                        </a>
                    </c:when>

                    <c:otherwise>
                        <p class="muted">
                            Tài khoản đã kích hoạt thành công.
                        </p>

                        <div class="form-actions">
                            <a class="button" href="${pageContext.request.contextPath}/products">Xem sản phẩm</a>
                            <a class="button button-secondary" href="${pageContext.request.contextPath}/orders">Lịch sử đơn hàng</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>

    </section>

</main>

<footer class="site-footer">
    <div class="container">
        Họ tên: NGUYỄN DUY QUANG · MSSV: 23110290 · Mã đề: 05
    </div>
</footer>

</body>
</html>
