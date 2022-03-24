package com.ggoreb.practice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ggoreb.practice.model.Answer;
import com.ggoreb.practice.model.FileAtch;
import com.ggoreb.practice.model.Question;
import com.ggoreb.practice.model.User;
import com.ggoreb.practice.repository.AnswerRepository;
import com.ggoreb.practice.repository.FileAtchRepository;
import com.ggoreb.practice.repository.QuestionRepository;

@Controller
public class QuestionController {
	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	AnswerRepository ar;

	@Autowired
	FileAtchRepository fr;

	@GetMapping("/download")
	public ResponseEntity<Resource> download(Question question) throws Exception {

		List<FileAtch> fList = fr.findByQuestion(question);

		String fName = fList.get(0).getSfn();

		File file = new File("c:/hrdkmb/" + fName);

		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		return ResponseEntity.ok()
				.header("content-disposition", "filename=" + URLEncoder.encode(file.getName(), "utf-8"))
				.contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream"))
//				.contentType(MediaType.parseMediaType("image/jpeg"))
				.body(resource);
	}

	@GetMapping("/question/list")
	public String question(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
		Page<Question> p = questionRepository.findAll(PageRequest.of(page - 1, 10, Sort.Direction.DESC, "createDate"));

		List<Question> list = p.getContent();

		model.addAttribute("list", list);
		return "question_list";
	}

	@GetMapping("/question/create")
	public String questionCreate() {
		return "question_create";
	}

	@PostMapping("/question/create")
	public String questionCreatePost(@ModelAttribute Question question, HttpSession session,
			MultipartHttpServletRequest mrq) {

		/* 질문 저장 */
		User user = (User) session.getAttribute("user");
		question.setUser(user);
		question.setCreateDate(new Date());
		questionRepository.save(question);

		/* 파일 저장 */
		Iterator<String> iter = mrq.getFileNames();
		while (iter.hasNext()) {
			String inputName = iter.next();
			List<MultipartFile> mFiles = mrq.getFiles(inputName);
			for (MultipartFile mFile : mFiles) {
				String oName = mFile.getOriginalFilename();

				if (oName == null || oName.equals("")) {
					break;
				}

				/* sName */
				String sName = "";
				String fname = oName.substring(0, oName.lastIndexOf("."));
				String fext = oName.substring(oName.lastIndexOf("."));
				long time = System.currentTimeMillis();
				sName = fname + "_" + time / 1000 + fext;

				try {
					FileAtch fatc = new FileAtch();
					fatc.setOfn(oName);
					fatc.setSfn(sName);
					fatc.setQuestion(question);
					fr.save(fatc);

					mFile.transferTo(new File("c:/hrdkmb/" + sName));
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "redirect:/question/list";
	}

	@GetMapping("/question/update")
	public String questionUpdate(Long id, Model model) {

		Optional<Question> opt = questionRepository.findById(id);

		// 조회한 질문이 존재한다면 ( 엉뚱한 id 조회시 존재하지 않을 수 있음)
		if (opt.isPresent()) {
			model.addAttribute("question", opt.get());
		} else {
			// 잘못된 용청입니다. 정상적인 접근이 아닙니다
		}
		return "question_update";
	}

	@PostMapping("/question/update")
	public String questionUpdatePost(@ModelAttribute Question question, HttpSession session, Long id) {
		User user = (User) session.getAttribute("user");

		Optional<Question> opt = questionRepository.findById(id);
		if (opt.isPresent()) {
			Question dbq = opt.get();
			dbq.setSubject(question.getSubject());
			dbq.setContent(question.getContent());
			questionRepository.save(dbq);
		}
		return "redirect:/question/list";
	}

	@GetMapping("/question/detail")
	public String questionDetail(Model model, Long id) {
		Optional<Question> opt = questionRepository.findById(id);
		if (opt.isPresent()) {
			model.addAttribute("question", opt.get());
			System.out.println(opt.get().toString());
		}

		List<Answer> opt2 = ar.findByQuestion(opt.get());
		model.addAttribute("alist", opt2);

		return "question_detail";
	}

	@GetMapping("/question/delete")
	public String questionDelete(Long id) {
		questionRepository.deleteById(id);

		return "redirect:/question/list";
	}
}
