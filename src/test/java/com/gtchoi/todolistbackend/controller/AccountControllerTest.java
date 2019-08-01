package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.service.AccountService;
import com.gtchoi.todolistbackend.util.RandomUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * 쿠키가 제대로 주입되었는지 확인하려고 단위 테스트 만들었는데, 다시 쿠키를 안쓰기로 해서 필요가 없어짐..
 * 나중에 컨트롤러 테스트할 때, 참고용으로 확인하기 위해 삭제하지 않는다.
 * @throws Exception
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void loginSuccess() throws Exception {
        final String message = "logined. check out the access token on http only cookie.";
        final String accessToken = RandomUtil.generateString();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage(message);
        loginResponse.setLogin(true);
        loginResponse.setAccessToken(accessToken);

        given(accountService.login("joma", "jo")).willReturn(loginResponse);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity(
                        "userId", "joma",
                                "password", "jo")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(true))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.accessToken").value(accessToken));
    }

    @Test
    public void loginFailed() throws Exception {
        final String message = "User credentials are not valid.";
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage(message);
        loginResponse.setLogin(false);

        given(accountService.login("joma", "the wrong password")).willReturn(loginResponse);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity(
                        "userId", "joma",
                        "password", "the wrong password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(false))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(cookie().doesNotExist("access-token"));
    }

    @Test
    public void validToken() throws Exception {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage("success");
        loginResponse.setLogin(true);
        given(accountService.isValidAccessToken(any())).willReturn(loginResponse);

        Cookie tokenCookie = new Cookie("access-token", "abc");
        tokenCookie.setHttpOnly(true);
        mockMvc.perform(get("/token").cookie(tokenCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(true))
                .andExpect(jsonPath("$.message").value("success"));

    }

    @Test(expected = NestedServletException.class)
    public void clientShouldSendHttpOnlyCookie() throws Exception {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage("success");
        loginResponse.setLogin(false);
        given(accountService.isValidAccessToken(any())).willReturn(loginResponse);

        Cookie tokenCookie = new Cookie("access-token", "abc");

        mockMvc.perform(get("/token").cookie(tokenCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(true))
                .andExpect(jsonPath("$.message").value("success"));
    }

    /**
     * copied from https://stackoverflow.com/questions/36568518/testing-form-posts-through-mockmvc
     */
    private String buildUrlEncodedFormEntity(String... params) {
        if( (params.length % 2) > 0 ) {
            throw new IllegalArgumentException("Need to give an even number of parameters");
        }
        StringBuilder result = new StringBuilder();
        for (int i=0; i<params.length; i+=2) {
            if( i > 0 ) {
                result.append('&');
            }
            try {
                result.
                        append(URLEncoder.encode(params[i], StandardCharsets.UTF_8.name())).
                        append('=').
                        append(URLEncoder.encode(params[i+1], StandardCharsets.UTF_8.name()));
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return result.toString();
    }
}