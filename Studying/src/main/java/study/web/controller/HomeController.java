package study.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import study.model.Item;
import study.repository.ItemRepository;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	String login() {
		return "login";
	}
	
	@RequestMapping(value = {"/", "index.html"}, method = RequestMethod.GET)
	String home(Model model, Pageable pageable) {
		Page<Item> page = itemRepository.findAll(pageable);
		
		model.addAttribute("page", page);

		return "index";
	}
	
	

}
