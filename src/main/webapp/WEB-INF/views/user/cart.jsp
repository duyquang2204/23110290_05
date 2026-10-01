<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN" />
<!DOCTYPE html><html lang="vi"><head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Giỏ hàng</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head><body>
<header class="site-header"><div class="container header-inner">
<a class="brand" href="${pageContext.request.contextPath}/home">Sổ cửa hàng</a>
<nav><a class="nav-link" href="${pageContext.request.contextPath}/products">Sản phẩm</a><a class="nav-link active" href="${pageContext.request.contextPath}/cart">Giỏ hàng</a><a class="nav-link" href="${pageContext.request.contextPath}/orders">Lịch sử đơn hàng</a></nav>
</div></header>
<main class="container main-content">
<div class="page-heading"><p class="eyebrow">ĐƠN HÀNG CỦA BẠN</p><h1>Giỏ hàng</h1></div>
<c:if test="${not empty cartNotice}"><p class="notice notice-success"><c:out value="${cartNotice}"/></p></c:if>
<c:if test="${not empty cartError}"><p class="notice notice-error"><c:out value="${cartError}"/></p></c:if>
<c:choose><c:when test="${empty cartItems}">
<section class="panel empty-state"><h2>Giỏ hàng đang trống</h2><a class="button" href="${pageContext.request.contextPath}/products">Chọn sản phẩm</a></section>
</c:when><c:otherwise>
<section class="panel"><div class="product-list">
<c:forEach var="item" items="${cartItems}">
<article class="product-row">
<c:choose><c:when test="${empty item.image}"><c:url var="img" value="/assets/images/book-default.jpg"/></c:when><c:otherwise><c:url var="img" value="${item.image}"/></c:otherwise></c:choose>
<img class="product-image" src="<c:out value='${img}'/>" alt="Ảnh sản phẩm">
<div class="product-content"><h2 class="product-title"><c:out value="${item.productName}"/></h2>
<p>Đơn giá: <strong><fmt:formatNumber value="${item.unitPrice}" type="number" maxFractionDigits="0"/> ₫</strong></p>
<p>Còn lại: <c:out value="${item.stock}"/> sản phẩm</p>
<form method="post" action="${pageContext.request.contextPath}/cart" class="form-actions">
<input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>"><input type="hidden" name="action" value="update"><input type="hidden" name="cartItemId" value="${item.cartItemId}">
<label>Số lượng <input type="number" name="quantity" min="1" max="${item.stock}" value="${item.quantity}" required></label><button class="button button-secondary" type="submit">Cập nhật</button>
</form>
<p>Thành tiền: <strong><fmt:formatNumber value="${item.lineTotal}" type="number" maxFractionDigits="0"/> ₫</strong></p>
<form method="post" action="${pageContext.request.contextPath}/cart"><input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>"><input type="hidden" name="action" value="remove"><input type="hidden" name="cartItemId" value="${item.cartItemId}"><button class="button button-secondary" type="submit">Xóa</button></form>
</div></article></c:forEach>
</div><hr><h2>Tổng cộng: <fmt:formatNumber value="${cartTotal}" type="number" maxFractionDigits="0"/> ₫</h2></section>
<section class="panel"><h2>Thông tin nhận hàng</h2><p class="muted">Thanh toán tiền mặt khi nhận hàng (COD).</p>
<form method="post" action="${pageContext.request.contextPath}/checkout">
<input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
<div class="form-field"><label for="customerName">Họ tên người nhận *</label><input id="customerName" name="customerName" maxlength="100" required value="<c:out value='${sessionScope.currentUser.fullname}'/>" autocomplete="name"></div>
<div class="form-field"><label for="phone">Số điện thoại *</label><input id="phone" name="phone" maxlength="20" required autocomplete="tel" pattern="[0-9+() .-]{9,20}"></div>
<div class="form-field"><label for="address">Địa chỉ nhận hàng *</label><textarea id="address" name="address" maxlength="300" required autocomplete="street-address"></textarea></div>
<button class="button" type="submit">Đặt hàng và thanh toán COD</button>
</form></section>
</c:otherwise></c:choose>
</main><footer class="site-footer"><div class="container">MSSV: 23110290 · Mã đề: 05</div></footer>
</body></html>
