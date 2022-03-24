package com.ggoreb.practice.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ggoreb.practice.model.Answer;
import com.ggoreb.practice.model.Question;
import com.ggoreb.practice.model.User;
import com.ggoreb.practice.repository.AnswerRepository;
import com.ggoreb.practice.repository.QuestionRepository;

@Controller
public class AnswerController {

	@Autowired
	AnswerRepository ar;
	
	
	@PostMapping("/answer/create")
	public String answerCreate(String content, long qid ,HttpSession session) {
		Answer answer = new Answer();
		
		
		answer.setContent(content);
		
		answer.setCreateDate(new Date());
		
		Question q = new Question();
		q.setId(qid);
		answer.setQuestion(q);
		
	
		
		User user = (User) session.getAttribute("user");
		answer.setUser(user);
		
		
		
		ar.save(answer);
	
		return "redirect:/question/detail?id="+qid;
	}


}
