package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.TodoDTO;
import com.gtchoi.todolistbackend.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoController {

    Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private TodoService todoService;

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

}
