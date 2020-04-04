package com.homework.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	// 根据username返回一个User对象
	@Query(value = "select * from user where username = :username", nativeQuery = true)
	User findByUsername(String username);

	// 根据nickname返回一个User对象
	@Query(value = "select * from user where nickname = :nickname", nativeQuery = true)
	User findByNickname(String nickname);
}
