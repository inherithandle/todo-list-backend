package com.gtchoi.todolistbackend.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hello on 20/08/2018.
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;
    private String userId;
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Project> projects;
    private String email;

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDefaultProject(EntityManager em) {
        if (!em.contains(this)) { // make sure this object in not managed by JPA provider.
            List<Project> projects = new ArrayList<>();
            Project project = new Project();
            projects.add(project);

            project.setProjectName("inbox");
            project.setUser(this);
            this.setProjects(projects);
        }
    }
}
