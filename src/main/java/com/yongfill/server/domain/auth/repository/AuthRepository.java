package com.spring.security.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.security.auth.domain.entity.Auth;
import com.spring.security.user.domain.entity.User;

public interface AuthRepository extends JpaRepository<Auth, Long>{

	Optional<Auth> findByRefreshToken(String refreshToken) throws Exception;

	boolean existsByUser(User user);

}
