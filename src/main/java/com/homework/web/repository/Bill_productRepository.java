package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Bill_product;

public interface Bill_productRepository extends JpaRepository<Bill_product, Integer> {
	@Query(value = "select * from bill_product where bill_id = :bill_id", nativeQuery = true)
	List<Bill_product> selectByBill_id(Integer bill_id);
}
