package com.homework.web.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//分类
@Data
@Entity
@Table(name = "category")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键

	@Column(name = "parent_id")
	private Integer parent_id;// 该分类的父类id，如果没有父类，则为0

	@Column(name = "name")
	private String name;// 分类名

	@Column(name = "is_using")
	private Boolean is_using;// 是否正在使用中

}
