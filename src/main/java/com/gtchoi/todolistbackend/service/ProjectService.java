package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<Project> getProjects(User user) {
        List<Project> projects = projectRepository.findByUser(user);
        return projects;
    }
}
