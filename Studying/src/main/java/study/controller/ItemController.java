package study.controller;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
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

	private static final Logger log = LoggerFactory
			.getLogger(ItemController.class);

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
	 * 客户端可以以这样的形式调用 /api/v1/item/?page=0&size=20&sort=id page 页号 size 每页多少内容
	 * sort 以那些属性排序（如果指定多个，则这样做 sort=username&sort=password）
	 * 
	 * 但是，这三个值都可以省略 省略时默认就是上面举例的这个形式
	 * 
	 * 可以使用命令行工具 curl 进行测试，配合python的json.tool得到一个好看的形式 curl -u
	 * 18366116016:..xiao -X GET 127.0.0.1:8080/api/v1/item/ | python
	 * -mjson.tool
	 * 
	 * @param pageable
	 *            客户端传来的参数，即上面的page、size、sort，可以查看PageRequest这个类
	 *            的构造函数，PageRequest(page, size, sort)，而 PageRequest 是 Pageable
	 *            的实现类
	 * 
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	List<Item> list(Pageable pageable) {
		System.out.println("method is ok");
		Page<Item> page = itemRepository.findAll(pageable);
		
		
		return page.getContent();
	}

	@RequestMapping(value="/loadmore",method=RequestMethod.POST)
	List<Item> loadMore(int number){
		Pageable pageable = new PageRequest(number, 20);
		Page<Item> page = itemRepository.findAll(pageable);
		System.out.println(page.getContent());
		return page.getContent();
	}
	/**
	 * curl -u 18366116016:..xiao -X POST -d "content=hello"
	 * 127.0.0.1:8080/api/v1/item/ | python -mjson.tool
	 * 
	 * @param content
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
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
	@RequestMapping(value="/file",method= RequestMethod.POST)
	Message uploadFile(@RequestParam("uploadfile")ArrayList<MultipartFile> files,HttpServletRequest request){
		System.out.println("方法执行那个了");
		saveFile(files, request);
		return new Message(1, "success");
	}
	@RequestMapping(value = "/files", method = RequestMethod.POST)
	Message postWithFiles(String content, String subject,
			@RequestParam("file") ArrayList<MultipartFile> files,
			HttpServletRequest request, Principal principal) {
		String syspath = request.getServletContext().getRealPath("/");
		File upload = new File(syspath + "/upload");
		String path = request.getContextPath();
		if (!upload.isDirectory()) {
			upload.mkdir();

		}
		String url = null;
		ArrayList<FileItem> fileItems = new ArrayList<>();

		for (MultipartFile file : files) {

			try {
				String filename = file.getOriginalFilename();
				File destFile = File.createTempFile("study-", "-" + filename,
						upload);

				System.out.println(destFile.toString());
				file.transferTo(destFile);
				url = path + "/upload/" + destFile.getName();

				FileItem fileItem = new FileItem(filename, url, FileItem.OTHER);
				System.out.println(isPicture(destFile.toString())+"true or false");
				if (isPicture(destFile.toString())) {
					BufferedImage buffered = ImageIO.read(destFile);
					BufferedImage bufferimage = scale(buffered,
							BufferedImage.TYPE_INT_RGB,
							buffered.getWidth() / 2, buffered.getHeight() / 2,
							0.5, 0.5);
					String[] fileresize = destFile.toString().split("\\.");
					ImageIO.write(bufferimage, fileresize[1].toString(),
							new File(fileresize[0] + "resize" + "."
									+ fileresize[1]));
				}
				fileItems.add(fileItem);
			} catch (IOException e) {
				e.printStackTrace();
				return new Message(1, "failure");
			}

		}

		User user = userRepository.findOne(principal.getName());

		Item item = new Item(content);
		item.setOwner(user);
		item.setSubject(subject);
		item = itemRepository.save(item);

		for (FileItem fileItem : fileItems) {
			fileItem.setItem(item);
			fileItemRepository.save(fileItem);

		}

		return new Message(0, "success");
	}
	
	private void saveFile(ArrayList<MultipartFile> files,HttpServletRequest request){
		String syspath = request.getServletContext().getRealPath("/");
		File upload = new File(syspath + "/upload");
		String path = request.getContextPath();
		if (!upload.isDirectory()) {
			upload.mkdir();

		}
		String url = null;
		ArrayList<FileItem> fileItems = new ArrayList<>();

		for (MultipartFile file : files) {

			try {
				String filename = file.getOriginalFilename();
				File destFile = File.createTempFile("study-", "-" + filename,
						upload);

				System.out.println(destFile.toString());
				file.transferTo(destFile);
				url = path + "/upload1/" + destFile.getName();

				FileItem fileItem = new FileItem(filename, url, FileItem.OTHER);
				System.out.println(isPicture(destFile.toString())+"true or false");
				if (isPicture(destFile.toString())) {
					BufferedImage buffered = ImageIO.read(destFile);
					BufferedImage bufferimage = scale(buffered,
							BufferedImage.TYPE_INT_RGB,
							buffered.getWidth() / 2, buffered.getHeight() / 2,
							0.5, 0.5);
					String[] fileresize = destFile.toString().split("\\.");
					ImageIO.write(bufferimage, fileresize[1].toString(),
							new File(fileresize[0] + "resize" + "."
									+ fileresize[1]));
				}else{
					
				}
				fileItems.add(fileItem);
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
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

	/**
	 * scale image
	 * 
	 * @param sbi
	 *            image to scale
	 * @param imageType
	 *            type of image
	 * @param dWidth
	 *            width of destination image
	 * @param dHeight
	 *            height of destination image
	 * @param fWidth
	 *            x-factor for transformation / scaling
	 * @param fHeight
	 *            y-factor for transformation / scaling
	 * @return scaled image
	 */
	public static BufferedImage scale(BufferedImage sbi, int imageType,
			int dWidth, int dHeight, double fWidth, double fHeight) {
		BufferedImage dbi = null;
		if (sbi != null) {
			dbi = new BufferedImage(dWidth, dHeight, imageType);
			Graphics2D g = dbi.createGraphics();
			AffineTransform at = AffineTransform.getScaleInstance(fWidth,
					fHeight);
			g.drawRenderedImage(sbi, at);
		}
		return dbi;
	}

	public boolean isPicture(String filename) {
		String imgeArray[] = { "bmp", "dib", "gif", "jfif", "jpe", "jpeg",
				"jpg", "png", "tif", "tiff", "ico" };

		for (int i = 0; i < imgeArray.length; i++) {
			if (filename.split("\\.")[1].equals(imgeArray[i])) {
				return true;
			}
		}
		return false;
	}
}