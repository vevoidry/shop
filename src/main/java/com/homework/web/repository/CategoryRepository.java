package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.homework.web.pojo.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	// 根据父分类的id取出所有子分类
	@Query(value = "select * from category where parent_id=:parent_id", nativeQuery = true)
	List<Category> selectAllByParent_id(Integer parent_id);

	// 根据父分类的id取出所有上架中的子分类
	@Query(value = "select * from category where is_using=true and parent_id=:parent_id", nativeQuery = true)
	List<Category> selectAllIs_usingByParent_id(Integer parent_id);

	// 根据父分类的id取出所有已下架的子分类
	@Query(value = "select * from category where is_using=false and parent_id=:parent_id", nativeQuery = true)
	List<Category> selectAllNotIs_usingByParent_id(Integer parent_id);

	// 取出所有下架中的非初级分类
	@Query(value = "select * from category where is_using=false and parent_id!=0", nativeQuery = true)
	List<Category> selectAllNotIs_usingParent_idNot0();

	// 根据id下架分类
	@Modifying(clearAutomatically = true)
	@Query(value = "update category set is_using=false where id=:id", nativeQuery = true)
	void updateNotIs_usingById(Integer id);

	// 根据id上架分类
	@Modifying(clearAutomatically = true)
	@Query(value = "update category set is_using=true where id=:id", nativeQuery = true)
	void updateIs_usingById(Integer id);

}
