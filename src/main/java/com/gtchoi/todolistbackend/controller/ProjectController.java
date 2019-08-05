package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.ProjectDTO;
import com.gtchoi.todolistbackend.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class ProjectController {

    Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    ProjectService projectService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/projects")
    public List<ProjectDTO> getProjects(User user) {
        List<Project> projects = projectService.getProjects(user);
        List<ProjectDTO> dtos = projects.stream().map(project -> modelMapper.map(project, ProjectDTO.class)).collect(toList());
        dtos.get(0).setSelected(true);
        return dtos;
    }

    @PostMapping("/project")
    public ResponseEntity<Project> addProject(User user, @RequestBody ProjectDTO projectDTO) {
        logger.debug("project name : {}", projectDTO.getProjectName());
        Project project = projectService.addProject(projectDTO, user);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/project")
    public ResponseEntity<?> makeChangeToProject(User user, @RequestBody ProjectDTO projectDTO) {
        logger.debug("Project DTO");
        logger.debug(projectDTO.getProjectName());
        projectService.modifyProject(projectDTO, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/project")
    public ResponseEntity<?> deleteProject(User user, @RequestParam Long projectNo) {
        projectService.deleteProject(projectNo, user);
        return ResponseEntity.noContent().build();
    }
}
