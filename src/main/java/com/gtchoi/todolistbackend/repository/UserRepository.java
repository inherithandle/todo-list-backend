package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by hello on 20/08/2018.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(String userId);

    User findByUserNo(Long userNo);

    User findByEmail(String email);
}
