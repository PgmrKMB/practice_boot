package com.ggoreb.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ggoreb.practice.model.Question;
import com.ggoreb.practice.model.User;

public interface QuestionRepository extends JpaRepository<Question, Long> {

	Object findByUserId(User user);

}
