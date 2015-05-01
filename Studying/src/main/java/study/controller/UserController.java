package study.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import study.entity.Authority;
import study.entity.BaseItem;
import study.entity.CommentItem;
import study.entity.FileFigure;
import study.entity.FileItem;
import study.entity.Item;
import study.entity.Message;
import study.entity.User;
import study.model.Userinfo;
import study.repository.AuthorityRepository;
import study.repository.FileFigureRepository;
import study.repository.FileItemRepository;
import study.repository.UserRepository;
import study.service.PictureService;

/**
 * 为了达到复用（移动终端和Web前端两部分），需要把Controller中属于业务流程（这里是注册判断
 * 过程）的部分封装成service，这个留到以后重构，或者你们有兴趣可以完成一下
 * 
 * @author seal
 *
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private PictureService pictureService;

	@Autowired
	private FileItemRepository fileItemRepository;

	@Autowired
	private FileFigureRepository fileFigureRepository;


	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String registe(@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("nickname") String nickname) {
		User u = userRepository.findOne(username);
		if (u != null) {
			return "user has been registered";
		} else {
			Authority au = authorityRepository.findOne("USER");
			u = new User(username, encoder.encode(password),
					Collections.singleton(au), nickname);
			userRepository.save(u);
			return "success";
		}

	}

	// 存储上传item的文件内容
	@RequestMapping(value = "/{username}/savefigure", method = RequestMethod.POST)
	String saveFile(
			@RequestParam(value = "file", required = false) MultipartFile files,
			@PathVariable("username") User user, HttpServletRequest request) {
		String syspath = request.getServletContext().getRealPath("/");
		File userFigure = new File(syspath + "/userFigure");
		String path = request.getContextPath();
		if (!userFigure.isDirectory()) {
			userFigure.mkdir();

		}
		String url = null;
		if (files != null) {
			FileFigure fileFigure = null;
			try {
				String filename = files.getOriginalFilename();
				File destFile = File.createTempFile("study-", "-" + filename,
						userFigure);

				files.transferTo(destFile);
				url = path + "/userFigure/" + destFile.getName();

				fileFigure = new FileFigure(filename, url, FileFigure.PICTURE);
				fileFigure.setUser(user);
				System.out.println("savefigur1e");

				fileFigureRepository.save(fileFigure);

			} catch (IOException e) {
				e.printStackTrace();
				return "failure";
			}

			user.setFigure(fileFigure);
			userRepository.save(user);

		}
		return "success";
	}

	/*
	 * 这里的处理感觉不是特别好
	 * 
	 * 关于这个 @Valid 会对提交的 json 进行验证
	 * 
	 * 如果出错，会出现这样的错误 { "error": "Bad Request", "exception":
	 * "org.springframework.web.bind.MethodArgumentNotValidException",
	 * "message":
	 * "Validation failed for argument at index 0 in method: study.model.Message study.controller.UserController.info(study.form.UserForm,study.model.User,org.springframework.validation.BindingResult), with 1 error(s): [Field error in object 'userForm' on field 'mail': rejected value [fsdf]; codes [Email.userForm.mail,Email.mail,Email.java.lang.String,Email]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [userForm.mail,mail]; arguments []; default message [mail],[Ljavax.validation.constraints.Pattern$Flag;@5a7f7a03,.*]; default message [not a well-formed email address]] "
	 * , "path": "/user/18366116016/info", "status": 400, "timestamp":
	 * 1418285229360 }
	 * 
	 * 所以 客户端需要对status code 进行判断
	 * 
	 * 事实上，我们会统一这个行为，有发生相应的错误都会设置 status code，以及发送一段 json 表示相关信息
	 */
	@RequestMapping(value = "/{username}/info", method = RequestMethod.PUT)
	public String info(@Valid Userinfo userinfo,
			@PathVariable("username") User user/* , BindingResult result */) {
		System.out.println(user.getUsername() + "username");
		// 按音序排列，方便找遗漏或者错误
		if (userinfo.getAge() != null) {
			// TODO user add age
		}

		if (userinfo.getEmail() != null) {
			// TODO User add email
		}

		if (userinfo.getNickname() != null) {
			user.setNickname(userinfo.getNickname());
		}

		if (userinfo.getPassword() != null) {
			user.setPassword(userinfo.getPassword());
		}

		if (userinfo.getPhonenumber() != null) {
			// TODO User add phonenumber
		}

		if (userinfo.getRealname() != null) {
			user.setRealname(userinfo.getRealname());
		}

		// if(userinfo.get)

		return "success";
	}

	@RequestMapping(value = "/{username}/info", method = RequestMethod.GET)
	public User info(@PathVariable("username") User user) {

		return user;
	}

	@RequestMapping(value = "/stars", method = RequestMethod.GET)
	public List<Item> stars(Principal principal) {

		User user = userRepository.findOne(principal.getName());
		return user.getStars();
	}

	@RequestMapping(value = "/items", method = RequestMethod.GET)
	public Set<BaseItem> items(Principal principal) {
		User user = userRepository.findOne(principal.getName());

		for (BaseItem item : user.getItems()) {

			if (item instanceof Item) {

			} else if (item instanceof CommentItem) {

			}

		}
		return null;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String log() {

		return "login success";

	}
	
	@RequestMapping(value = "/changepassword",method = RequestMethod.POST)
	public String changepass(@RequestParam("rawpassword")String rawpassword,@RequestParam("newpassword")String newpassword,Principal principal){
		User user = userRepository.findOne(principal.getName());
		String pass = user.getPassword();
		if(encoder.matches(pass, rawpassword)){
			user.setPassword(encoder.encode(newpassword));
			userRepository.save(user);
			return "change success";
		}else{
			return "raw password is wrong";
		}
	}

}
