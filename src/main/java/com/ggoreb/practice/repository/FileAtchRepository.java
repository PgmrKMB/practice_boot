package com.ggoreb.practice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ggoreb.practice.model.FileAtch;
import com.ggoreb.practice.model.Question;


public interface FileAtchRepository extends JpaRepository<FileAtch, Long> {

	List<FileAtch> findByQuestion(Question question);
	
}
