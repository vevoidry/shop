package com.homework.web.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//商品
@Data
@Entity
@Table(name = "product")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "category_id")
	private Integer category_id;// 分类

	@Column(name = "title")
	private String title;// 标题

	@Column(name = "content")
	private String content;// 描述

	@Column(name = "price")
	private Double price;// 单价

	@Column(name = "image")
	private String image;// 图片

	@Column(name = "quantity")
	private Integer quantity;// 库存

	@Column(name = "is_using")
	private Boolean is_using;// 是否上架
}
