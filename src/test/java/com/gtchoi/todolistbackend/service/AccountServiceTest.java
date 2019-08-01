package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@Import(NecessaryBeanConfig.class)
public class AccountServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private User getUser() {
        final String userId = "gyutaechoi";
        final String password = "correct password";
        final String encrypted = passwordEncoder.encode(password);
        User user = new User();
        user.setUserId(userId);
        user.setPassword(encrypted);
        return user;
    }

    @Test
    public void loginSuccess() {
        User user = getUser();
        final String PASSWORD = "correct password";

        given(userRepository.findByUserId(user.getUserId())).willReturn(user);
        LoginResponse loginResponse = accountService.login(user.getUserId(), PASSWORD);

        assertThat(loginResponse.getAccessToken().length(), is(36));
        assertThat(loginResponse.getMessage().contains("access token"), is(true));
        assertThat(loginResponse.isLogin(), is(true));
    }

    @Test
    public void userIdNotFound() {
        User user = getUser();
        final String PASSWORD = "correct password";
        given(userRepository.findByUserId(user.getUserId())).willReturn(null);
        LoginResponse loginResponse = accountService.login(user.getUserId(), PASSWORD);

        assertThat(loginResponse.getAccessToken(), is(nullValue()));
        assertThat(loginResponse.getMessage().contains("are not valid"), is(true));
        assertThat(loginResponse.isLogin(), is(false));
    }

    @Test
    public void passwordIsWrong() {
        User user = getUser();
        final String WRONG_PASSWORD = "WRONG PASSWORD";

        given(userRepository.findByUserId(user.getUserId())).willReturn(user);
        LoginResponse loginResponse = accountService.login(user.getUserId(), WRONG_PASSWORD);

        assertThat(loginResponse.getAccessToken(), is(nullValue()));
        assertThat(loginResponse.getMessage().contains("are not valid"), is(true));
        assertThat(loginResponse.isLogin(), is(false));
    }

    @Test
    public void validAccessToken() {
        final String accessToken = "abc";
        LoginResponse loginResponse = accountService.isValidAccessToken(accessToken);

        assertThat(loginResponse.getMessage(), is("success"));
        assertThat(loginResponse.getAccessToken(), is(accessToken));
        assertThat(loginResponse.isLogin(), is(true));
    }

    @Test
    public void invalidAccessToken() {
        // TODO : 현재 실패.
        final String accessToken = "abc";
        LoginResponse loginResponse = accountService.isValidAccessToken(accessToken);

        assertThat(loginResponse.getAccessToken(), is(nullValue()));
        assertThat(loginResponse.isLogin(), is(false));
    }




}