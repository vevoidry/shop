package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	@Query(value = "select * from cart where user_id=:user_id", nativeQuery = true)
	List<Cart> selectAllByUser_id(Integer user_id);

	@Query(value = "select * from cart where user_id=:user_id and product_id=:product_id", nativeQuery = true)
	Cart selectByUser_idProduct_id(Integer user_id, Integer product_id);
}
