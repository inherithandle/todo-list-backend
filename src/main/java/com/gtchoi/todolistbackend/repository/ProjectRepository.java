package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.Project;
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

    @Modifying
    @Query("delete from project p where p.projectNo = :projectNo")
    int deleteProjectByProjectNo(@Param("projectNo") long projectNo);
}
