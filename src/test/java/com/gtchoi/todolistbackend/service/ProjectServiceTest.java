package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.config.ModelMapperConfig;
import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.ProjectDTO;
import com.gtchoi.todolistbackend.repository.ProjectRepository;
import com.gtchoi.todolistbackend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectServiceTest {

    @TestConfiguration
    @Import(ModelMapperConfig.class)
    static class ProjectServiceConfig {
        @Bean
        public ProjectService projectService() {
            return new ProjectService();
        }
    }

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    ModelMapper modelMapper;

    private User user;
    private User user2;
    private Project project;

    public void givenUserAndProject() {
        this.project = new Project();
        project.setProjectName("new project");


        List<Project> projects = new ArrayList<>();
        projects.add(project);

        this.user = new User();
        user.setPassword("abc");
        user.setUserId("gtchoi");
        user.setProjects(projects);
        project.setUser(user);

        this.user = userRepository.save(this.user);
        this.project = this.user.getProjects().get(0);
    }

    public void givenAnotherUser() {
        this.user2 = new User();
        user2.setPassword("abc");
        user2.setUserId("junit-runner");
        this.user2 = userRepository.save(this.user2);
    }

    @Test
    public void modifyProject() {
        final String changed = "changed inbox name!";
        givenUserAndProject();
        ProjectDTO projectDTO = modelMapper.map(this.project, ProjectDTO.class);
        projectDTO.setProjectName(changed);

        projectService.modifyProject(projectDTO, user);

        Project modifiedProject = projectRepository.findById(project.getProjectNo()).get();

        assertThat(modifiedProject.getProjectName(), is(changed));
    }

    @Test(expected = NoSuchElementException.class)
    public void modifyProjectNotFound() {
        givenUserAndProject();
        ProjectDTO projectDTO = modelMapper.map(this.project, ProjectDTO.class);
        projectDTO.setProjectNo(213897124L);

        projectService.modifyProject(projectDTO, user);
    }

    @Test(expected = NoSuchElementException.class)
    public void modifyProjectUserNotFound() {
        givenUserAndProject();
        givenAnotherUser();
        ProjectDTO projectDTO = modelMapper.map(this.project, ProjectDTO.class);
        assertThat(this.project.getUser().getUserNo(), is(not(this.user2.getUserNo())));

        projectService.modifyProject(projectDTO, user2);
    }

    @Test
    public void deleteProject() {
        givenUserAndProject();
        ProjectDTO projectDTO = modelMapper.map(this.project, ProjectDTO.class);

        int numOfDeleted = projectService.deleteProject(projectDTO, this.user);
        assertThat(numOfDeleted, is(1));
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteProjectNotFound() {
        givenUserAndProject();
        ProjectDTO projectDTO = modelMapper.map(this.project, ProjectDTO.class);
        projectDTO.setProjectNo(2394732L);

        projectService.deleteProject(projectDTO, this.user);
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteProjectUserNotFound() {
        givenUserAndProject();
        givenAnotherUser();
        ProjectDTO projectDTO = modelMapper.map(this.project, ProjectDTO.class);
        assertThat(this.project.getUser().getUserNo(), is(not(this.user2.getUserNo())));

        projectService.deleteProject(projectDTO, user2);
    }

    @Test
    public void addProject() {
        givenUserAndProject();
        ProjectDTO projectDTO = modelMapper.map(this.project, ProjectDTO.class);
        Project fromDB = projectService.addProject(projectDTO, user);
        assertThat(fromDB.getProjectName(), is("new project"));
        assertThat(fromDB.getProjectNo(), greaterThan(0L));
    }
}