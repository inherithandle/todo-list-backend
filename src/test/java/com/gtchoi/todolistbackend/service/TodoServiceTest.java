package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.config.ModelMapperConfig;
import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.Todo;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.TodoDTO;
import com.gtchoi.todolistbackend.repository.ProjectRepository;
import com.gtchoi.todolistbackend.repository.TodoRepository;
import com.gtchoi.todolistbackend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TodoServiceTest {

    @TestConfiguration
    @Import(ModelMapperConfig.class)
    static class TodoServiceConfig {
        @Bean
        public TodoService todoService() {
            return new TodoService();
        }
    }

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoService todoService;

    @Autowired
    ModelMapper modelMapper;

    private User user;
    private User user2;
    private Project project;
    private Todo todo;

    public void givenUserAndProject() {
        this.project = new Project();
        project.setProjectName("new project");

        List<Project> projects = new ArrayList<>();
        projects.add(project);

        this.user = new User();
        user.setPassword("abc");
        user.setUserId("gtchoi");
        user.setProjects(projects);
        project.setUser(user);

        this.user = userRepository.save(this.user);
        this.project = this.user.getProjects().get(0);
    }

    public void givenTodo() {
        givenUserAndProject();
        this.todo = new Todo();
        this.todo.setText("a new todo");
        this.todo.setProject(this.project);
        this.todo = todoRepository.save(this.todo);
    }

    public void givenAnotherUser() {
        this.user2 = new User();
        user2.setPassword("abc");
        user2.setUserId("junit-runner");
        this.user2 = userRepository.save(this.user2);
    }

    @Test
    public void addTodo() {
        givenUserAndProject();

        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setProjectNo(this.project.getProjectNo());
        todoDTO.setText("a new todo");
        TodoDTO saved = todoService.addTodo(todoDTO);

        assertThat(saved.getId(), greaterThan(0L));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addTodoGivenNonExsitingProject() {
        givenUserAndProject();

        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setProjectNo(11012382L); // this project is not on database.
        todoDTO.setText("a new todo");
        TodoDTO saved = todoService.addTodo(todoDTO);
    }

    @Test
    public void modifyTodo() {
        givenTodo();
        TodoDTO todoDTO = modelMapper.map(this.todo, TodoDTO.class);
        todoDTO.setText("modified");

        todoService.modifyTodo(todoDTO, this.user);

        Todo todoFromDB = todoRepository.findById(todoDTO.getId()).get();
        assertThat(todoFromDB.getText(), is("modified"));
    }

    @Test(expected = NoSuchElementException.class)
    public void modifyTodoAndUserNotFound() {
        givenTodo();
        givenAnotherUser();

        TodoDTO todoDTO = modelMapper.map(this.todo, TodoDTO.class);
        todoDTO.setText("modified");
        todoService.modifyTodo(todoDTO, this.user2);
    }

    @Test
    public void deleteTodo() {
        givenTodo();
        Long id = this.todo.getId();
        int deleted = todoService.deleteTodo(id, this.user);

        assertThat(deleted, is(1));
        // TODO: DELETE 쿼리후 롤백 되는 현상?
    }

    /**
     * 유저 A가 유저 B의 todo를 삭제하려고 할 때, 에러가 발생해야한다.
     */
    @Test(expected = NoSuchElementException.class)
    public void deleteTodoAndUserNotFound() {
        givenTodo();
        givenAnotherUser();

        todoService.deleteTodo(this.todo.getId(), this.user2);
    }

}