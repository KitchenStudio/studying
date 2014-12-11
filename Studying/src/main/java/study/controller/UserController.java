package study.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import study.model.Authority;
import study.model.Message;
import study.model.User;
import study.repository.AuthorityRepository;
import study.repository.UserRepository;

/**
 * 为了达到复用（移动终端和Web前端两部分），需要把Controller中属于业务流程（这里是注册判断
 * 过程）的部分封装成service，这个留到以后重构，或者你们有兴趣可以完成一下
 * @author seal
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	Message registe(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		
		User u = userRepository.findOne(username);
		if (u != null) {
			return new Message(1, "user has been registered");
		} else {
			Authority au = authorityRepository.findOne("USER");
			u = new User(username,encoder.encode(password), Collections.singleton(au));
			userRepository.save(u);
			return new Message(0, "success");
		}
		
	}

}
