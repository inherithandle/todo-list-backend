package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.Todo;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.exception.UnAuthorizedException;
import com.gtchoi.todolistbackend.model.TodoDTO;
import com.gtchoi.todolistbackend.repository.ProjectRepository;
import com.gtchoi.todolistbackend.repository.TodoRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class TodoService {

    Logger logger = LoggerFactory.getLogger(TodoService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Transactional
    public TodoDTO addTodo(TodoDTO todoDTO) {
        Todo todo = modelMapper.map(todoDTO, Todo.class);

        Project project = projectRepository.getOne(todoDTO.getProjectNo());
        todo.setProject(project);

        Todo saved = todoRepository.save(todo);
        todoDTO.setId(saved.getId());
        return todoDTO;
    }

    @Transactional
    public void modifyTodo(TodoDTO todoDTO, User user) {
        Todo todo = todoRepository.findByTodoIdAndUser(todoDTO.getId(), user.getUserNo());
        if (todo == null) {
            logger.debug("todo 수정요청: todo id {}가 DB에 존재하지 않습니다.", todoDTO.getId());
            throw new NoSuchElementException();
        }
        todo.setText(todoDTO.getText());
        todo.setCompleted(todoDTO.isCompleted());
        todo.setDueDate(todoDTO.getLocalDateTimeDueDate());
    }

    @Transactional
    public int deleteTodo(Long id, User user) {
        Todo todo = todoRepository.findByTodoIdAndUser(id, user.getUserNo());
        if (todo == null) {
            logger.debug("todo 삭제요청: todo id {}가 DB에 존재하지 않거나 todo를 수정할 권한이 없습니다.", id);
            throw new NoSuchElementException();
        }

        int deleted = todoRepository.deleteTodoById(id);
        if (deleted == 0) {
            logger.debug("todo 삭제요청: todo id {}가 DB에 존재하지 않습니다.", id);
            throw new NoSuchElementException();
        }
        return deleted;
    }


    @Transactional
    public void toggleTodo(Long id, User user) {
        Todo todo = todoRepository.findByTodoIdAndUser(id, user.getUserNo());
        if (todo == null) {
            logger.debug("todo 수정요청: todo id {}가 DB에 존재하지 않거나 todo를 수정할 권한이 없습니다.", id);
            throw new NoSuchElementException();
        }
        todo.setCompleted(!todo.isCompleted());
    }

    @Transactional(readOnly = true)
    public Page<Todo> getTodos(User user, Long projectNo, Pageable pageable) {
        Page<Todo> todos = todoRepository.getTodos(projectNo, pageable);
        Project project = todos.getContent().get(0).getProject();
        if (project == null) {
            throw new NoSuchElementException();
        } else if (project.getUser().getUserNo() != user.getUserNo()) {
            throw new UnAuthorizedException();
        }
        return todos;
    }

}
