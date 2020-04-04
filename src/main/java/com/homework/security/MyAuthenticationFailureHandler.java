package com.homework.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;


@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		logger.info("用户身份认证失败，异常信息为：" + exception.getMessage());
		logger.info("正在将认证失败的用户重定向至登录页......");
		// 跳转至登录页
		redirectStrategy.sendRedirect(request, response, "/authentication/login_register");
//		response.sendRedirect( "/authentication/login_register");
//		webSocket.sendMessage("账号或密码错误");
	}
}
