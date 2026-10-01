package vn.edu.hcmute.de05.service.impl;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import vn.edu.hcmute.de05.entity.User_23110290;
import vn.edu.hcmute.de05.exception.ValidationException_23110290;
import vn.edu.hcmute.de05.repository.UserRepository_23110290;
import vn.edu.hcmute.de05.repository.impl.UserRepositoryImpl_23110290;
import vn.edu.hcmute.de05.service.AuthService_23110290;
import vn.edu.hcmute.de05.service.MailService_23110290;
import vn.edu.hcmute.de05.util.JpaUtil_23110290;
import vn.edu.hcmute.de05.util.PasswordUtil_23110290;

public class AuthServiceImpl_23110290
        implements AuthService_23110290 {

    private final UserRepository_23110290 repository =
            new UserRepositoryImpl_23110290();

    private final MailService_23110290 mailService =
            new MailService_23110290();

    @Override
    public void register(Map<String, String> form) {
        String username = text(form.get("username"));
        String fullname = text(form.get("fullname"));
        String email = normalizeEmail(form.get("email"));
        String password = form.getOrDefault("password", "");
        String confirm = form.getOrDefault("confirmPassword", "");

        Map<String, String> errors = new LinkedHashMap<>();

        if (!username.matches("[A-Za-z0-9_]{3,50}")) {
            errors.put(
                    "username",
                    "Tên đăng nhập gồm 3–50 chữ, số hoặc dấu gạch dưới.");
        }

        if (fullname.isEmpty() || fullname.length() > 50) {
            errors.put("fullname", "Họ tên cần từ 1 đến 50 ký tự.");
        }

        if (email.length() > 100
                || !email.matches(
                        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errors.put("email", "Email không hợp lệ.");
        }

        if (password.length() < 8 || password.length() > 128) {
            errors.put("password", "Mật khẩu cần từ 8 đến 128 ký tự.");
        }

        if (!Objects.equals(password, confirm)) {
            errors.put("confirmPassword", "Mật khẩu nhập lại không khớp.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException_23110290(errors);
        }

        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (repository.findByUsername(em, username).isPresent()) {
                errors.put("username", "Tên đăng nhập đã được sử dụng.");
            }

            if (repository.findByEmail(em, email, false).isPresent()) {
                errors.put(
                        "email",
                        "Email đã được sử dụng. "
                                + "Nếu chưa kích hoạt, mở trang xác thực OTP.");
            }

            if (!errors.isEmpty()) {
                throw new ValidationException_23110290(errors);
            }

            String otp = PasswordUtil_23110290.generateOtp();

            User_23110290 user = new User_23110290();

            user.setUsername(username);
            user.setFullname(fullname);
            user.setEmail(email);
            user.setPassword(
                    PasswordUtil_23110290.hashPassword(password));
            user.setRole(repository.findRole(em, "USER"));
            user.setStatus(0);
            user.setCode(createOtpState(email, otp));

            repository.persist(em, user);
            em.flush();

            mailService.sendOtp(email, otp);

            transaction.commit();

        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        } finally {
            em.close();
        }
    }

    @Override
    public void activate(String emailValue, String otpValue) {
        String email = normalizeEmail(emailValue);
        String otp = text(otpValue);
        String failure = null;

        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            User_23110290 user = repository
                    .findByEmail(em, email, true)
                    .orElse(null);

            if (user == null) {
                failure = "Email hoặc mã xác thực không hợp lệ.";

            } else if (Integer.valueOf(1).equals(user.getStatus())) {
                failure = "Tài khoản đã kích hoạt. Hãy đăng nhập.";

            } else {
                String[] state = parseOtpState(user.getCode());

                if (state == null) {
                    failure = "Không có OTP hợp lệ. Hãy gửi lại mã.";

                } else {
                    long expiresAt = Long.parseLong(state[0]);
                    int attempts = Integer.parseInt(state[2]);
                    long now = Instant.now().getEpochSecond();

                    if (now >= expiresAt) {
                        failure = "OTP đã hết hạn. Hãy gửi lại mã.";

                    } else if (attempts >= 5) {
                        failure = "Đã nhập sai 5 lần. Hãy gửi lại mã.";

                    } else {
                        String submittedDigest =
                                PasswordUtil_23110290.otpDigest(email, otp);

                        boolean matches = otp.matches("[0-9]{6}")
                                && PasswordUtil_23110290.sameDigest(
                                        state[1],
                                        submittedDigest);

                        if (matches) {
                            user.setStatus(1);
                            user.setCode(null);
                        } else {
                            attempts++;

                            user.setCode(
                                    state[0] + ":" + state[1] + ":" + attempts);

                            failure = "OTP không đúng. Còn "
                                    + (5 - attempts) + " lần thử.";
                        }
                    }
                }
            }

            transaction.commit();

        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        } finally {
            em.close();
        }

        if (failure != null) {
            throw new ValidationException_23110290(
                    Map.of("otp", failure));
        }
    }

    @Override
    public void resendOtp(String emailValue) {
        String email = normalizeEmail(emailValue);

        EntityManager em = JpaUtil_23110290.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            User_23110290 user = repository
                    .findByEmail(em, email, true)
                    .orElseThrow(() -> new ValidationException_23110290(
                            Map.of("email", "Không tìm thấy tài khoản.")));

            if (Integer.valueOf(1).equals(user.getStatus())) {
                throw new ValidationException_23110290(
                        Map.of("email", "Tài khoản đã kích hoạt."));
            }

            String[] state = parseOtpState(user.getCode());
            long now = Instant.now().getEpochSecond();

            if (state != null
                    && Long.parseLong(state[0]) - now > 240) {

                throw new ValidationException_23110290(
                        Map.of(
                                "email",
                                "Vui lòng chờ ít nhất 60 giây giữa hai lần gửi."));
            }

            String otp = PasswordUtil_23110290.generateOtp();

            user.setCode(createOtpState(email, otp));
            mailService.sendOtp(email, otp);

            transaction.commit();

        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        } finally {
            em.close();
        }
    }

    @Override
    public Map<String, Object> login(String usernameValue, String password) {
        String username = text(usernameValue);

        if (password == null || password.length() > 128) {
            throw new IllegalArgumentException(
                    "Tên đăng nhập hoặc mật khẩu không đúng.");
        }

        EntityManager em = JpaUtil_23110290.createEntityManager();

        try {
            User_23110290 user = repository
                    .findByUsername(em, username)
                    .orElse(null);

            if (user == null
                    || !PasswordUtil_23110290.verifyPassword(
                            password,
                            user.getPassword())) {

                throw new IllegalArgumentException(
                        "Tên đăng nhập hoặc mật khẩu không đúng.");
            }

            if (!Integer.valueOf(1).equals(user.getStatus())) {
                throw new IllegalArgumentException(
                        "Tài khoản chưa kích hoạt. Hãy xác thực OTP.");
            }

            String role = user.getRole().getRoleName();

            if (!"USER".equals(role)
                    && !"SELLER".equals(role)
                    && !"ADMIN".equals(role)) {

                throw new IllegalArgumentException(
                        "Vai trò tài khoản không hợp lệ.");
            }

            if ("SELLER".equals(role) && user.getSeller() == null) {
                throw new IllegalArgumentException(
                        "Tài khoản Seller chưa được gán cửa hàng.");
            }

            Map<String, Object> principal = new LinkedHashMap<>();

            principal.put("userId", user.getUserId());
            principal.put("username", user.getUsername());
            principal.put("fullname", user.getFullname());
            principal.put("role", role);

            if (user.getSeller() != null) {
                principal.put(
                        "sellerId",
                        user.getSeller().getSellerId());
            }

            return principal;

        } finally {
            em.close();
        }
    }

    private String createOtpState(String email, String otp) {
        long expiresAt = Instant.now().getEpochSecond() + 300;

        return expiresAt
                + ":" + PasswordUtil_23110290.otpDigest(email, otp)
                + ":0";
    }

    private String[] parseOtpState(String value) {
        if (value == null) {
            return null;
        }

        String[] parts = value.split(":");

        if (parts.length != 3) {
            return null;
        }

        try {
            Long.parseLong(parts[0]);
            int attempts = Integer.parseInt(parts[2]);

            if (!parts[1].matches("[0-9a-f]{32}")
                    || attempts < 0 || attempts > 5) {
                return null;
            }

            return parts;

        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String normalizeEmail(String value) {
        return text(value).toLowerCase(Locale.ROOT);
    }

    private String text(String value) {
        return value == null ? "" : value.trim();
    }
}