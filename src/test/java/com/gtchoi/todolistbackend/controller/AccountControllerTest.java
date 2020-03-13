package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.entity.UserToken;
import com.gtchoi.todolistbackend.enums.TokenType;
import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.repository.UserTokenRepository;
import com.gtchoi.todolistbackend.service.AccountService;
import com.gtchoi.todolistbackend.util.RandomUtil;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.Cookie;
import java.nio.charset.Charset;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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

    @MockBean
    private UserTokenRepository userTokenRepository;

    @Test
    public void loginSuccess() throws Exception {
        final String message = "logined. check out the access token on http only cookie.";
        final String accessToken = RandomUtil.generateString();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage(message);
        loginResponse.setLogin(true);
        loginResponse.setAccessToken(accessToken);

        JSONObject jsonBuilder = new JSONObject();
        jsonBuilder.put("userId", "joma");
        jsonBuilder.put("password", "jo");

        given(accountService.login("joma", "jo")).willReturn(loginResponse);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
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
        JSONObject jsonBuilder = new JSONObject();
        jsonBuilder.put("userId", "joma");
        jsonBuilder.put("password", "the wrong password");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
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

        UserToken userToken = new UserToken();
        userToken.setUser(new User());
        given(userTokenRepository.findById("abc")).willReturn(Optional.of(userToken));

        Cookie tokenCookie = new Cookie("access-token", "abc");
        mockMvc.perform(get("/token").cookie(tokenCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(true))
                .andExpect(jsonPath("$.message").value("success"));

    }

    @Test
    public void userDoesntHaveAccessTokenInCookie() throws Exception {
        mockMvc.perform(get("/token"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void signupValidationSuccess() throws Exception {
        JSONObject jsonBuilder = new JSONObject();
        jsonBuilder.put("userId", "joma");
        jsonBuilder.put("password", "jomajoma");
        jsonBuilder.put("confirmPassword", "jomajoma");

        MvcResult mvcResult = mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
                .andExpect(status().isOk()).andReturn();

        System.out.println(mvcResult.getRequest().getHeader("Accept"));
        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    public void missingJsonHeader() throws Exception {
        JSONObject jsonBuilder = new JSONObject();
        jsonBuilder.put("userId", "joma");
        jsonBuilder.put("password", "jomajoma");
        jsonBuilder.put("confirmPassword", "jomajoma");

        mockMvc.perform(post("/signup")
                .content(jsonBuilder.toString()))
                .andExpect(status().isUnsupportedMediaType()).andReturn();
    }

    @Test
    public void singupIdValidationFailed() throws Exception {
        JSONObject jsonBuilder = new JSONObject();
        jsonBuilder.put("userId", "jo");
        jsonBuilder.put("password", "jomajoma");
        jsonBuilder.put("confirmPassword", "jomajoma");

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void singupIdValidationFailed2() throws Exception {
        JSONObject jsonBuilder = new JSONObject();
        jsonBuilder.put("userId", "1joma");
        jsonBuilder.put("password", "jomajoma");
        jsonBuilder.put("confirmPassword", "jomajoma");

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void passwordsNotMatch() throws Exception {
        JSONObject jsonBuilder = new JSONObject();
        jsonBuilder.put("userId", "joma");
        jsonBuilder.put("password", "jomajoma");
        jsonBuilder.put("confirmPassword", "1234");

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 유효하지않은토큰_401_응답() throws Exception {
        final String bearerToken = "bad-access-token";

        given(userTokenRepository.findById(bearerToken)).willReturn(Optional.empty());

        mockMvc.perform(get("/token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 유효토큰_200_OK() throws Exception {
        final String bearerToken = "access-token";
        UserToken mockUserToken = new UserToken();
        mockUserToken.setUser(new User());
        given(userTokenRepository.findById(bearerToken)).willReturn(Optional.of(mockUserToken));

        mockMvc.perform(get("/token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk());
    }

    @Test
    public void getBearer() {
        assertThat("bearer access-token".substring(7), is("access-token"));
    }

    @Test
    public void 유효하지않은_구글_auth_code_400_응답() throws Exception {
        final String mockAuthCode = "mock-auth-code";
        JSONObject jsonBuilder = new JSONObject();
        jsonBuilder.put("authorizationCode", mockAuthCode);
        jsonBuilder.put("tokenType", "GOOGLE");


        given(accountService.loginWithThirdParty(TokenType.GOOGLE, mockAuthCode)).willThrow(
                HttpClientErrorException.BadRequest.create(HttpStatus.BAD_REQUEST, "hello", null, null, Charset.defaultCharset())
        );
        mockMvc.perform(post("/login-with-third-party")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void hello() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get("/account-test")
                .accept(MediaType.APPLICATION_XML)).andReturn().getResponse();

        System.out.println("hello xml");
        System.out.println(response.getContentAsString());

        response = mockMvc.perform(get("/account-test")
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn().getResponse();


        System.out.println("hello json");
        System.out.println(response.getContentAsString());
    }

}