package com.homework.web.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//购物车
@Data
@Entity
@Table(name = "cart")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_id")
	private Integer user_id;// 用户

	@Column(name = "product_id")
	private Integer product_id;// 商品

	@Column(name = "product_quantity")
	private Integer product_quantity;// 商品数量

}
