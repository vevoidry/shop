package com.homework.web.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.User;
import com.homework.web.repository.UserRepository;
import com.homework.web.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User insert(User user) {
		user.setRole("vip");
		user.setGender("男");
		user.setImage("x.png");
		user.setNickname(UUID.randomUUID().toString());
		return userRepository.save(user);
	}

	@Override
	public User selectById(Integer id) {
		return userRepository.findById(id).get();
	}

	@Override
	public User selectByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User selectByNickname(String nickname) {
		return userRepository.findByNickname(nickname);
	}

	@Override
	public User update(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

}
