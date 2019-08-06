package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    private User user;
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

    @Test
    public void getProjects() {
        List<Project> projects = projectRepository.findByUserNo(1L);
        assertThat(projects.get(0).getProjectName(), is("inbox"));
        assertThat(projects.size(), is(2));
    }

    @Test
    public void getProjectByUser() {
        User joma = userRepository.findByUserNo(1L);
        assertThat(joma.getUserId(), is("joma"));

        List<Project> projects = projectRepository.findByUser(joma);
        assertThat(projects.get(0).getProjectName(), is("inbox"));
    }

    @Test
    public void getProjectByProjectNoAndUserNo() {
        givenUserAndProject();
        long userNo = this.user.getUserNo();
        Long projectNo = this.project.getProjectNo();
        System.out.println("userNo" + userNo);
        System.out.println("projectNo" + userNo);

        Project projectFromDB = projectRepository.findByProjectNoAndUserNo(projectNo, userNo);

        assertThat(projectFromDB.getProjectName(), is("new project"));
    }

    @Test(expected = EntityNotFoundException.class)
    public void getOneTest() {
        Project one = projectRepository.getOne(329847L);
        one.getProjectName();

    }
}