package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.ProjectDTO;
import com.gtchoi.todolistbackend.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/projects")
    public List<ProjectDTO> getProjects(User user) {
        List<Project> projects = projectService.getProjects(user);
        assert projects.size() > 0;
        List<ProjectDTO> collect = projects.stream().map(project -> modelMapper.map(project, ProjectDTO.class)).collect(toList());
        return collect;
    }
}
