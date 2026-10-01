package vn.edu.hcmute.de05.service;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailService_23110290 {

    public void sendOtp(String recipient, String otp) {
        String username = System.getenv("MAIL_USERNAME");
        String password = System.getenv("MAIL_PASSWORD");

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {

            throw new IllegalStateException(
                    "Chưa cấu hình MAIL_USERNAME và MAIL_PASSWORD.");
        }

        Properties properties = new Properties();

        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");
        properties.setProperty("mail.smtp.ssl.checkserveridentity", "true");

        properties.setProperty("mail.smtp.connectiontimeout", "10000");
        properties.setProperty("mail.smtp.timeout", "10000");
        properties.setProperty("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                            getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                username,
                                password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username));

            message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(recipient));

            message.setSubject(
                    "Mã kích hoạt tài khoản Sổ cửa hàng",
                    StandardCharsets.UTF_8.name());

            message.setText(
                    "Mã OTP của bạn là: " + otp
                            + "\n\nMã có hiệu lực trong 5 phút."
                            + "\nKhông chia sẻ mã này cho người khác."
                            + "\n\nSổ cửa hàng - MSSV 23110290 - Đề 05",
                    StandardCharsets.UTF_8.name());

            Transport.send(message);

        } catch (MessagingException exception) {
            throw new IllegalStateException(
                    "Không gửi được email OTP. Kiểm tra cấu hình SMTP.",
                    exception);
        }
    }
}