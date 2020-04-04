package com.homework.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homework.web.pojo.Bill;
import com.homework.web.pojo.Bill_product;
import com.homework.web.pojo.Cart;
import com.homework.web.pojo.Product;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.BillServiceImpl;
import com.homework.web.service.impl.Bill_productServiceImpl;
import com.homework.web.service.impl.CartServiceImpl;
import com.homework.web.service.impl.ProductServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/bills")
public class BillController {

	@Autowired
	private BillServiceImpl billServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private CartServiceImpl cartServiceImpl;
	@Autowired
	private Bill_productServiceImpl bill_productServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;

	// 用户进行支付，新增一个订单
	@PostMapping
	@Transactional
	public String postBill(Authentication authentication) {
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User user = userServiceImpl.selectByUsername(username);
		// 取出该用户购物车的所有数据
		List<Cart> cartList = cartServiceImpl.selectAllByUser_id(user.getId());
		if (cartList == null) {
			return "redirect:/carts";
		}
		// 生成订单
		Bill bill = new Bill();
		bill.setUser_id(user.getId());
		Bill bill2 = billServiceImpl.insert(bill);
		// 生成订单关联商品
		Double sum = 0.0;
		Iterator<Cart> iterator = cartList.iterator();
		while (iterator.hasNext()) {
			Cart next = iterator.next();
			Product product = productServiceImpl.selectById(next.getProduct_id());
			// 判断该商品是否已经下架
			if (product.getIs_using() == false) {
				throw new RuntimeException(product.getTitle() + "已经下架，请删除该商品后再进行支付");
			}
			// 判断库存是否充足
			if (product.getQuantity() < next.getProduct_quantity()) {
				throw new RuntimeException(product.getTitle() + "的库存只有" + product.getQuantity() + "件，您购买的数量为"
						+ next.getProduct_quantity() + "，库存不足。");
			}
			// 将库存减少，代表卖出
			product.setQuantity(product.getQuantity() - next.getProduct_quantity());
			productServiceImpl.update(product);
			// 从购物车删除数据
			cartServiceImpl.deleteById(next.getId());
			// 保存关联
			Bill_product bill_product = new Bill_product();
			bill_product.setBill_id(bill2.getId());
			bill_product.setProduct_id(next.getProduct_id());
			bill_product.setProduct_quantity(next.getProduct_quantity());
			bill_productServiceImpl.insert(bill_product);
			// 添加订单总金额
			sum += next.getProduct_quantity() * product.getPrice();
		}
		bill2.setMoney(sum);
		Bill bill3 = billServiceImpl.update(bill2);
		if (bill3.getMoney() == 0) {
			billServiceImpl.deleteById(bill3.getId());
		}
		return "redirect:/bills";
	}

	// 用户跳转到订单页面
	@GetMapping
	public String getBill(Authentication authentication, Model model) {
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User user = userServiceImpl.selectByUsername(username);
		// 获取该用户的所有已付款订单
		List<Bill> billLIst1 = billServiceImpl.selectAllByUser_idStatus(user.getId(), "已付款");
		// 获取该用户的所有已发货订单
		List<Bill> billLIst2 = billServiceImpl.selectAllByUser_idStatus(user.getId(), "已发货");
		// 获取该用户的所有已收货订单
		List<Bill> billLIst3 = billServiceImpl.selectAllByUser_idStatus(user.getId(), "已收货");
		model.addAttribute("billLIst1", billLIst1);
		model.addAttribute("billLIst2", billLIst2);
		model.addAttribute("billLIst3", billLIst3);
		return "bill";
	}

	// 管理员跳转到管理订单页面
	@GetMapping("/all")
	public String all(Model model) {
		// 获取所有已付款订单
		HashMap<Bill, User> billUserMap1 = new HashMap<Bill, User>();
		List<Bill> billList1 = billServiceImpl.selectAllByStatus("已付款");
		if (billList1.size() != 0) {
			Iterator<Bill> iterator = billList1.iterator();
			while (iterator.hasNext()) {
				Bill bill = iterator.next();
				User user = userServiceImpl.selectById(bill.getUser_id());
				billUserMap1.put(bill, user);
			}
		}
		model.addAttribute("billUserMap1", billUserMap1);
		// 获取所有已发货订单
		HashMap<Bill, User> billUserMap2 = new HashMap<Bill, User>();
		List<Bill> billList2 = billServiceImpl.selectAllByStatus("已发货");
		if (billList2.size() != 0) {
			Iterator<Bill> iterator = billList2.iterator();
			while (iterator.hasNext()) {
				Bill bill = iterator.next();
				User user = userServiceImpl.selectById(bill.getUser_id());
				billUserMap2.put(bill, user);
			}
		}
		model.addAttribute("billUserMap2", billUserMap2);
		// 获取所有已收货订单
		HashMap<Bill, User> billUserMap3 = new HashMap<Bill, User>();
		List<Bill> billList3 = billServiceImpl.selectAllByStatus("已收货");
		if (billList3.size() != 0) {
			Iterator<Bill> iterator = billList3.iterator();
			while (iterator.hasNext()) {
				Bill bill = iterator.next();
				User user = userServiceImpl.selectById(bill.getUser_id());
				billUserMap3.put(bill, user);
			}
		}
		model.addAttribute("billUserMap3", billUserMap3);
		return "admin_bill";
	}

	// 管理员修改订单状态
	@GetMapping("/{id:[0-9]*}")
	public String send(String status, @PathVariable Integer id) {
		System.out.println(status);
		Bill bill = billServiceImpl.findById(id);
		bill.setStatus(status);
		billServiceImpl.update(bill);
		return "redirect:/bills/all";
	}

	// 用户修改订单状态
	@GetMapping("/{user_id:[0-9]*}/{id:[0-9]*}")
	public String get(String status, @PathVariable Integer user_id, @PathVariable Integer id) {
		System.out.println(status);
		Bill bill = billServiceImpl.findById(id);
		bill.setStatus(status);
		billServiceImpl.update(bill);
		return "redirect:/bills";
	}

}
