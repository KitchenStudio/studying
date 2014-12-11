package study.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import study.model.FileItem;
import study.model.Item;
import study.model.Message;
import study.model.User;
import study.repository.FileItemRepository;
import study.repository.ItemRepository;
import study.repository.UserRepository;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private FileItemRepository fileItemRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	List<Item> list(Pageable pageable) {
		Page<Item> page = itemRepository.findAll(pageable);

		return page.getContent();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	Message post(String content) {

		Item item = new Item(content);
		itemRepository.save(item);

		return new Message(0, "success");
	}

	/*
	 * FIXME 在这里每一次判断 upload 文件夹是否存在不是一个好的方式 我们需要在应用启动的时候做这个判断
	 */
	@RequestMapping(value = "/files", method = RequestMethod.POST)
	Message postWithFiles(String content,
			@RequestParam("file") ArrayList<MultipartFile> files,
			HttpServletRequest request) {

		String syspath = request.getServletContext().getRealPath("/");
		System.out.println(syspath);
		File upload = new File(syspath + "/upload");
		String path = request.getContextPath();

		if (!upload.isDirectory()) {
			upload.mkdir();
		}
		
		ArrayList<FileItem> fileItems = new ArrayList<>();

		for (MultipartFile file : files) {

			try {
				String filename = file.getOriginalFilename();
				File destFile = File.createTempFile("study-",
						"-" + filename, upload);
				file.transferTo(destFile);
				String url = path + "/upload/" + destFile.getName();
				FileItem fileItem = new FileItem(filename, url, FileItem.OTHER);
				fileItems.add(fileItem);
			} catch (IOException e) {
				e.printStackTrace();
				return new Message(1, "failure");
			}
		}
		
		Item item = new Item(content);
		item = itemRepository.save(item);
		
		for (FileItem fileItem : fileItems) {
			fileItem.setItem(item);
			fileItemRepository.save(fileItem);
		}

		return new Message(0, "success");
	}

	// TODO 这边更新 赞的数量 需要写成一个事务
	// 赞即收藏
	@RequestMapping(value = "/{id}/star", method = RequestMethod.PUT)
	Message star(@PathVariable("id") Item item, HttpServletRequest request) {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		User user = userRepository.findOne(username);
		if (user.getStars().contains(item)) {
			return new Message(2, "has been starred");
		} else {
			item.setStarNumber(item.getStarNumber() + 1);
			item = itemRepository.save(item);
			user.getStars().add(item);
			userRepository.save(user);
		}
		;

		return new Message(0, "success");
	}
}
