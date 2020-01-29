package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.Todo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void getDueDateFromStore() {
        Todo todo = todoRepository.findByTodoIdAndUser(1L, 1L);

        assertThat(todo, is(notNullValue()));
        assertThat(todo.getDueDate(), is(notNullValue()));
        System.out.println(todo.getDueDate().toString());
    }

    @Test
    public void dueDateTest() {
        Todo todo = new Todo();

        LocalDateTime nowMax = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        todo.setDueDate(nowMax);
        todo.setText("a new todo");
        todo.setCompleted(false);
        todo.setProject(null);


        Todo save = todoRepository.save(todo);
        Long id = save.getId();

        Todo todoFromStore = todoRepository.findById(id).get();

        System.out.println(todoFromStore.getDueDate().toString());
        // expected output: 오늘날짜, yyyy-mm-ddT23:59:59.999999999
    }
}
