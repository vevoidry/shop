package com.homework.web.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.homework.web.pojo.Category;
import com.homework.web.pojo.Product;
import com.homework.web.service.impl.CategoryServiceImpl;
import com.homework.web.service.impl.ProductServiceImpl;

@Controller
@RequestMapping("/categorys")
public class CategoryController {

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;

	// 管理员获取分类管理页面
	@GetMapping
	public String getCategory(Model model, Integer category_id, Integer category_id2,
			@RequestParam(defaultValue = "12") Integer size, @RequestParam(defaultValue = "1") Integer page) {
		// 上架分类
		// 存储所有上架的初级分类及每个初级分类下的上架的二级分类
		HashMap<Category, List<Category>> categoryMap = new HashMap<Category, List<Category>>();
		// 取出所有上架的初级分类
		List<Category> categoryList = categoryServiceImpl.selectAllIs_usingByParent_id(0);
		// 取出每个初级分类下的所有上架的二级分类
		if (categoryList.size() != 0) {
			Iterator<Category> iterator = categoryList.iterator();
			while (iterator.hasNext()) {
				Category category = iterator.next();
				List<Category> categoryList2 = categoryServiceImpl.selectAllIs_usingByParent_id(category.getId());
				categoryMap.put(category, categoryList2);
			}
		}
		model.addAttribute("categoryMap", categoryMap);
		// 下架分类
		// 取出所有下架的初级分类
		List<Category> categoryList2 = categoryServiceImpl.selectAllNotIs_usingByParent_id(0);
		model.addAttribute("categoryList2", categoryList2);
		// 取出所有下架的二级分类
		HashMap<Category, List<Category>> categoryMap2 = new HashMap<Category, List<Category>>();
		// 取出所有初级分类
		List<Category> categoryList3 = categoryServiceImpl.selectAllByParent_id(0);
		if (categoryList3.size() != 0) {
			Iterator<Category> iterator = categoryList3.iterator();
			// 取出相应初级分类下的所有下架的二级分类
			while (iterator.hasNext()) {
				Category next = iterator.next();
				List<Category> categoryList4 = categoryServiceImpl.selectAllNotIs_usingByParent_id(next.getId());
				categoryMap2.put(next, categoryList4);
			}
		}
		model.addAttribute("categoryMap2", categoryMap2);
		return "admin_category";
	}

	// 管理员添加一个分类
	@PostMapping
	@Transactional
	public String postCategory(Category category) {
		// 添加分类
		categoryServiceImpl.insert(category);
		return "redirect:/categorys";
	}

	// 管理员修改一个分类的分类名
	@PutMapping
	@Transactional
	public String putCategory(Category category) {
		Category category2 = categoryServiceImpl.selectById(category.getId());
		category2.setName(category.getName());
		categoryServiceImpl.update(category2);
		return "redirect:/categorys";
	}

	// 管理员上/下架一个分类（及其子分类与相关商品）
	@DeleteMapping
	@Transactional
	public String deleteCategory(Category category, String on_off) {
		if (on_off == null) {
			return "redirect:/categorys";
		}
		if (on_off.trim().equals("on")) {// 进行上架
			category = categoryServiceImpl.selectById(category.getId());
			// 判断是初级分类还是二级分类
			if (category.getParent_id() == 0) {
				// 查出该初级分类下的所有下架中的二级分类
				List<Category> categoryList = categoryServiceImpl.selectAllNotIs_usingByParent_id(category.getId());
				// 对所有二级分类的商品进行上架
				if (categoryList.size() != 0) {
					Iterator<Category> categoryListIterator = categoryList.iterator();
					while (categoryListIterator.hasNext()) {
						Category next = categoryListIterator.next();
						// 上架该二级分类下的所有商品
						productServiceImpl.updateIs_usingByCategory_id(next.getId());
						// 上架二级分类
						categoryServiceImpl.updateIs_usingById(next.getId());
					}
				}
				// 上架该初级分类
				categoryServiceImpl.updateIs_usingById(category.getId());
			} else {
				// 上架该二级分类下的所有商品
				productServiceImpl.updateIs_usingByCategory_id(category.getId());
				// 上架该二级分类
				categoryServiceImpl.updateIs_usingById(category.getId());
				// 上架该二级分类的初级分类
				categoryServiceImpl.updateIs_usingById(category.getParent_id());
			}
		} else if (on_off.trim().equals("off")) {// 进行下架
			category = categoryServiceImpl.selectById(category.getId());
			// 判断是初级分类还是二级分类
			if (category.getParent_id() == 0) {
				// 查出该初级分类下的所有上架中的二级分类
				List<Category> categoryList = categoryServiceImpl.selectAllIs_usingByParent_id(category.getId());
				// 对所有二级分类的商品进行下架
				if (categoryList.size() != 0) {
					Iterator<Category> categoryListIterator = categoryList.iterator();
					while (categoryListIterator.hasNext()) {
						Category next = categoryListIterator.next();
						// 下架该二级分类下的所有商品
						productServiceImpl.updateNotIs_usingByCategory_id(next.getId());
						// 下架二级分类
						categoryServiceImpl.updateNotIs_usingById(next.getId());
					}
				}
				// 下架该初级分类
				categoryServiceImpl.updateNotIs_usingById(category.getId());
			} else {
				// 下架该二级分类下的所有商品
				productServiceImpl.updateNotIs_usingByCategory_id(category.getId());
				// 下架该分类
				categoryServiceImpl.updateNotIs_usingById(category.getId());
			}
		}
		return "redirect:/categorys";
	}

}
