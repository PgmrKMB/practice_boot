package com.ggoreb.practice.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ggoreb.practice.model.User;
import com.ggoreb.practice.repository.UserRepository;

@Controller
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/user/info")
	public String userInfo(Model model, HttpSession session) {
	
		User u = (User) session.getAttribute("user");
		
		User user = userRepository.findById(u.getId()).get();
		model.addAttribute("user", user);
		
		return "user_info";
	}

	
	
	@GetMapping("/user/check")
	@ResponseBody
	//Json 응답
	public User userCheck(String email) {
		
		User user = userRepository.findByEmail(email);
		
		return user;
	}
	
	

	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
	
	
	@PostMapping("/signup")
	public String signupPost(@ModelAttribute User user) {
		userRepository.save(user);
		return "redirect:/signin";
	}
	
	
	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}
	
	
	@PostMapping("/signin")
	public String signinPost(@ModelAttribute User user, HttpSession session) {
		Optional<User> opt = userRepository.findByEmailAndPwd(user.getEmail(), user.getPwd());
		if(opt.isPresent()) {
			session.setAttribute("user", opt.get());
		}
		return "redirect:/question/list";
	}
	
	
	@GetMapping("/signout")
	public String signout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
