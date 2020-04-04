package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Bill_product;

public interface Bill_productService {

	Bill_product insert(Bill_product bill_product);
	
	List<Bill_product> selectByBill_id(Integer bill_id);
}
