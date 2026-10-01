<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="title"
       value="${mode == 'register' ? 'Đăng ký tài khoản' : (mode == 'verify' ? 'Kích hoạt tài khoản' : 'Đăng nhập')}" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title><c:out value="${title}" /></title>

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

            <a class="nav-link"
               href="${pageContext.request.contextPath}/login">
                Đăng nhập
            </a>
        </nav>
    </div>
</header>

<main class="container main-content">

    <div class="auth-container">

        <div class="page-heading">
            <p class="eyebrow">TÀI KHOẢN CỬA HÀNG</p>
            <h1><c:out value="${title}" /></h1>
        </div>

        <div class="panel">

            <c:if test="${not empty notice}">
                <p class="notice notice-success" role="status">
                    <c:out value="${notice}" />
                </p>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <p class="notice notice-error" role="alert">
                    <c:out value="${errorMessage}" />
                </p>
            </c:if>

            <c:choose>

                <c:when test="${mode == 'register'}">

                    <form method="post"
                          action="${pageContext.request.contextPath}/register"
                          novalidate>

                        <input type="hidden"
                               name="csrfToken"
                               value="<c:out value='${sessionScope.csrfToken}' />">

                        <div class="form-field">
                            <label for="fullname">Họ tên *</label>

                            <input id="fullname"
                                   name="fullname"
                                   type="text"
                                   maxlength="50"
                                   autocomplete="name"
                                   value="<c:out value='${form.fullname}' />">

                            <span class="field-error">
                                <c:out value="${errors.fullname}" />
                            </span>
                        </div>

                        <div class="form-field">
                            <label for="username">Tên đăng nhập *</label>

                            <input id="username"
                                   name="username"
                                   type="text"
                                   maxlength="50"
                                   autocomplete="username"
                                   value="<c:out value='${form.username}' />">

                            <span class="field-error">
                                <c:out value="${errors.username}" />
                            </span>
                        </div>

                        <div class="form-field">
                            <label for="email">Email nhận OTP *</label>

                            <input id="email"
                                   name="email"
                                   type="email"
                                   maxlength="100"
                                   autocomplete="email"
                                   value="<c:out value='${form.email}' />">

                            <span class="field-error">
                                <c:out value="${errors.email}" />
                            </span>
                        </div>

                        <div class="form-field">
                            <label for="password">Mật khẩu *</label>

                            <input id="password"
                                   name="password"
                                   type="password"
                                   maxlength="128"
                                   autocomplete="new-password">

                            <span class="field-error">
                                <c:out value="${errors.password}" />
                            </span>
                        </div>

                        <div class="form-field">
                            <label for="confirmPassword">
                                Nhập lại mật khẩu *
                            </label>

                            <input id="confirmPassword"
                                   name="confirmPassword"
                                   type="password"
                                   maxlength="128"
                                   autocomplete="new-password">

                            <span class="field-error">
                                <c:out value="${errors.confirmPassword}" />
                            </span>
                        </div>

                        <button class="button" type="submit">
                            Đăng ký và gửi OTP
                        </button>
                    </form>

                    <p>
                        Đã có tài khoản?
                        <a href="${pageContext.request.contextPath}/login">
                            Đăng nhập
                        </a>
                    </p>

                    <a href="${pageContext.request.contextPath}/verify">
                        Đã đăng ký nhưng chưa kích hoạt?
                    </a>

                </c:when>

                <c:when test="${mode == 'verify'}">

                    <p class="muted">
                        Nhập mã 6 chữ số trong email.
                        Mã có hiệu lực 5 phút.
                    </p>

                    <form method="post"
                          action="${pageContext.request.contextPath}/verify"
                          novalidate>

                        <input type="hidden"
                               name="csrfToken"
                               value="<c:out value='${sessionScope.csrfToken}' />">

                        <div class="form-field">
                            <label for="email">Email đã đăng ký</label>

                            <input id="email"
                                   name="email"
                                   type="email"
                                   maxlength="100"
                                   value="<c:out value='${form.email}' />">

                            <span class="field-error">
                                <c:out value="${errors.email}" />
                            </span>
                        </div>

                        <div class="form-field">
                            <label for="otp">Mã OTP</label>

                            <input id="otp"
                                   name="otp"
                                   type="text"
                                   maxlength="6"
                                   inputmode="numeric"
                                   autocomplete="one-time-code">

                            <span class="field-error">
                                <c:out value="${errors.otp}" />
                            </span>
                        </div>

                        <div class="form-actions">
                            <button class="button"
                                    type="submit">
                                Kích hoạt
                            </button>

                            <button class="button button-secondary"
                                    type="submit"
                                    formaction="${pageContext.request.contextPath}/resend-otp">
                                Gửi lại OTP
                            </button>
                        </div>
                    </form>

                    <p>
                        <a href="${pageContext.request.contextPath}/login">
                            Quay lại đăng nhập
                        </a>
                    </p>

                </c:when>

                <c:otherwise>

                    <form method="post"
                          action="${pageContext.request.contextPath}/login">

                        <input type="hidden"
                               name="csrfToken"
                               value="<c:out value='${sessionScope.csrfToken}' />">

                        <div class="form-field">
                            <label for="username">Tên đăng nhập</label>

                            <input id="username"
                                   name="username"
                                   type="text"
                                   maxlength="50"
                                   autocomplete="username"
                                   required
                                   value="<c:out value='${form.username}' />">
                        </div>

                        <div class="form-field">
                            <label for="password">Mật khẩu</label>

                            <input id="password"
                                   name="password"
                                   type="password"
                                   maxlength="128"
                                   autocomplete="current-password"
                                   required>
                        </div>

                        <button class="button" type="submit">
                            Đăng nhập
                        </button>
                    </form>

                    <p>
                        Chưa có tài khoản?
                        <a href="${pageContext.request.contextPath}/register">
                            Đăng ký
                        </a>
                    </p>

                    <a href="${pageContext.request.contextPath}/verify">
                        Kích hoạt bằng OTP
                    </a>

                </c:otherwise>

            </c:choose>

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