package com.homework.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.homework.web.pojo.Category;
import com.homework.web.pojo.Product;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.CategoryServiceImpl;
import com.homework.web.service.impl.ProductServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
public class PageController {
	@Autowired
	private CategoryServiceImpl categoryServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;

	// 主页
	@GetMapping("/")
	public String index(Model model, Integer category_id, Integer category_id2,
			@RequestParam(defaultValue = "12") Integer size, @RequestParam(defaultValue = "1") Integer page) {
		// 取出所有上架的初级分类
		List<Category> categoryList = categoryServiceImpl.selectAllIs_usingByParent_id(0);
		model.addAttribute("categoryList", categoryList);
		if (categoryList.size() != 0) {
			// 判断是否有初级分类的参数，没有则赋予一个初级分类
			if (category_id == null) {
				category_id = categoryList.get(0).getId();
			}
			model.addAttribute("category_id", category_id);
			// 取出该初级分类的所有上架中的二级分类
			List<Category> categoryList2 = categoryServiceImpl.selectAllIs_usingByParent_id(category_id);
			model.addAttribute("categoryList2", categoryList2);
			if (categoryList2.size() != 0) {
				// 判断是否有二级分类的参数，没有则赋予一个二级分类
				if (category_id2 == null) {
					category_id2 = categoryList2.get(0).getId();
				}
				model.addAttribute("category_id2", category_id2);
				// 取出该二级分类下该分页上架中的商品
				List<Product> productList = productServiceImpl.selectAllIs_usingByCategory_idPage(category_id2, size,
						page);
				model.addAttribute("productList", productList);
				// 取出该二级分类上架中的商品总数量，以便得到分页大小
				Integer count = productServiceImpl.selectTheCountIs_usingByCategory_id(category_id2);
				Integer pageSize = count / size;
				if (count % size != 0) {
					pageSize += 1;
				}
				ArrayList<Integer> pageList = new ArrayList<Integer>();
				for (int i = 1; i <= pageSize; i++) {
					pageList.add(i);
				}
				model.addAttribute("pageList", pageList);
			}
		}
		return "index";
	}

	// 登录注册页面
	@GetMapping("/authentication/login_register")
	public String login_register() {
		return "login_register";
	}


}
