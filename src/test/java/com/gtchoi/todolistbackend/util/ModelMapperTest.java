package com.gtchoi.todolistbackend.util;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.Todo;
import com.gtchoi.todolistbackend.model.ProjectDTO;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ModelMapperTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    public void convert() {

        List<Todo> todos = new ArrayList<>();
        Project project = new Project();
        project.setProjectName("inbox");
        project.setTodos(todos);

        Todo todo = new Todo();
        todo.setCompleted(false);
        todo.setText("hello world");
        todos.add(todo);

        Todo todo2 = new Todo();
        todo2.setCompleted(false);
        todo2.setText("hello new todo 2 world");
        todos.add(todo2);

        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        assertThat(projectDTO.getProjectName(), is("inbox"));
        assertThat(projectDTO.getTodos().get(0).getText(), is("hello world"));
        assertThat(projectDTO.getTodos().get(1).getText(), is("hello new todo 2 world"));
    }
}
