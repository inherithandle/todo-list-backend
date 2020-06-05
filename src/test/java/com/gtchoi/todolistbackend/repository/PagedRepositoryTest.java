package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.Todo;
import com.gtchoi.todolistbackend.model.TodoDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PagedRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    ProjectRepository projectRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    public void pagedProjectWithTodos() {
        PageRequest pageable = PageRequest.of(0, 5, Sort.unsorted());
        Page<Project> page = projectRepository.getProjectWithTodos(3L, 4, pageable);

        Project project = page.getContent().get(0);

        System.out.println("count : " + page.getTotalElements());
        for (Todo t : project.getTodos()) {
            System.out.println(t.getText());
        }
    }

    @Test
    public void paged() {
        PageRequest pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("text")));
        Page<Todo> page = todoRepository.findByProject_User_UserNo(3L, pageable);
        List<Todo> content = page.getContent();
        assertThat(content.size(), is(5));
        assertThat(content.get(0).getText(), is("todo 9 at page 1"));
        assertThat(content.get(4).getText(), is("todo 5 at page 1"));

//        pageable = PageRequest.of(1, 5, Sort.by(Sort.Order.desc("text")));
//        page = todoRepository.findByProject_User_UserNo(3L, pageable);
//        content = page.getContent();
//
//        assertThat(content.size(), is(5));
//        assertThat(content.get(0).getText(), is("todo 4 at page 0"));
//        assertThat(content.get(1).getText(), is("todo 3 at page 0"));
    }

    @Test
    public void mapPageEntityObjectToDTO() {
        // map Page<Todo> to Page<TodoDTO>
        PageRequest pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("text")));
        Page<Todo> page = todoRepository.findByProject_User_UserNo(3L, pageable);

        Page<TodoDTO> dtoPage = page.map(todo -> modelMapper.map(todo, TodoDTO.class));
        List<TodoDTO> content = dtoPage.getContent();

        assertThat(content.size(), is(5));
        assertThat(content.get(0).getText(), is("todo 9 at page 1"));
        assertThat(content.get(4).getText(), is("todo 5 at page 1"));
    }
}
