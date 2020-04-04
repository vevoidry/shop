package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Category;

public interface CategoryService {

	// 来自repository
	List<Category> selectAllByParent_id(Integer parent_id);
	
	List<Category> selectAllIs_usingByParent_id(Integer parent_id);

	List<Category> selectAllNotIs_usingByParent_id(Integer parent_id);

	void updateNotIs_usingById(Integer id);

	void updateIs_usingById(Integer id);
	
	List<Category> selectAllNotIs_usingParent_idNot0();

	// 其他
	List<Category> selectAll();

	Category insert(Category category);

	Category update(Category category);

	void deleteById(Integer id);

	Category selectById(Integer id);

}
