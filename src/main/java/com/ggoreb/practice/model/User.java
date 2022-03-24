package com.ggoreb.practice.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	String email;
	@JsonIgnore
	String pwd;
	String name;
	
	
	//manytoone 으로 연결되어 있으면 mappedby 사용 , 양방향
	//Junit에서는 EAGER옵션 사용 또는 @Transational 사용
	//Thymeleaf 에서는 EAGER옵션 사용하지 않아도 동작
	@JsonIgnore
	@OneToMany(mappedBy = "user")
    List<Question> questions = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Answer> answers = new ArrayList<>();

}
