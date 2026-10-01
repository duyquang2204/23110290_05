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

    <title>
        <c:out value="${product.productName}" /> — Chi tiết sản phẩm
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

            <nav aria-label="Điều hướng chính">

                <a class="nav-link"
                   href="${pageContext.request.contextPath}/home">
                    Trang chủ
                </a>

                <a class="nav-link active"
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

        <a class="back-link"
           href="${pageContext.request.contextPath}/products">
            ← Quay lại danh sách sản phẩm
        </a>

        <div class="page-heading">
            <p class="eyebrow">CHI TIẾT SẢN PHẨM</p>

            <h1>
                <c:out value="${product.productName}" />
            </h1>

            <p class="muted">
                Cửa hàng:
                <c:out value="${product.seller.sellername}" />

                · SellerID:
                <c:out value="${product.seller.sellerId}" />
            </p>
        </div>

        <c:choose>

            <c:when test="${empty product.images}">
                <c:url var="imageUrl"
                       value="/assets/images/book-default.jpg" />
            </c:when>

            <c:otherwise>
                <c:url var="imageUrl"
                       value="${product.images}" />
            </c:otherwise>

        </c:choose>

        <article class="panel product-detail">

            <img class="detail-image"
                 src="<c:out value='${imageUrl}' />"
                 alt="Ảnh minh họa sản phẩm">

            <div class="detail-content">

                <h2>
                    <c:out value="${product.productName}" />
                </h2>

                <div class="info-list">

                    <div>
                        <span class="muted">Mã sản phẩm</span>

                        <span class="record-code">
                            <c:out value="${product.productCode}" />
                        </span>
                    </div>

                    <div>
                        <span class="muted">Danh mục</span>

                        <span>
                            <c:out
                                value="${product.category.categoryName}" />
                        </span>
                    </div>

                    <div>
                        <span class="muted">Giá</span>

                        <strong class="product-price">
                            <fmt:formatNumber
                                value="${product.price}"
                                type="number"
                                maxFractionDigits="0" />
                            ₫
                        </strong>
                    </div>

                    <div><span class="muted">Còn lại</span><span><c:out value="${product.stock}"/> sản phẩm</span></div>

                    <div>
                        <span class="muted">Description</span>

                        <span class="description-text">
                            <c:out
                                value="${product.description}"
                                default="Chưa có mô tả." />
                        </span>
                    </div>

                </div>

            </div>

            <c:if test="${product.stock gt 0}">
                <form method="post" action="${pageContext.request.contextPath}/cart" class="form-actions">
                    <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}' />">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="${product.productId}">
                    <label>Số lượng <input type="number" name="quantity" min="1" max="${product.stock}" value="1" required></label>
                    <button class="button" type="submit">Thêm vào giỏ</button>
                </form>
            </c:if>
            <c:if test="${product.stock le 0}"><p class="muted">Tạm hết hàng</p></c:if>

        </article>

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
