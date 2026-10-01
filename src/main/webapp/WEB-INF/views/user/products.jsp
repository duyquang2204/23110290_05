<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="vi_VN" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Sản phẩm theo cửa hàng</title>

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

                <a class="nav-link active"
                   aria-current="page"
                   href="${pageContext.request.contextPath}/products">
                    Sản phẩm
                </a>
                <c:if test="${sessionScope.currentUser.role == 'USER'}">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cart">Giỏ hàng</a>
                </c:if>
            </nav>

        </div>
    </header>

    <main class="container main-content">

        <div class="page-heading">
            <p class="eyebrow">DANH MỤC SẢN PHẨM</p>
            <h1>Sản phẩm theo cửa hàng</h1>

            <p class="muted">
                Chọn tên sản phẩm để xem thông tin chi tiết.
            </p>
        </div>

        <c:choose>
            <c:when test="${empty productGroups}">
                <div class="panel empty-state">
                    <h2>Chưa có sản phẩm</h2>
                    <p class="muted">
                        Danh sách sẽ hiển thị khi có dữ liệu sản phẩm.
                    </p>
                </div>
            </c:when>

            <c:otherwise>
                <c:forEach var="group" items="${productGroups}">

                    <section class="seller-section">

                        <div class="seller-heading">
                            <div>
                                <p class="eyebrow">
                                    MÃ CỬA HÀNG:
                                    <span class="record-code">
                                        <c:out value="${group.key}" />
                                    </span>
                                </p>

                                <h2>
                                    <c:out
                                        value="${group.value[0].seller.sellername}" />
                                </h2>
                            </div>

                            <span class="muted">
                                <c:out value="${group.value.size()}" />
                                sản phẩm
                            </span>
                        </div>

                        <div class="product-list">
                            <c:forEach var="item" items="${group.value}">

                                <c:url var="detailUrl"
                                       value="/product-detail">
                                    <c:param name="id"
                                             value="${item.productId}" />
                                </c:url>

                                <c:choose>
                                    <c:when test="${empty item.images}">
                                        <c:url var="imageUrl"
                                               value="/assets/images/book-default.svg" />
                                    </c:when>

                                    <c:otherwise>
                                        <c:url var="imageUrl"
                                               value="${item.images}" />
                                    </c:otherwise>
                                </c:choose>

                                <article class="product-row">

                                    <img class="product-image"
                                         src="<c:out value='${imageUrl}' />"
                                         alt="Ảnh minh họa sản phẩm">

                                    <div class="product-content">

                                        <h3 class="product-title">
                                            <a href="<c:out value='${detailUrl}' />">
                                                <c:out
                                                    value="${item.productName}" />
                                            </a>
                                        </h3>

                                        <div class="product-meta">

                                            <div>
                                                <span class="muted">
                                                    Mã sản phẩm:
                                                </span>

                                                <span class="record-code">
                                                    <c:out
                                                        value="${item.productCode}" />
                                                </span>
                                            </div>

                                            <div>
                                                <span class="muted">
                                                    Danh mục:
                                                </span>

                                                <span>
                                                    <c:out
                                                        value="${item.category.categoryName}" />
                                                </span>
                                            </div>

                                            <div>
                                                <span class="muted">
                                                    Giá:
                                                </span>

                                                <strong class="product-price">
                                                    <fmt:formatNumber
                                                        value="${item.price}"
                                                        type="number"
                                                        maxFractionDigits="0" />
                                                    ₫
                                                </strong>
                                            </div>

                                            <div>
                                                <span class="muted">
                                                    Còn lại:
                                                </span>

                                                <span>
                                                    <c:out
                                                        value="${item.stock}" />
                                                </span>
                                            </div>

                                        </div>

                                        <c:if test="${item.stock gt 0}">
                                            <form method="post" action="${pageContext.request.contextPath}/cart" class="form-actions">
                                                <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}' />">
                                                <input type="hidden" name="action" value="add">
                                                <input type="hidden" name="productId" value="${item.productId}">
                                                <label>Số lượng <input type="number" name="quantity" min="1" max="${item.stock}" value="1" required></label>
                                                <button class="button button-small" type="submit">Thêm vào giỏ</button>
                                            </form>
                                        </c:if>
                                        <c:if test="${item.stock le 0}"><p class="muted">Tạm hết hàng</p></c:if>

                                    </div>
                                </article>

                            </c:forEach>
                        </div>

                    </section>

                </c:forEach>
            </c:otherwise>
        </c:choose>

    </main>

    <footer class="site-footer">
        <div class="container">
            Họ tên: NGUYỄN DUY QUANG
            · MSSV: 23110290
            · Mã đề: 05
        </div>
    </footer>

</body>
</html>
