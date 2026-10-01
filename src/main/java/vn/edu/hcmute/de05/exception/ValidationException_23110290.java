package vn.edu.hcmute.de05.exception;

import java.util.LinkedHashMap;
import java.util.Map;

public class ValidationException_23110290 extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Map<String, String> errors;

    public ValidationException_23110290(Map<String, String> errors) {
        super("Dữ liệu nhập chưa hợp lệ.");
        this.errors = new LinkedHashMap<>(errors);
    }

    public Map<String, String> getErrors() {
        return new LinkedHashMap<>(errors);
    }
}