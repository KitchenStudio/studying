package study.controller;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

import com.fasterxml.jackson.databind.util.BeanUtil;

import study.entity.BaseItem;
import study.entity.CommentItem;
import study.entity.FileItem;
import study.entity.Item;
import study.entity.Message;
import study.entity.User;
import study.model.Comment;
import study.model.DetailItem;
import study.model.ListItem;
import study.repository.FileItemRepository;
import study.repository.ItemRepository;
import study.repository.UserRepository;
import study.service.PictureService;

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
	private PictureService pictureService;

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
	List<ListItem> get(Pageable pageable) {
		Page<Item> page = itemRepository.findAll(pageable);
		List<ListItem> items = new ArrayList<>();
		for (Item item : page) {
			ListItem listItem = new ListItem();
			BeanUtils.copyProperties(item, listItem);
			listItem.setNickname(item.getOwner().getNickname());
			listItem.setStars(0);
			listItem.setUps(0);
			listItem.setComments(item.getComments().size());
			listItem.setCreatedTime(item.getCreatedTime());
			listItem.setUserId(item.getOwner().getUsername());

			items.add(listItem);
		}

		return items;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	DetailItem get(@PathVariable("id") Item item) {

		DetailItem detailItem = new DetailItem();

		if (item.getOwner().getFigure() != null)
			detailItem.setUserFigure(item.getOwner().getFigure().getUrl());
		if (item.getFileItems() != null) {
			detailItem.setFiles(item.getFileItems());
		}

		Set<CommentItem> comments = item.getComments();
		for (CommentItem commentItem : comments) {
			Comment comment = new Comment();
			comment.setContent(commentItem.getContent());
			comment.setCreatedTime(commentItem.getCreatedTime());
			if (commentItem.getOwner().getFigure() != null)
				comment.setUserFigure(commentItem.getOwner().getFigure()
						.getUrl());
			comment.setUsername(commentItem.getOwner().getNickname());

			for (FileItem fileItem : commentItem.getFiles()) {
				study.model.FileItem file = new study.model.FileItem();
				BeanUtils.copyProperties(fileItem, file);
				comment.getFiles().add(file);
			}
			detailItem.getComments().add(comment);
		}
		return detailItem;
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

		Item item = new Item();
		item.setContent(content);
		item.setOwner(user);
		itemRepository.save(item);

		return new Message(0, "success");
	}

	// /*
	// * FIXME 在这里每一次判断 upload 文件夹是否存在不是一个好的方式 我们需要在应用启动的时候做这个判断
	// */
	//
	// @RequestMapping(value = "/files", method = RequestMethod.POST)
	// Message postWithFiles(String content, String subject,
	// @RequestParam(value = "file",required = false) ArrayList<MultipartFile>
	// files,
	// HttpServletRequest request, Principal principal) {
	// String syspath = request.getServletContext().getRealPath("/");
	// File upload = new File(syspath + "/upload");
	// String path = request.getContextPath();
	// if (!upload.isDirectory()) {
	// upload.mkdir();
	//
	// }
	// String url = null;
	// ArrayList<FileItem> fileItems = new ArrayList<>();
	// if (files != null) {
	// for (MultipartFile file : files) {
	//
	// try {
	// String filename = file.getOriginalFilename();
	// File destFile = File.createTempFile("study-", "-"
	// + filename, upload);
	//
	// System.out.println(destFile.toString());
	// file.transferTo(destFile);
	// url = path + "/upload/" + destFile.getName();
	//
	// FileItem fileItem = new FileItem(filename, url,
	// FileItem.PICTURE);
	// if (pictureService.isPicture(destFile.toString())) {
	// BufferedImage buffered = ImageIO.read(destFile);
	// BufferedImage bufferimage = pictureService.scale(
	// buffered, BufferedImage.TYPE_INT_RGB,
	// buffered.getWidth() / 2,
	// buffered.getHeight() / 2, 0.5, 0.5);
	// String[] fileresize = destFile.toString().split("\\.");
	// ImageIO.write(bufferimage, fileresize[1].toString(),
	// new File(fileresize[0] + "resize" + "."
	// + fileresize[1]));
	// }
	// fileItems.add(fileItem);
	// } catch (IOException e) {
	// e.printStackTrace();
	// return new Message(1, "failure");
	// }
	//
	// }
	// }
	// User user = userRepository.findOne(principal.getName());
	//
	// Item item = new Item();
	// item.setContent(content);
	// item.setOwner(user);
	// item.setSubject(subject);
	// item = itemRepository.save(item);
	//
	// for (FileItem fileItem : fileItems) {
	// fileItem.setItem(item);
	// fileItemRepository.save(fileItem);
	//
	// }
	//
	// return new Message(0, "success");
	// }

	// 存储上传item的文字内容
	@RequestMapping(value = "/saveinfo", method = RequestMethod.POST)
	String uploadinfo(String content, String subject,
			HttpServletRequest request, Principal principal) {

		User user = userRepository.findOne(principal.getName());

		Item item = new Item();
		item.setContent(content);
		item.setOwner(user);
		item.setSubject(subject);
		item = itemRepository.save(item);
		return item.getId().toString();
	}

	// 存储上传item的文件内容
	@RequestMapping(value = "/{id}/savefile", method = RequestMethod.POST)
	Message saveFile(
			@RequestParam(value = "file", required = false) ArrayList<MultipartFile> files,
			@PathVariable("id") Item item, HttpServletRequest request,
			Principal principal) {

		String syspath = request.getServletContext().getRealPath("/");
		File upload = new File(syspath + "/upload");
		String path = request.getContextPath();
		if (!upload.isDirectory()) {
			upload.mkdir();

		}
		String url = null;
		ArrayList<FileItem> fileItems = new ArrayList<>();
		if (files != null) {
			for (MultipartFile file : files) {

				try {
					String filename = file.getOriginalFilename();
					File destFile = File.createTempFile("study-", "-"
							+ filename, upload);

					file.transferTo(destFile);
					url = path + "/upload/" + destFile.getName();

					FileItem fileItem = null;
					System.out.println(destFile.getPath() + "path");
					if (pictureService.isPicture(destFile.getPath())) {

						BufferedImage buffered = ImageIO.read(destFile);
						int width = buffered.getWidth() / 2;
						int height = buffered.getHeight() / 2;
						BufferedImage bufferimage = pictureService.scale(
								buffered, BufferedImage.TYPE_INT_RGB, width,
								height, 0.5, 0.5);
						String[] fileresize = destFile.toString().split("\\.");
						String resizePath = fileresize[0] + "resize" + "."
								+ fileresize[1];
						String cutPath = fileresize[0] + "resizecut" + "."
								+ fileresize[1];
						ImageIO.write(bufferimage, fileresize[1].toString(),
								new File(resizePath));
						if (width < height) {
							cutCenterImage(resizePath, cutPath, width, width);
						} else {
							cutCenterImage(resizePath, cutPath, height, height);
						}
						System.out.println("is picture");
						fileItem = new FileItem(filename, url, FileItem.PICTURE);
					} else if (pictureService.isSound(destFile.getPath())) {
						System.out.println("is sound");
						fileItem = new FileItem(filename, url, FileItem.AUDIO);
					} else {
						System.out.println("is file");
						fileItem = new FileItem(filename, url, FileItem.FILE);
					}
					fileItems.add(fileItem);
				} catch (IOException e) {
					e.printStackTrace();
					return new Message(1, "failure");
				}
				for (FileItem fileItem : fileItems) {
					fileItem.setItem(item);
					fileItemRepository.save(fileItem);

				}
			}
		}
		return new Message(0, "success");
	}

	// TODO 这边更新 赞的数量 需要写成一个事务
	// 赞即收藏
	@RequestMapping(value = "/{id}/star", method = RequestMethod.POST)
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

	/*
	 * 根据尺寸图片居中裁剪
	 */
	public static void cutCenterImage(String src, String dest, int w, int h)
			throws IOException {
		String ext = src.substring(src.lastIndexOf(".") + 1);

		// ImageReader声称能够解码指定格式
		Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(ext);
		ImageReader reader = it.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		int imageIndex = 0;
		Rectangle rect = new Rectangle((reader.getWidth(imageIndex) - w) / 2,
				(reader.getHeight(imageIndex) - h) / 2, w, h);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, ext, new File(dest));

	}

	/*
	 * 得到收藏的列表
	 */
	@RequestMapping(value = "/{username}/collectionlist", method = RequestMethod.GET)
	List<ListItem> getCollection(@PathVariable("username") User user) {
		System.out.println(user.getUsername());
		List<Item> listcollections = user.getStars();
		List<ListItem> items = new ArrayList<ListItem>();
		for (Item item : listcollections) {
			ListItem listItem = new ListItem();
			BeanUtils.copyProperties(item, listItem);
			listItem.setNickname(item.getOwner().getNickname());
			listItem.setStars(item.getStarNumber());
			listItem.setUps(0);
			listItem.setComments(item.getComments().size());
			listItem.setCreatedTime(item.getCreatedTime());
			listItem.setUserId(item.getOwner().getUsername());

			items.add(listItem);
		}
		return items;

	}

	/*
	 * 搜索得到的列表
	 */
	@RequestMapping(value="/search",method = RequestMethod.GET)
	List<ListItem> getSearchList(@RequestParam("keyword")String keyword){
		List<Item> items = itemRepository.findByContentContainsOrSubjectContains(keyword,keyword);
		List<ListItem> listitems = new ArrayList<>();
		for (Item item : items) {
			ListItem listItem = new ListItem();
			BeanUtils.copyProperties(item, listItem);
			listItem.setNickname(item.getOwner().getNickname());
			listItem.setStars(0);
			listItem.setUps(0);
			listItem.setComments(item.getComments().size());
			listItem.setCreatedTime(item.getCreatedTime());
			listItem.setUserId(item.getOwner().getUsername());

			listitems.add(listItem);
		}

		return listitems;
	}
	
	 
}