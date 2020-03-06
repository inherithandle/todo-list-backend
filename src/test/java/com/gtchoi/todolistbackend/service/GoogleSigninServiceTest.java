package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.config.RestTemplateConfiguration;
import com.gtchoi.todolistbackend.model.GoogleTokenResponse;
import com.gtchoi.todolistbackend.model.GoogleUserResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@Import(value = {RestTemplateConfiguration.class})
public class GoogleSigninServiceTest {

    @TestConfiguration
    @TestPropertySource(value = "classpath:application.properties")
    static class ThisConfig {

        @Bean
        public GoogleSigninService googleSigninService() {
            return new GoogleSigninService();
        }
    }

    @Autowired
    GoogleSigninService googleSigninService;

    @Test
    public void constants() {
        assertThat(googleSigninService.getClientId(), is("768285404183-q27nonqv8mkccdfp5q4as2enfcpq4tdh.apps.googleusercontent.com"));
        assertThat(googleSigninService.getClientSecret(), is("Vw-xAUfxpfDaGVQbvSLyQWYM"));
    }

    @Test
    public void authCodeEndpoint() {
        final String AUTH_CODE = "4/vwGEdX7d54KLcvIeAE9I7m54qYs0kMIW3vMDqGZryxsawtpA4p6Km3K0Q_dBbHzCyeRFm6-NFeDUXP8vQZCpTRo";
        GoogleTokenResponse response = googleSigninService.callAuthCodeEndpoint(AUTH_CODE);

        assertThat(response.getAccessToken(), is(notNullValue()));
        assertThat(response.getExpiresIn(), greaterThan(0));
        assertThat(response.getTokenType(), is("Bearer"));
    }

    @Test
    public void userInfoEndpoint() {
        final String ACCESS_TOKEN = "ya29.ImC7B7yHz0Fy77vfGwUe8zjdrjrNQBeeGW0i8zMNgxG-Bal_ktwc-X0dfp-hkN-Xw1hJfRVw8StVJkgL7Ztwpm9Y-7Nj8-aEFQBUDRPhwNiyDcUdwLWBxldvGYvjQPmYaNk";
        GoogleUserResponse googleUserResponse = googleSigninService.callUserInfoEndpoint(ACCESS_TOKEN);

        assertThat(googleUserResponse.getEmail(), is("inherithandle@gmail.com"));
        assertThat(googleUserResponse.isEmailVerified(), is(true));
        assertThat(googleUserResponse.getSub(), is(notNullValue()));
        /*
            문서에 iat, at_hash 등 여러가지 프로퍼티가 있지만, 리턴받지 못하고 있다.
            expected response :
            {
              "sub": "114641801168891213434",
              "picture": "https://lh3.googleusercontent.com/a-/AAuE7mAmA7-_s4nmZsW66IRP8av2sLUci9CXhATLvyxO",
              "email": "inherithandle@gmail.com",
              "email_verified": true
            }
         */
    }

    @Test
    public void getGoogleEmail() {
        final String AUTH_CODE = "4/vwFeud87yx3XWfIIqmhMu_5tDAY4ShTcG5AmtsDXUWYM75zzM5_3jeqkwiAenAK_uCrlLZWKgS-mLRNqjIKEQ-U";
        String googleEmail = googleSigninService.getEmail(AUTH_CODE);

        assertThat(googleEmail, is("inherithandle@gmail.com"));
    }



}