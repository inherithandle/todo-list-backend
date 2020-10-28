package com.gtchoi.todolistbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    private final String OK_MESSAGE = "To-do List REST API Server: 200 OK!";

    @GetMapping
    public ResponseEntity<String> index() {
        return ResponseEntity.ok(OK_MESSAGE);
    }
}
