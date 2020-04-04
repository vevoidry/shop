package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query(value = "select * from product where  category_id=:category_id", nativeQuery = true)
	List<Product> selectAllByCategory_id(Integer category_id);

	// 根据分类与页面参数获取上架的商品
	@Query(value = "select * from product where is_using=true and category_id=:category_id limit :nonWantedSize,:wantedSize", nativeQuery = true)
	List<Product> selectAllIs_usingByCategory_idPage(Integer category_id, Integer nonWantedSize, Integer wantedSize);

	@Modifying(clearAutomatically = true)
	@Query(value = "delete from product where category_id=:category_id", nativeQuery = true)
	void deleteByCategory_id(Integer category_id);

	@Query(value = "select count(id) from product where is_using=true and category_id=:category_id", nativeQuery = true)
	Integer selectTheCountIs_usingByCategory_id(Integer category_id);

	// 根据分类id使其下的所有商品都下架
	@Modifying(clearAutomatically = true)
	@Query(value = "update product set is_using=false where category_id=:category_id", nativeQuery = true)
	void updateNotIs_usingByCategory_id(Integer category_id);

	// 根据分类id使其下的所有商品都上架
	@Modifying(clearAutomatically = true)
	@Query(value = "update product set is_using=true where category_id=:category_id", nativeQuery = true)
	void updateIs_usingByCategory_id(Integer category_id);
}
