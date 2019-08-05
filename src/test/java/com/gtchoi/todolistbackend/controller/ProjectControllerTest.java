package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.Todo;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.entity.UserToken;
import com.gtchoi.todolistbackend.exception.UnAuthorizedException;
import com.gtchoi.todolistbackend.repository.UserTokenRepository;
import com.gtchoi.todolistbackend.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProjectService projectService;

    @MockBean
    UserTokenRepository userTokenRepository;

    @TestConfiguration
    static class ModelMapperConfigTest {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Autowired
    ModelMapper modelMapper;

    @Test
    public void getProjects() throws Exception {
        List<Project> projects = new ArrayList<>();
        User user = new User();
        user.setUserId("joma");
        user.setProjects(projects);
        user.setPassword("abc");
        user.setUserNo(1L);

        UserToken userToken = new UserToken();
        userToken.setUser(user);

        List<Todo> todos = new ArrayList<>();
        Project project = new Project();
        project.setProjectName("inbox");
        project.setTodos(todos);
        projects.add(project);

        Todo todo = new Todo();
        todo.setCompleted(false);
        todo.setText("hello world");
        todos.add(todo);
        project.setTodos(todos);

        final Cookie cookie = new Cookie("access-token", "access");
        given(projectService.getProjects(any())).willReturn(projects);
        given(userTokenRepository.findById("access")).willReturn(Optional.of(userToken));

        mockMvc.perform(get("/projects")
                .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectName").value("inbox"))
                .andExpect(jsonPath("$[0].todos[0].text").value("hello world"))
                .andExpect(jsonPath("$[0].todos[0].completed").value("false"))
                .andExpect(jsonPath("$[0].todos[0].completed").isBoolean());

    }

    @Test
    public void noCookieError() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(UnAuthorizedException.MESSAGE));
    }

    @Test
    public void invalidAccessTokenError() throws Exception {
        final Cookie cookie = new Cookie("access-token", "access");
        given(projectService.getProjects(any())).willReturn(null);
        mockMvc.perform(get("/projects").cookie(cookie))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(UnAuthorizedException.MESSAGE));
    }

    @Test
    public void addProject() throws Exception {
        String jsonString = "{" +
                "\"projectName\": \"inbox\"," +
                "\"todos\": null" +
                "}";

        Project project = new Project();
        project.setProjectName("inbox");
        final Cookie cookie = new Cookie("access-token", "access");
        given(userTokenRepository.findById("access")).willReturn(Optional.of(new UserToken()));
        given(projectService.addProject(any(), any())).willReturn(project);

        MvcResult mvcResult = mockMvc.perform(post("/project").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonString)
                .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectName").value("inbox"))
                .andReturn();

        System.out.println("status: " + mvcResult.getResponse().getStatus());
        System.out.println("body : " + mvcResult.getRequest().getContentAsString());
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void modifyProject() throws Exception {
        final Cookie cookie = new Cookie("access-token", "access");
        given(userTokenRepository.findById("access")).willReturn(Optional.of(new UserToken()));
        willThrow(new NoSuchElementException())
                .given(projectService).modifyProject(any(), any());

        MvcResult mvcResult = mockMvc.perform(put("/project").contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.out.println(mvcResult.getResponse().getStatus());
        System.out.println(mvcResult.getResponse().getContentAsString());
    }



}