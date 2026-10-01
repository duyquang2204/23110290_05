<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN" />
<!DOCTYPE html><html lang="vi"><head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Lịch sử đặt hàng</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head><body>
<header class="site-header"><div class="container header-inner">
<a class="brand" href="${pageContext.request.contextPath}/home">Sổ cửa hàng</a>
<nav><a class="nav-link" href="${pageContext.request.contextPath}/products">Sản phẩm</a><a class="nav-link" href="${pageContext.request.contextPath}/cart">Giỏ hàng</a><a class="nav-link active" href="${pageContext.request.contextPath}/orders">Lịch sử đơn hàng</a></nav>
</div></header>
<main class="container main-content">
<div class="page-heading"><p class="eyebrow">TÀI KHOẢN CỦA BẠN</p><h1>Lịch sử đặt hàng</h1><p class="muted">Theo dõi trạng thái và thông tin các đơn đã đặt.</p></div>
<section class="panel">
<form method="get" action="${pageContext.request.contextPath}/orders" class="form-actions">
<label for="status">Lọc theo trạng thái</label>
<select id="status" name="status"><option value="ALL" ${selectedStatus == 'ALL' ? 'selected' : ''}>Tất cả đơn hàng</option>
<c:forEach var="status" items="${statuses}"><option value="${status.code}" ${selectedStatus == status.code ? 'selected' : ''}><c:out value="${status.label}"/></option></c:forEach>
</select><button class="button button-secondary" type="submit">Lọc</button>
</form></section>
<c:choose><c:when test="${empty orders}"><section class="panel empty-state"><h2>Chưa có đơn hàng ở trạng thái này</h2><a class="button" href="${pageContext.request.contextPath}/products">Tiếp tục mua sắm</a></section></c:when>
<c:otherwise><c:forEach var="order" items="${orders}"><section class="panel order-card">
<div class="seller-heading"><div><p class="eyebrow">MÃ ĐƠN</p><h2><c:out value="${order.orderCode}"/></h2></div><strong><c:out value="${order.statusLabel}"/></strong></div>
<p>Ngày đặt: <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p>
<p>Thanh toán: <c:out value="${order.paymentMethod}"/> · COD</p>
<p>Người nhận: <c:out value="${order.customerName}"/> · <c:out value="${order.phone}"/></p>
<p>Địa chỉ: <c:out value="${order.shippingAddress}"/></p>
<h3>Sản phẩm</h3><ul><c:forEach var="item" items="${order.items}"><li><c:out value="${item.productName}"/> — <c:out value="${item.quantity}"/> × <fmt:formatNumber value="${item.unitPrice}" type="number" maxFractionDigits="0"/> ₫ = <fmt:formatNumber value="${item.lineTotal}" type="number" maxFractionDigits="0"/> ₫</li></c:forEach></ul>
<h3>Tổng đơn: <fmt:formatNumber value="${order.total}" type="number" maxFractionDigits="0"/> ₫</h3>
</section></c:forEach></c:otherwise></c:choose>
</main><footer class="site-footer"><div class="container">MSSV: 23110290 · Mã đề: 05</div></footer>
</body></html>
