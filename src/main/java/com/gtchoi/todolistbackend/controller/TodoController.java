package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.entity.Todo;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.TodoDTO;
import com.gtchoi.todolistbackend.service.TodoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoController {

    private Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/todos")
    public Page<TodoDTO> getTodos(User user, @RequestParam Long projectNo, Pageable pageable) {
        Page<Todo> todosPage = todoService.getTodos(user, projectNo, pageable);
        return todosPage.map(todo -> modelMapper.map(todo, TodoDTO.class));
    }

    @PostMapping("/todo")
    public ResponseEntity<TodoDTO> addTodo(User user, @RequestBody TodoDTO todoDTO) {
        TodoDTO todo = todoService.addTodo(todoDTO);
        return ResponseEntity.ok(todo);
    }

    @PutMapping("/todo")
    public ResponseEntity<?> modifyTodo(User user, @RequestBody TodoDTO todoDTO) {
        todoService.modifyTodo(todoDTO, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<?> deleteTodo(User user, @RequestParam Long id) {
        todoService.deleteTodo(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public void page(Pageable pageable) {
        logger.debug("page index : {}", pageable.getPageNumber());
        logger.debug("page size : {}", pageable.getPageSize());
        logger.debug("sort : {}", pageable.getSort().toString());
    }

}
