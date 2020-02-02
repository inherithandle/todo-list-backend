package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, String> {

    @Query("SELECT u FROM UserToken u join fetch u.user WHERE u.accessToken = :accessToken")
    Optional<UserToken> findByAccessToken(@Param("accessToken") String accessToken);
}
