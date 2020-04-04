package com.homework.web.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homework.web.pojo.Cart;
import com.homework.web.pojo.Product;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.CartServiceImpl;
import com.homework.web.service.impl.ProductServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/carts")
public class CartController {

	@Autowired
	private CartServiceImpl cartServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;

	// 用户跳转到购物车页面
	@GetMapping
	public String getCart(Model model, Authentication authentication) {
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User user = userServiceImpl.selectByUsername(username);
		HashMap<Cart, Product> cartProductMap = new HashMap<Cart, Product>();
		List<Cart> cartList = cartServiceImpl.selectAllByUser_id(user.getId());
		Double money_sum = 0.0;
		if (cartList != null) {
			Iterator<Cart> iterator = cartList.iterator();
			while (iterator.hasNext()) {
				Cart cart = iterator.next();
				Product product = productServiceImpl.selectById(cart.getProduct_id());
				cartProductMap.put(cart, product);
				money_sum += cart.getProduct_quantity() * product.getPrice();
			}
		}
		model.addAttribute("money_sum", money_sum);
		model.addAttribute("cartProductMap", cartProductMap);
		return "cart";
	}

	// 用户添加一个商品到购物车
	@PostMapping
	public String postCart(Cart cart, Authentication authentication) {
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User user = userServiceImpl.selectByUsername(username);
		cart.setUser_id(user.getId());
		// 判断该用户是否已经存在该类商品，若存在，则添加商品数量，否则新增
		Cart cart2 = cartServiceImpl.selectByUser_idProduct_id(user.getId(), cart.getProduct_id());
		if (cart2 == null) {
			cartServiceImpl.insert(cart);
		} else {
			cart2.setProduct_quantity(cart2.getProduct_quantity() + cart.getProduct_quantity());
			cartServiceImpl.update(cart2);
		}
		return "redirect:/carts";
	}

	@GetMapping("/{id:[0-9]*}")
	public String deleteCart(@PathVariable Integer id) {
		cartServiceImpl.deleteById(id);
		return "redirect:/carts";
	}

}
