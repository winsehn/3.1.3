package web.model.auth;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Password {
    private final String encodedPassword;

    public Password(String rawPassword, PasswordEncoder passwordEncoder) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("rawPassword is null or empty");
        }
        this.encodedPassword = passwordEncoder.encode(rawPassword);
    }

    public Password(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }
}
