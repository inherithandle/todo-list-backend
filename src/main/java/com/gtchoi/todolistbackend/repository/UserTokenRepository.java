package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, String> {
}
