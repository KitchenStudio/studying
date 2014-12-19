package study.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

/**
 * 这个类是 负责帖子的交互逻辑
 * 
 * 现在，这个帖子支持包含消息，同时可以用上传图片、音频和其他文件
 * 
 * @author seal
 *
 */
@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

	/*
	 * 以下为仓库实例，@Autowired 的注解使得Spring可以为我们提供这些对象
	 */
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private FileItemRepository fileItemRepository;

	/**
	 * 
	 * 客户端可以以这样的形式调用
	 * /item/?page=0&size=20&sort=id
	 * page 页号
	 * size 每页多少内容
	 * sort 以那些属性排序（如果指定多个，则这样做 sort=username&sort=password）
	 * 
	 * 但是，这三个值都可以省略
	 * 省略时默认就是上面举例的这个形式
	 * 
	 * 可以使用命令行工具 curl 进行测试，配合python的json.tool得到一个好看的形式
	 * curl -u 18366116016:..xiao -X GET 127.0.0.1:8080/item/  | python -mjson.tool
	 * 
	 * @param pageable 客户端传来的参数，即上面的page、size、sort，可以查看PageRequest这个类
	 * 的构造函数，PageRequest(page, size, sort)，而 PageRequest 是 Pageable 的实现类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	List<Item> list(Pageable pageable) {
		Page<Item> page = itemRepository.findAll(pageable);

		return page.getContent();
	}

	/**
	 * curl -u 18366116016:..xiao -X POST -d "content=hello" 127.0.0.1:8080/item/ | python -mjson.tool
	 * 
	 * @param content
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	Message post(String content, Principal principal) {
		User user = userRepository.findOne(principal.getName());

		Item item = new Item(content);
		item.setOwner(user);
		itemRepository.save(item);

		return new Message(0, "success");
	}

	/*
	 * FIXME 在这里每一次判断 upload 文件夹是否存在不是一个好的方式 我们需要在应用启动的时候做这个判断
	 */
	@RequestMapping(value = "/files", method = RequestMethod.POST)
	Message postWithFiles(String content,
			@RequestParam("file") ArrayList<MultipartFile> files,
			HttpServletRequest request,
			Principal principal) {

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
		
		User user = userRepository.findOne(principal.getName());

		Item item = new Item(content);
		item.setOwner(user);
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

		return new Message(0, "success");
	}
}
