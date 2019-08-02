package com.gtchoi.todolistbackend.model;

import java.util.List;

public class ProjectDTO {

    private String projectName;
    private List<TodoDTO> todos;


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<TodoDTO> getTodos() {
        return todos;
    }

    public void setTodos(List<TodoDTO> todos) {
        this.todos = todos;
    }
}
