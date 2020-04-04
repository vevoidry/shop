package com.homework.web.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Bill;
import com.homework.web.repository.BillRepository;
import com.homework.web.service.BillService;

@Service
public class BillServiceImpl implements BillService {
	@Autowired
	private BillRepository billRepository;

	@Override
	public Bill insert(Bill bill) {
		bill.setStatus("已付款");
		bill.setMoney(0.0);
		bill.setCreate_time(new Date());
		return billRepository.save(bill);
	}

	@Override
	public Bill update(Bill bill) {
		return billRepository.save(bill);
	}

	@Override
	public Bill findById(Integer id) {
		return billRepository.findById(id).get();
	}

	@Override
	public List<Bill> selectAllByUser_id(Integer user_id) {
		return billRepository.selectAllByUser_id(user_id);
	}

	@Override
	public void deleteById(Integer id) {
		billRepository.deleteById(id);
	}

	@Override
	public List<Bill> findAll() {
		return billRepository.findAll();
	}

	@Override
	public List<Bill> selectAllByStatus(String status) {
		return billRepository.selectAllByStatus(status);
	}

	@Override
	public List<Bill> selectAllByUser_idStatus(Integer user_id, String status) {
		return billRepository.selectAllByUser_idStatus(user_id, status);
	}

}
