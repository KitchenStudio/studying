package study.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import study.model.Authority;
import study.model.Item;
import study.model.Message;
import study.model.User;
import study.repository.AuthorityRepository;
import study.repository.UserRepository;

/**
 * 为了达到复用（移动终端和Web前端两部分），需要把Controller中属于业务流程（这里是注册判断
 * 过程）的部分封装成service，这个留到以后重构，或者你们有兴趣可以完成一下
 * 
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
			u = new User(username, encoder.encode(password),
					Collections.singleton(au));
			userRepository.save(u);
			return new Message(0, "success");
		}

	}

	/*
	 * 这里的处理感觉不是特别好
	 * 
	 * 关于这个 @Valid 会对提交的 json 进行验证
	 * 
	 * 如果出错，会出现这样的错误
	 * {
     *    "error": "Bad Request",
     *    "exception": "org.springframework.web.bind.MethodArgumentNotValidException",
     *    "message": "Validation failed for argument at index 0 in method: study.model.Message study.controller.UserController.info(study.form.UserForm,study.model.User,org.springframework.validation.BindingResult), with 1 error(s): [Field error in object 'userForm' on field 'mail': rejected value [fsdf]; codes [Email.userForm.mail,Email.mail,Email.java.lang.String,Email]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [userForm.mail,mail]; arguments []; default message [mail],[Ljavax.validation.constraints.Pattern$Flag;@5a7f7a03,.*]; default message [not a well-formed email address]] ",
     *    "path": "/user/18366116016/info",
     *    "status": 400,
     *    "timestamp": 1418285229360
     * }
     *
     * 所以 客户端需要对status code 进行判断
     * 
     * 事实上，我们会统一这个行为，有发生相应的错误都会设置 status code，以及发送一段
     * json 表示相关信息
	 */
	@RequestMapping(value = "/info", method = RequestMethod.PUT)
	Message info(@Valid @RequestBody User form,
			@PathVariable("username") User user
			) {
		if (form.getRealname() != null) {
			user.setRealname(form.getRealname());
		}

		if (form.getNickname() != null) {
			user.setNickname(form.getNickname());
		}

		if (form.getMail() != null) {
			user.setMail(form.getMail());
		}

		return new Message(0, "success");
	}
	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	User info(@PathVariable("username") User user) {
		return user;
	}
	
	@RequestMapping(value = "/stars", method = RequestMethod.GET)
	Set<Item> stars(Principal principal) {
		
		User user = userRepository.findOne(principal.getName());
		return user.getStars();
	}
	
	@RequestMapping(value = "/items", method = RequestMethod.GET)
	Set<Item> items(Principal principal) {
		User user = userRepository.findOne(principal.getName());
		return user.getItems();
	}

}
