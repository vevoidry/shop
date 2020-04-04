package com.homework.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.homework.web.pojo.User;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private AuthenticationManager authenticationManager;

	// 注册用户
	@PostMapping
	public String postUser(User user, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, Model model) {
		// 获取用户名与密码并去除两侧空格
		String username = user.getUsername().trim();
		String password = user.getPassword().trim();
		// 判断位数是否符合要求
		if (username.length() < 6 || username.length() > 15 || password.length() < 6 || password.length() > 15) {
			throw new RuntimeException("用户名与密码的长度必须为6-15位");
		}
		// 判断输入的数据是否符合要求（必须为大小写字母或数字）
		Pattern p = Pattern.compile("[0-9A-Za-z]*");
		Matcher m1 = p.matcher(username);
		Matcher m2 = p.matcher(password);
		if (!m1.matches() || !m2.matches()) {
			throw new RuntimeException("用户名与密码只能为数字或大小写字母");
		}
		// 判断用户名是否已经被使用
		User user2 = userServiceImpl.selectByUsername(username);
		if (user2 != null) {
			throw new RuntimeException("该用户名已被使用，请重新输入");
		}
		// 根据注册信息创建新用户
		User user3 = userServiceImpl.insert(user);
		// 以下为实现自动登录功能
		// 生成Authentication
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user3.getUsername(),
				user3.getPassword());
		try {
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authenticatedUser = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
			request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());
			// 设置session的user数据
			request.getSession().setAttribute("user", user3);
		} catch (Exception e) {
			System.out.println("Authentication failed: " + e.getMessage());
		}
		// 跳转至主页
		return "redirect:/";
	}

	// 进入个人主页
	@GetMapping
	public String getUsersById() {
		return "user";
	}

	// 进入个人主页修改页
	@GetMapping("/edit")
	public String getUser(Model model, Authentication authentication) {
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User user = userServiceImpl.selectByUsername(username);
		// 获取用户信息
		user.setPassword(null);// 将用户密码设置为null
		// 将数据放入model
		model.addAttribute("user", user);// 用户个人数据
		// 跳转至用户个人主页修改页
		return "user_edit";
	}

	// 提交修改信息
	@PutMapping
	public String putUser(User user, Authentication authentication, MultipartFile filename, HttpServletRequest request)
			throws Exception {
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.selectByUsername(username);
		// 判断传输的图片名是否为空
		if (!(filename.getOriginalFilename()).equals("")) {// 不能用==null进行判断
			// 保存到static下的image文件夹中
			String directoryPath = ResourceUtils.getURL("src").getPath() + "main/resources/static/image/";
			// 通过UUID获取随机字符串并为图片重命名，防止图片名冲突
			String imageName = UUID.randomUUID().toString().replaceAll("-", "") + filename.getOriginalFilename();
			File file = new File(directoryPath, imageName);
			if (!file.exists()) {
				file.createNewFile();
			}
			filename.transferTo(file);
			me.setImage(imageName);
		} else {
			me.setImage(me.getImage());
		}
		System.out.println(user.getGender());
		System.out.println(user.getNickname());
		me.setGender(user.getGender());
		me.setNickname(user.getNickname());
		me = userServiceImpl.update(me);
		// 刷新session
		request.getSession().setAttribute("user", me);
		return "redirect:/users";
	}

}
