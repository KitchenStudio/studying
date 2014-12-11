package study.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import study.model.Item;
import study.model.Message;
import study.repository.ItemRepository;

@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemRepository itemRepository;

	@RequestMapping("/list")
	List<Item> list(Pageable pageable) {
		Page<Item> page =  itemRepository.findAll(pageable);
		
		return page.getContent();
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	Message add(String content) {
		
		Item item = new Item(Item.MESSAGE, content, null);
		itemRepository.save(item);
		
		return new Message(0, "success");
	}
	
	/*
	 * FIXME 在这里每一次判断 upload 文件夹是否存在不是一个好的方式
	 * 我们需要在应用启动的时候做这个判断
	 */
	@RequestMapping(value = "/add_file", method = RequestMethod.POST)
	Message addFile(String content, MultipartFile file, HttpServletRequest request) {

		String path = request.getServletContext().getRealPath("/");
		File upload = new File(path + "/upload");

		if (!upload.isDirectory()) {
			upload.mkdir();
		}

		try {
			File destFile= File.createTempFile("file-", "-" + file.getOriginalFilename(), upload);
			file.transferTo(destFile);
			String url = request.getServletContext().getContextPath() + "/upload/" + destFile.getName() ;
			itemRepository.save(new Item(Item.FILE, content, url));
		} catch (IOException e) {
			e.printStackTrace();
			return new Message(1, "failure");
		}

		return new Message(0, "success");
	}
}
