package com.homework.security;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ResourceUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
	@Autowired
	private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// 授权管理，permitAll()为不需要任何权限，即游客即可访问。.authenticated()则为必须登录才可访问。
				.authorizeRequests()
				// antMatchers和regexMatchers是两种不同的字符串匹配方法，可搭配使用
//				.regexMatchers(HttpMethod.GET, "/").permitAll()// 获取主页
//				.regexMatchers(HttpMethod.GET, "/authentication/login_register").permitAll()// 获取注册页面

				.regexMatchers(HttpMethod.POST, "/bills").authenticated()// 用户进行支付，新增一个订单
				.regexMatchers(HttpMethod.GET, "/bills").authenticated()// 用户跳转到订单页面
				.regexMatchers(HttpMethod.GET, "/bills/all").hasAuthority("ROLE_admin")// 管理员跳转到管理订单页面
				.regexMatchers(HttpMethod.GET, "/bills/[0-9]*").hasAuthority("ROLE_admin")// 管理员修改订单状态
				.regexMatchers(HttpMethod.GET, "/bills/[0-9]*/[0-9]*").authenticated()// 用户修改订单状态
				
				.regexMatchers(HttpMethod.GET, "/carts").authenticated()// 用户跳转到购物车页面
				.regexMatchers(HttpMethod.POST, "/carts").authenticated()// 用户添加一个商品到购物车

				.regexMatchers(HttpMethod.GET, "/categorys.*").hasAuthority("ROLE_admin")// 管理员获取分类管理页面
				.regexMatchers(HttpMethod.POST, "/categorys").hasAuthority("ROLE_admin")// 管理员添加一个分类
				.regexMatchers(HttpMethod.PUT, "/categorys").hasAuthority("ROLE_admin")// 管理员修改一个分类的分类名
				.regexMatchers(HttpMethod.DELETE, "/categorys").hasAuthority("ROLE_admin")// 管理员上/下架一个分类（及其子分类与相关商品）

				.regexMatchers(HttpMethod.GET, "/products").hasAuthority("ROLE_admin")// 管理员获取商品管理页面
				.regexMatchers(HttpMethod.GET, "/products/api.*").hasAuthority("ROLE_admin")// 管理员根据初级分类获取其下所有上架中的二级分类的json
				.regexMatchers(HttpMethod.POST, "/products").hasAuthority("ROLE_admin")// 管理员添加一个商品
				.regexMatchers(HttpMethod.GET, "/products/[0-9]*").permitAll()// 游客获取一个具体商品的页面
				.regexMatchers(HttpMethod.DELETE, "/products").hasAuthority("ROLE_admin")// 管理员对一个商品进行上/下架，同时改变其分类的上下架状态
				.regexMatchers(HttpMethod.GET, "/products/edit.*").hasAuthority("ROLE_admin")// 管理员跳转到修改商品数据的页面
				.regexMatchers(HttpMethod.PUT, "/products").hasAuthority("ROLE_admin")// 管理员修改商品
				
//				.regexMatchers(HttpMethod.POST, "/users").permitAll()// 注册用户
				.regexMatchers(HttpMethod.GET, "/users").authenticated()// 获取个人主页
				.regexMatchers(HttpMethod.GET, "/users/edit").authenticated()// 进入个人主页修改页
				.regexMatchers(HttpMethod.PUT, "/users").authenticated()// 修改个人信息

				.anyRequest().permitAll()// 剩下的url一律不需要任何权限

				// 认证管理
				.and().formLogin().loginPage("/authentication/login_register")
				.loginProcessingUrl("/authentication/authenticate").successHandler(myAuthenticationSuccessHandler)
				.failureHandler(myAuthenticationFailureHandler)

				// 退出管理
				.and().logout().logoutUrl("/authentication/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID")
				.and().csrf().disable();
	}

	// 用于注册后自动登录
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}

}
