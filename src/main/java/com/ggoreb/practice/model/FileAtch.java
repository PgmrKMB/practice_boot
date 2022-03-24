package com.ggoreb.practice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class FileAtch {

	@Id
	@GeneratedValue
	Long id;
	
	String ofn;
	String sfn;
	
	@ManyToOne
	@ToString.Exclude
	Question question;
	
}
