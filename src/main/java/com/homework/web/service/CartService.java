package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Cart;

public interface CartService {
	List<Cart> selectAllByUser_id(Integer user_id);

	Cart insert(Cart cart);
	
	void deleteById(Integer id);
	
	Cart selectByUser_idProduct_id(Integer user_id, Integer product_id);
	
	Cart update(Cart cart);
}
