package com.homework.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.homework.web.pojo.Category;
import com.homework.web.pojo.Product;
import com.homework.web.service.impl.CategoryServiceImpl;
import com.homework.web.service.impl.ProductServiceImpl;

@Controller
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	// 管理员获取商品管理页面
	@GetMapping
	public String getProduct(Model model) {
		// 存储数据的对象
		HashMap<Category, List<HashMap<Category, List<Product>>>> map = new HashMap<Category, List<HashMap<Category, List<Product>>>>();
		// 获取所有初级分类
		List<Category> categoryList = categoryServiceImpl.selectAllByParent_id(0);// 所有初级分类
		System.out.println(1);
		if (categoryList.size() != 0) {// 遍历初级分类
			Iterator<Category> iterator = categoryList.iterator();
			while (iterator.hasNext()) {
				System.out.println(2);
				ArrayList<HashMap<Category, List<Product>>> list = new ArrayList<HashMap<Category, List<Product>>>();
				Category next = iterator.next();
				List<Category> categoryList2 = categoryServiceImpl.selectAllByParent_id(next.getId());// 每个初级分类下的二级分类
				if (categoryList2.size() != 0) {// 遍历二级分类
					Iterator<Category> iterator2 = categoryList2.iterator();
					while (iterator2.hasNext()) {
						System.out.println(3);
						Category next2 = iterator2.next();
						List<Product> productList = productServiceImpl.selectAllByCategory_id(next2.getId());
						HashMap<Category, List<Product>> hashMap = new HashMap<Category, List<Product>>();
						hashMap.put(next2, productList);
						list.add(hashMap);
					}
				}
				map.put(next, list);
			}
		}
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("map", map);
		return "admin_product";
	}

	// 管理员根据初级分类获取其下所有上架中的二级分类的json
	@GetMapping("/api")
	@ResponseBody//使得返回的数据为json数据
	public List<Category> getCategoryByParent_id(Integer parent_id) {
		return categoryServiceImpl.selectAllIs_usingByParent_id(parent_id);
	}

	// 管理员添加一个商品
	@PostMapping
	@Transactional
	public String postProduct(Product product, MultipartFile filename) throws Exception {
		if (product.getCategory_id() == null || product.getTitle().trim().equals("")
				|| product.getContent().trim().equals("") || product.getPrice() == null
				|| product.getQuantity() == null) {
			throw new RuntimeException("商品初级分类，二级分类，商品标题，商品内容，商品单价与商品库存均不可为空");
		}
		// 判断传输的图片名是否为空
		if ((filename.getOriginalFilename()).equals("")) {
			throw new RuntimeException("您上传的图片有问题");
		}
		// 保存到static下的image文件夹中
		String directoryPath = ResourceUtils.getURL("src").getPath() + "main/resources/static/image/";
		// 通过UUID获取随机字符串并为图片重命名，防止图片名冲突
		String imageName = UUID.randomUUID().toString().replaceAll("-", "") + filename.getOriginalFilename();
		File file = new File(directoryPath, imageName);
		if (!file.exists()) {
			file.createNewFile();
		}
		filename.transferTo(file);
		// 设置图片
		product.setImage(imageName);
		productServiceImpl.insert(product);
		return "redirect:/products";
	}

	// 游客获取一个具体商品的页面
	@GetMapping("/{id:[0-9]*}")
	public String getProductById(@PathVariable Integer id, Model model) {
		// 获取初级分类
		Product product = productServiceImpl.selectById(id);
		model.addAttribute("product", product);
		return "product";
	}

	// 管理员对一个商品进行上/下架，同时改变其分类的上下架状态
	@DeleteMapping
	public String deleteProduct(Integer id, String on_off) {
		if (on_off == null) {
			return "redirect:/products";
		}
		if (on_off.trim().equals("on")) {// 进行上架
			// 对商品进行上架
			Product product = productServiceImpl.selectById(id);
			product.setIs_using(true);
			productServiceImpl.update(product);
			// 对该商品的二级分类进行上架
			Category category = categoryServiceImpl.selectById(product.getCategory_id());
			if (!category.getIs_using()) {
				category.setIs_using(true);
				categoryServiceImpl.update(category);
			}
			// 对该商品的初级分类进行上架
			Category category2 = categoryServiceImpl.selectById(category.getParent_id());
			if (!category2.getIs_using()) {
				category2.setIs_using(true);
				categoryServiceImpl.update(category2);
			}
		} else if (on_off.trim().equals("off")) {// 进行下架
			// 对商品进行下架
			Product product = productServiceImpl.selectById(id);
			product.setIs_using(false);
			productServiceImpl.update(product);
		}
		return "redirect:/products";
	}

	// 管理员跳转到修改商品数据的页面
	@GetMapping("/edit")
	public String editProduct(Integer id, Model model) {
		Product product = productServiceImpl.selectById(id);
		model.addAttribute("product", product);
		return "admin_product_edit";
	}

	// 管理员修改商品
	@PutMapping
	public String putProduct(Product product, MultipartFile filename) throws Exception {
		if (product.getTitle().trim().equals("") || product.getContent().trim().equals("") || product.getPrice() == null
				|| product.getQuantity() == null) {
			throw new RuntimeException("商品商品标题，商品内容，商品单价与商品库存均不可为空");
		}
		Product product2 = productServiceImpl.selectById(product.getId());
		product2.setTitle(product.getTitle());
		product2.setContent(product.getContent());
		product2.setPrice(product.getPrice());
		product2.setQuantity(product.getQuantity());
		// 判断传输的图片名是否为空
		if (!(filename.getOriginalFilename()).equals("")) {// 不能用==null进行判断
			// 保存到static下的image文件夹中
			String directoryPath = ResourceUtils.getURL("src").getPath() + "main/resources/static/image/";
			// 通过UUID获取随机字符串并为图片重命名，防止图片名冲突
			String imageName = UUID.randomUUID().toString().replaceAll("-", "") + filename.getOriginalFilename();
			File file = new File(directoryPath, imageName);
			if (!file.exists()) {
				file.createNewFile();
			}
			filename.transferTo(file);
			product2.setImage(imageName);
		}
		productServiceImpl.update(product2);
		return "redirect:/products";
	}
}
