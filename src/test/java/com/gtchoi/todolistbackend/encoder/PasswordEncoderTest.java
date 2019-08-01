package com.gtchoi.todolistbackend.encoder;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.TestCase.assertTrue;


public class PasswordEncoderTest {
    @Test
    public void encoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
        String rawPassword = "123456";
        for (int i = 0; i < 5; i++) {
            String encodedPasswordFromForm = encoder.encode("123456");
            assertTrue(encoder.matches(rawPassword, encodedPasswordFromForm));
        }

    }

    @Test
    public void test2() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
        boolean matches = encoder.matches("ma", "$2a$11$qk30XEzncfCSRtz/KPKYceTFabDJD1WG8FC99yQ6Dz./P2edZ3zeS");
        assertTrue(matches);

    }

    @Test
    public void test3() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
        String ma = encoder.encode("ma");
        System.out.println(ma);
    }
}
