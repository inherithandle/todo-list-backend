package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.entity.UserToken;
import com.gtchoi.todolistbackend.model.TodoDTO;
import com.gtchoi.todolistbackend.repository.UserTokenRepository;
import com.gtchoi.todolistbackend.service.TodoService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @MockBean
    private UserTokenRepository userTokenRepository;

    private String todoJson;
    private Cookie cookie;
    private UserToken userToken;
    private User user;



    private void givenNewTodoJsonString() throws JSONException {
        JSONObject builder = new JSONObject();
        this.todoJson = builder.put("text", "a new todo").toString();
    }

    private void givenTokenAndUserAndCookie() {
        this.cookie = new Cookie("access-token", "access");
        this.userToken = new UserToken();
        this.user = new User();
        userToken.setUser(this.user);
        given(userTokenRepository.findById("access")).willReturn(Optional.of(userToken));
    }

    @Test
    public void addTodo() throws Exception {
        givenNewTodoJsonString();
        givenTokenAndUserAndCookie();

        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setText("a new todo");
        given(todoService.addTodo(any())).willReturn(todoDTO);

        MvcResult mvcResult = mockMvc.perform(post("/todo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.todoJson)
                .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("a new todo"))
                .andReturn();
    }

    @Test
    public void name() throws Exception {
        givenNewTodoJsonString();
        givenTokenAndUserAndCookie();
        given(todoService.addTodo(any())).willThrow(new DataIntegrityViolationException(""));

        mockMvc.perform(post("/todo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.todoJson)
                .cookie(cookie))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }
}
