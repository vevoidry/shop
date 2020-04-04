package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.User;

public interface UserService {
	User insert(User user);

	User selectById(Integer id);

	User selectByUsername(String username);

	User selectByNickname(String nickname);
	
	User update(User user);
	
	List<User> findAll();
}
