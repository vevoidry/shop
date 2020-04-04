package com.homework.web.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//订单
@Data
@Entity
@Table(name = "bill")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Bill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_id")
	private Integer user_id;// 用户

	@Column(name = "money")
	private Double money;// 总金额

	@Column(name = "status")
	private String status;// 订单状态：已付款，已发货，已收货

	@Column(name = "create_time")
	private Date create_time;// 订单时间
}
