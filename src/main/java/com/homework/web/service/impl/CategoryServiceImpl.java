package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Category;
import com.homework.web.repository.CategoryRepository;
import com.homework.web.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> selectAllByParent_id(Integer parent_id) {
		return categoryRepository.selectAllByParent_id(parent_id);
	}

	@Override
	public List<Category> selectAllIs_usingByParent_id(Integer parent_id) {
		return categoryRepository.selectAllIs_usingByParent_id(parent_id);
	}

	@Override
	public List<Category> selectAllNotIs_usingByParent_id(Integer parent_id) {
		return categoryRepository.selectAllNotIs_usingByParent_id(parent_id);
	}

	@Override
	public void updateNotIs_usingById(Integer id) {
		categoryRepository.updateNotIs_usingById(id);
	}

	@Override
	public void updateIs_usingById(Integer id) {
		categoryRepository.updateIs_usingById(id);
	}

	@Override
	public List<Category> selectAllNotIs_usingParent_idNot0() {
		return categoryRepository.selectAllNotIs_usingParent_idNot0();
	}

	//
	@Override
	public Category insert(Category category) {
		category.setIs_using(true);
		return categoryRepository.save(category);
	}

	@Override
	public List<Category> selectAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Category selectById(Integer id) {
		return categoryRepository.findById(id).get();
	}

	@Override
	public Category update(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public void deleteById(Integer id) {
		categoryRepository.deleteById(id);
	}

}
