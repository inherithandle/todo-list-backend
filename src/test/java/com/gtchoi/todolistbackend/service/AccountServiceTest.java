package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.config.RestTemplateConfiguration;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.entity.UserToken;
import com.gtchoi.todolistbackend.enums.TokenType;
import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.repository.UserRepository;
import com.gtchoi.todolistbackend.repository.UserTokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@Import(value = {NecessaryBeanConfig.class, RestTemplateConfiguration.class})
public class AccountServiceTest {

    @TestConfiguration
    static class ThisConfig {
        @Bean
        public AccountService accountService() {
            return new AccountService();
        }

        @Bean
        public GoogleSigninService googleSigninService() {
            return new GoogleSigninService();
        }
    }

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserTokenRepository userTokenRepository;

    @MockBean
    ModelMapper modelMapper;

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    final String ACCESS_TOKEN = "abc";

    @Test
    public void isNotNull() {
        assertThat(accountService, is(notNullValue()));
    }

    private User getUser() {
        final String userId = "gyutaechoi";
        final String password = "correct password";
        final String encrypted = passwordEncoder.encode(password);
        User user = new User();
        user.setUserId(userId);
        user.setPassword(encrypted);
        return user;
    }

    private UserToken getUserToken() {
        return new UserToken(ACCESS_TOKEN, getUser());
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
        given(userTokenRepository.findById(ACCESS_TOKEN)).willReturn(Optional.of(getUserToken()));
        LoginResponse loginResponse = accountService.isValidAccessToken(ACCESS_TOKEN);

        assertThat(loginResponse.getMessage(), is("success"));
        assertThat(loginResponse.getAccessToken(), is(ACCESS_TOKEN));
        assertThat(loginResponse.isLogin(), is(true));
    }

    @Test
    public void invalidAccessToken() {
        final String wrongAccessToken = "wrong-access-token";
        given(userTokenRepository.findById(wrongAccessToken)).willReturn(Optional.empty());
        LoginResponse loginResponse = accountService.isValidAccessToken(wrongAccessToken);

        assertThat(loginResponse.getAccessToken(), is(nullValue()));
        assertThat(loginResponse.isLogin(), is(false));
    }

    @Test(expected = ConstraintViolationException.class)
    public void methodValidator() {
        accountService.login("1abcddd", "adsfadfadf");
    }

    @Test
    public void loginWithThirdParty() {
        final String authCode = "4/vwF1-wOWowwgLZ2lsLWTGSYNHW1P6lPl5OKyKEhbVRHQbINWZc-G7uDBxc41KhJNsFLzGeN_boDf9tG1AfMT7ro";
        accountService.loginWithThirdParty(TokenType.GOOGLE, authCode);
    }
}