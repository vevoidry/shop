package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Bill_product;
import com.homework.web.repository.Bill_productRepository;
import com.homework.web.service.Bill_productService;

@Service
public class Bill_productServiceImpl implements Bill_productService {
	@Autowired
	private Bill_productRepository bill_productRepository;

	@Override
	public Bill_product insert(Bill_product bill_product) {
		return bill_productRepository.save(bill_product);
	}

	@Override
	public List<Bill_product> selectByBill_id(Integer bill_id) {
		return bill_productRepository.selectByBill_id(bill_id);
	}
}
