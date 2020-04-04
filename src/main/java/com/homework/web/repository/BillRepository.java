package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Bill;
import com.homework.web.pojo.User;

public interface BillRepository extends JpaRepository<Bill, Integer> {

	@Query(value = "select * from bill where user_id = :user_id", nativeQuery = true)
	List<Bill> selectAllByUser_id(Integer user_id);

	@Query(value = "select * from bill where status = :status", nativeQuery = true)
	List<Bill> selectAllByStatus(String status);

	@Query(value = "select * from bill where user_id = :user_id and status=:status", nativeQuery = true)
	List<Bill> selectAllByUser_idStatus(Integer user_id, String status);
}
