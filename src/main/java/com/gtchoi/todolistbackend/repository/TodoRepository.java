package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by hello on 20/08/2018.
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Modifying
    @Query("delete from todo t where t.id = :id")
    int deleteTodoById(@Param("id") long id);

    @Query("SELECT t FROM todo t join fetch t.project p join fetch p.user where t.id = :id and p.user.userNo = :userNo")
    Todo findByTodoIdAndUser(@Param("id") Long id, @Param("userNo") Long userNo);

    Page<Todo> findByProject_User_UserNo(Long userNo, Pageable pageable);

    @Query("SELECT t from todo t join t.project p where p.projectNo = :projectNo")
    Page<Todo> getTodos(@Param("projectNo") Long projectNo, Pageable pageable);
}
