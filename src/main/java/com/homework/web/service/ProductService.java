package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Category;
import com.homework.web.pojo.Product;

public interface ProductService {
	List<Product> selectAllByCategory_id(Integer category_id);
	
	List<Product> selectAllIs_usingByCategory_idPage(Integer category_id, Integer size, Integer page);

	Product insert(Product product);

	Product selectById(Integer id);

	void deleteByCategory_id(Integer category_id);

	Product update(Product product);
	
	Integer selectTheCountIs_usingByCategory_id(Integer category_id);
	
	void updateNotIs_usingByCategory_id(Integer category_id);
	
	void updateIs_usingByCategory_id(Integer category_id);

}
