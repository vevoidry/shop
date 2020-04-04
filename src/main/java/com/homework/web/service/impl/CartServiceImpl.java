package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Cart;
import com.homework.web.repository.CartRepository;
import com.homework.web.service.CartService;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRepository cartRepository;

	@Override
	public List<Cart> selectAllByUser_id(Integer user_id) {
		return cartRepository.selectAllByUser_id(user_id);
	}

	@Override
	public Cart insert(Cart cart) {
		return cartRepository.save(cart);
	}

	@Override
	public void deleteById(Integer id) {
		cartRepository.deleteById(id);
	}

	@Override
	public Cart selectByUser_idProduct_id(Integer user_id, Integer product_id) {
		return cartRepository.selectByUser_idProduct_id(user_id, product_id);
	}

	@Override
	public Cart update(Cart cart) {
		return cartRepository.save(cart);
	}
}
