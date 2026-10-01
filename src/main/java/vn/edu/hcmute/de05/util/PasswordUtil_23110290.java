package vn.edu.hcmute.de05.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil_23110290 {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int ITERATIONS = 210000;

    private PasswordUtil_23110290() {
    }

    public static String hashPassword(String password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);

        byte[] hash = derive(password, salt, ITERATIONS);

        return ITERATIONS
                + ":" + Base64.getEncoder().encodeToString(salt)
                + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(
            String password,
            String storedValue) {

        if (password == null || storedValue == null) {
            return false;
        }

        try {
            String[] parts = storedValue.split(":");

            if (parts.length != 3) {
                return false;
            }

            int iterations = Integer.parseInt(parts[0]);

            if (iterations < 10000 || iterations > 1000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expected = Base64.getDecoder().decode(parts[2]);
            byte[] actual = derive(password, salt, iterations);

            return MessageDigest.isEqual(expected, actual);

        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public static String generateOtp() {
        return String.format(
                Locale.ROOT,
                "%06d",
                RANDOM.nextInt(1000000));
    }

    public static String otpDigest(String email, String otp) {
        try {
            byte[] digest = MessageDigest
                    .getInstance("SHA-256")
                    .digest(
                            (email + ":" + otp)
                                    .getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(digest, 0, 16);

        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public static boolean sameDigest(String first, String second) {
        return MessageDigest.isEqual(
                first.getBytes(StandardCharsets.UTF_8),
                second.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] derive(
            String password,
            byte[] salt,
            int iterations) {

        PBEKeySpec specification = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                256);

        try {
            return SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(specification)
                    .getEncoded();

        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException(
                    "Không thể băm mật khẩu.",
                    exception);
        } finally {
            specification.clearPassword();
        }
    }
}