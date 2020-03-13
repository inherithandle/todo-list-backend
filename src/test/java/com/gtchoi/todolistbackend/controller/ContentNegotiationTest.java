package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.model.LoginDTO;
import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.repository.UserTokenRepository;
import com.gtchoi.todolistbackend.service.AccountService;
import com.gtchoi.todolistbackend.util.JaxbUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.bind.JAXBException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class ContentNegotiationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserTokenRepository userTokenRepository;

    LoginResponse loginResponse = new LoginResponse();
    LoginDTO loginDTO = new LoginDTO();
    JSONObject jsonBuilder = new JSONObject();

    @Before
    public void setup() throws JSONException, JAXBException {
        final String userId = "gtchoi";
        final String password = "password";
        loginResponse.setUserId(userId);
        loginResponse.setLogin(true);
        jsonBuilder.put("userId", userId);
        jsonBuilder.put("password", password);

        loginDTO.setUserId(userId);
        loginDTO.setPassword(password);
    }

    @Test
    public void json주고_xml받기() throws Exception {
        given(accountService.login(any(), any())).willReturn(loginResponse);

        String xmlString = mockMvc.perform(post("/login")
                .accept(MediaType.APPLICATION_XML)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
                .andReturn().getResponse().getContentAsString();

        System.out.println("response:");
        System.out.println(xmlString);

        LoginResponse unmarshal = (LoginResponse) JaxbUtil.unmarshal(xmlString, LoginResponse.class);

        assertThat(unmarshal.getUserId(), is("gtchoi"));
        assertThat(unmarshal.isLogin(), is(true));

    }

    @Test
    public void json주고_json받기() throws Exception {
        given(accountService.login(any(), any())).willReturn(loginResponse);

        mockMvc.perform(post("/login")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(jsonBuilder.toString()))
            .andExpect(jsonPath("$.login").value(true));
    }

    @Test
    public void xml주고_xml받기() throws Exception {

        given(accountService.login(any(), any())).willReturn(loginResponse);

        String xmlString = mockMvc.perform(post("/login")
                .accept(MediaType.APPLICATION_XML)
                .contentType(MediaType.APPLICATION_XML)
                .content(JaxbUtil.marshal(loginDTO, LoginDTO.class)))
                .andReturn().getResponse().getContentAsString();

        System.out.println("xmlString : " + xmlString);
        LoginResponse unmarshal = (LoginResponse) JaxbUtil.unmarshal(xmlString, LoginResponse.class);

        assertThat(unmarshal.getUserId(), is("gtchoi"));
        assertThat(unmarshal.isLogin(), is(true));


    }

    @Test
    public void xml주고_json받기() throws Exception {
        given(accountService.login(any(), any())).willReturn(loginResponse);

        mockMvc.perform(post("/login")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_XML)
                .content(JaxbUtil.marshal(loginDTO, LoginDTO.class)))
                .andExpect(jsonPath("$.login").value(true));
    }

    @Test
    public void favorQueryStringJson() throws Exception {
        // Accept header보다 query string format이 우선순위가 더 높은지 확인한다.
        given(accountService.login(any(), any())).willReturn(loginResponse);

        mockMvc.perform(post("/login?format=json")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_XML)
                .content(jsonBuilder.toString()))
                .andExpect(jsonPath("$.login").value(true));
    }

    @Test
    public void favorMediaQueryStringXml() throws Exception {
        // Accept header보다 query string format이 우선순위가 더 높은지 확인한다.
        given(accountService.login(any(), any())).willReturn(loginResponse);

        String xmlString = mockMvc.perform(post("/login?format=xml")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
                .andReturn().getResponse().getContentAsString();


        System.out.println("xmlString : " + xmlString);
        LoginResponse unmarshal = (LoginResponse) JaxbUtil.unmarshal(xmlString, LoginResponse.class);

        assertThat(unmarshal.getUserId(), is("gtchoi"));
        assertThat(unmarshal.isLogin(), is(true));
    }

    @Test
    public void favorPathExtensionJson() throws Exception {
        // Accept header보다 확장자가 우선순위가 더 높은지 확인한다.
        given(accountService.login(any(), any())).willReturn(loginResponse);

        mockMvc.perform(post("/login.json")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_XML)
                .content(jsonBuilder.toString()))
                .andExpect(jsonPath("$.login").value(true));
    }

    @Test
    public void favorPathExtensionXml() throws Exception {
        // Accept header보다 확장자가 우선순위가 더 높은지 확인한다.
        given(accountService.login(any(), any())).willReturn(loginResponse);

        String xmlString = mockMvc.perform(post("/login.xml")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
                .andReturn().getResponse().getContentAsString();


        System.out.println("xmlString : " + xmlString);
        LoginResponse unmarshal = (LoginResponse) JaxbUtil.unmarshal(xmlString, LoginResponse.class);

        assertThat(unmarshal.getUserId(), is("gtchoi"));
        assertThat(unmarshal.isLogin(), is(true));
    }

    @Test
    public void ppaStrategy() throws Exception {
        // PPA(Path, Parameter, Accept)가 동작하는지 확인한다. Path, Parameter, Accept header 순으로 우선순위를 갖는다.

        given(accountService.login(any(), any())).willReturn(loginResponse);

        String xmlString = mockMvc.perform(post("/login.xml?format=json")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBuilder.toString()))
                .andReturn().getResponse().getContentAsString();


        System.out.println("xmlString : " + xmlString);
        LoginResponse unmarshal = (LoginResponse) JaxbUtil.unmarshal(xmlString, LoginResponse.class);

        assertThat(unmarshal.getUserId(), is("gtchoi"));
        assertThat(unmarshal.isLogin(), is(true));
    }
}
