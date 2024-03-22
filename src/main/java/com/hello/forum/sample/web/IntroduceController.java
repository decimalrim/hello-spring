package com.hello.forum.sample.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 없음 동작x
public class IntroduceController {
	
	// http://localhost:8080/introduce
	
	@GetMapping("/introduce")
	public String viewIntroducePage(Model model) {
		model.addAttribute("name", "홍길동");
		model.addAttribute("age", "40");
		model.addAttribute("city", "Seoul");
		
		return "introduce";
	}

}
