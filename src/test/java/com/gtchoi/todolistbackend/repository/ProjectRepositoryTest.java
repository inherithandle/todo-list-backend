package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}