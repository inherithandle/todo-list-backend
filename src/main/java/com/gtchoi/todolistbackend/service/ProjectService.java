package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.exception.InternalServerErrorException;
import com.gtchoi.todolistbackend.model.ProjectDTO;
import com.gtchoi.todolistbackend.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectService {

    Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<Project> getProjects(User user) {
        List<Project> projects = projectRepository.findByUserNo(user.getUserNo());
        if (projects == null || projects.size() == 0) {
            logger.error("유저당 프로젝트가 1개가 반드시 존재해야합니다. 비정상적인 방식으로 유저의 프로젝트가 삭제되었습니다.");
            throw new InternalServerErrorException("");
        }
        return projects;
    }

    @Transactional
    public Project addProject(ProjectDTO projectDTO, User user) {
        Project project = new Project();
        project.setProjectName(projectDTO.getProjectName());
        project.setUser(user);
        Project saved = projectRepository.save(project);
        return saved;
    }

    @Transactional
    public void modifyProject(ProjectDTO projectDTO, User user) {
        Project project = projectRepository.findByProjectNoAndUserNo(projectDTO.getProjectNo(), user.getUserNo());
        if (project == null) {
            logger.debug("projectNo {}가 DB에 존재하지 않습니다.", projectDTO.getProjectNo());
            throw new NoSuchElementException();
        }
        project.setProjectName(projectDTO.getProjectName());
    }

    @Transactional
    public int deleteProject(Long projectNo, User user) {
        int deleted = projectRepository.deleteProjectByProjectNo(projectNo, user.getUserNo());
        if (deleted == 0) {
            logger.debug("projectNo {}가 DB에 존재하지 않습니다.", projectNo);
            throw new NoSuchElementException();
        }
        return deleted;
    }

    @Transactional(readOnly = true)
    public Page<Project> getProjectWithTodos(User user, long projectNo, Pageable pageable) {
        // projectRepository.findByUserUserNoAndProjectNo(user.getUserNo, projectNo, pageable);
        // return page<Project>
        // if null... not authorized ..

        // todo join project join user...


        Page<Project> pageProject = projectRepository.getProjectWithTodos(user.getUserNo(), projectNo, pageable);

        if (pageProject.getContent().get(0) == null) {
            logger.error("not authorized.");
            throw new RuntimeException();
        }
        return pageProject;
    }
}
