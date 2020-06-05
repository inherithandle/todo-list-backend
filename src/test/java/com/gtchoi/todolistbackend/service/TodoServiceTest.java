package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.config.ModelMapperConfig;
import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.Todo;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.exception.UnAuthorizedException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
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

    @Test
    public void getPagedTodo() {
        PageRequest page1 = PageRequest.of(0, 5);
        PageRequest page2 = PageRequest.of(1, 5);
        User user = userRepository.getOne(3L);
        Page<Todo> pageTodos1 = todoService.getTodos(user, 4L, page1);
        Page<Todo> pageTodos2 = todoService.getTodos(user, 4L, page2);

        assertThat(pageTodos1.getTotalElements(), is(10L));
        assertThat(pageTodos1.getNumberOfElements(), is(5));
        assertThat(pageTodos1.getContent().get(0).getText(), is("todo 4 at page 0"));

        assertThat(pageTodos2.getTotalElements(), is(10L));
        assertThat(pageTodos2.getNumberOfElements(), is(5));
        assertThat(pageTodos2.getContent().get(0).getText(), is("todo 5 at page 1"));
    }

    @Test
    public void getPagedTodoWithSort() {
        PageRequest page1 = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("dueDate")));
        PageRequest page2 = PageRequest.of(1, 5, Sort.by(Sort.Order.desc("dueDate")));
        User user = userRepository.getOne(3L);
        Page<Todo> pageTodos1 = todoService.getTodos(user, 4L, page1);
        Page<Todo> pageTodos2 = todoService.getTodos(user, 4L, page2);

        assertThat(pageTodos1.getTotalElements(), is(10L));
        assertThat(pageTodos1.getNumberOfElements(), is(5));
        assertThat(pageTodos1.getContent().get(0).getDueDate(), is(LocalDateTime.of(2023, 3, 29, 0, 0, 0)));

        assertThat(pageTodos2.getTotalElements(), is(10L));
        assertThat(pageTodos2.getNumberOfElements(), is(5));
        assertThat(pageTodos2.getContent().get(0).getDueDate(), is(LocalDateTime.of(2023, 3, 12, 0, 0, 0)));
    }

    @Test(expected = UnAuthorizedException.class)
    public void 유저A가_유저B의_TODO에_접근() {
        PageRequest page = PageRequest.of(0, 10);
        User user = userRepository.getOne(2L);
        todoService.getTodos(user, 4L, page);
    }

    @Test(expected = NoSuchElementException.class)
    public void 존재하지않는_Todo_접근() {
        PageRequest page = PageRequest.of(0, 10);
        User user = userRepository.getOne(2L);
        todoService.getTodos(user, 404L, page);
    }

    @Test
    public void concat() {
        String s = "select * " +
                "from (   " +
                "    select *, dense_rank() OVER (ORDER BY post_id) rank " +
                "    from (   " +
                "        select p.*, pc.* " +
                "        from post p  " +
                "        left join post_comment pc on p.id = pc.post_id  " +
                "        order by p.created_on " +
                "    ) p_pc " +
                ") p_pc_r " +
                "where p_pc_r.rank <= :rank";
        System.out.println(s);
    }

}