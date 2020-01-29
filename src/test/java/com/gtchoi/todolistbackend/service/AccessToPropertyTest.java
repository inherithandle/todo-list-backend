package com.gtchoi.todolistbackend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties", "classpath:application-private.properties"})
public class AccessToPropertyTest {

    @Value("${google.oauth.client-id}")
    private String clientId;

    @Value("${allowed.origins}")
    private String allowedOrigins;

    @TestConfiguration
    static class ThisConfig {

        @Bean
        public GoogleSigninService googleSigninService() {
            return new GoogleSigninService();
        }
    }

    @Autowired
    private GoogleSigninService googleSigninService;

    @MockBean
    RestTemplate restTemplate;

    /*
        application.properties 파일이 private.properties를 include하기 위해서
        반드시 TestPropertySource에 private.properties를 명시해준다.
     */
    @Test
    public void propertyTest() {
        assertThat(allowedOrigins, is("http://localhost:8080"));
        assertThat(clientId, is("768285404183-q27nonqv8mkccdfp5q4as2enfcpq4tdh.apps.googleusercontent.com"));
        assertThat(googleSigninService.getClientId(), is("768285404183-q27nonqv8mkccdfp5q4as2enfcpq4tdh.apps.googleusercontent.com"));
    }
}
