package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Product;
import com.homework.web.repository.ProductRepository;
import com.homework.web.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<Product> selectAllByCategory_id(Integer category_id) {
		return productRepository.selectAllByCategory_id(category_id);
	}

	@Override
	public List<Product> selectAllIs_usingByCategory_idPage(Integer category_id, Integer size, Integer page) {
		return productRepository.selectAllIs_usingByCategory_idPage(category_id, (page - 1) * size, size);
	}

	@Override
	public Product insert(Product product) {
		product.setIs_using(true);
		return productRepository.save(product);
	}

	@Override
	public Product selectById(Integer id) {
		return productRepository.findById(id).get();
	}

	@Override
	public void deleteByCategory_id(Integer category_id) {
		productRepository.deleteByCategory_id(category_id);
	}

	@Override
	public Product update(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Integer selectTheCountIs_usingByCategory_id(Integer category_id) {
		return productRepository.selectTheCountIs_usingByCategory_id(category_id);
	}

	@Override
	public void updateNotIs_usingByCategory_id(Integer category_id) {
		productRepository.updateNotIs_usingByCategory_id(category_id);
	}

	@Override
	public void updateIs_usingByCategory_id(Integer category_id) {
		productRepository.updateIs_usingByCategory_id(category_id);
	}
}
