package com.homework.web.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//订单与商品关联表
@Data
@Entity
@Table(name = "bill_product")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Bill_product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "bill_id")
	private Integer bill_id;// 订单号

	@Column(name = "product_id")
	private Integer product_id;// 商品号

	@Column(name = "product_quantity")
	private Integer product_quantity;// 商品数量
}
