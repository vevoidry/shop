package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Bill;

public interface BillService {
	Bill findById(Integer id);
	
	Bill insert(Bill bill);
	
	Bill update(Bill bill);
	
	List<Bill> selectAllByUser_id(Integer user_id);
	
	void deleteById(Integer id);
	
	List<Bill> findAll();
	
	List<Bill> selectAllByStatus(String status);
	
	List<Bill> selectAllByUser_idStatus(Integer user_id, String status);
}
