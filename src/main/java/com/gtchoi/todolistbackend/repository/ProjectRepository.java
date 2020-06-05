package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hello on 28/08/2018.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM project p left join fetch p.todos WHERE p.user.userNo = :userNo")
    List<Project> findFirstProjectBy(@Param("userNo") long userNo, Pageable pageable);

    @Query("SELECT DISTINCT p FROM project p left join fetch p.todos WHERE p.user.userNo = :userNo")
    List<Project> findByUserNo(@Param("userNo") long userNo);

    @Query("SELECT p FROM project p WHERE p.user.userNo = :userNo and p.projectNo = :projectNo")
    Project findByProjectNoAndUserNo(@Param("projectNo") Long projectNo, @Param("userNo") long userNo);

    Project findByProjectNo(Long projectNo);

    @Modifying
    @Query("delete from project p where p.projectNo = :projectNo and p.user.userNo = :userNo")
    int deleteProjectByProjectNo(@Param("projectNo") long projectNo, @Param("userNo") long userNo);

    List<Project> findByUser(User user);

    @Query(value = "SELECT p FROM project p LEFT JOIN FETCH p.todos t WHERE p.projectNo = :projectNo AND p.user.userNo = :userNo",
            countQuery = "SELECT COUNT(t) FROM todo t WHERE t.project.projectNo = :projectNo")
    Page<Project> getProjectWithTodos(@Param("userNo") long userNo, @Param("projectNo") long projectNo, Pageable pageable);
}
