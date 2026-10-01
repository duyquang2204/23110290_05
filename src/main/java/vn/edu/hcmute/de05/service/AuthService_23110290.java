package vn.edu.hcmute.de05.service;

import java.util.Map;

public interface AuthService_23110290 {

    void register(Map<String, String> form);

    void activate(String email, String otp);

    void resendOtp(String email);

    Map<String, Object> login(String username, String password);
}