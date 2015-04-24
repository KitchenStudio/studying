package study;

import java.util.HashSet;
import java.util.Set;

import javax.print.attribute.HashAttributeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import study.entity.Authority;
import study.entity.CommentItem;
import study.entity.Item;
import study.entity.User;
import study.repository.AuthorityRepository;
import study.repository.CommentRepository;
import study.repository.ItemRepository;
import study.repository.UserRepository;

@Configuration
public class DataInitial {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	private static final Logger logger = LoggerFactory
			.getLogger(DataInitial.class);

	@Bean
	public String initUser() {
//		Authority userAuthority = new Authority("USER");
//		Authority adminAuthority = new Authority("ADMIN");
//		authorityRepository.save(userAuthority);
//		authorityRepository.save(adminAuthority);
//
//		Set<Authority> authorities = new HashSet<>();
//		authorities.add(userAuthority);
//		authorities.add(adminAuthority);
//
//		User user = new User("18366116016", "..xiao", authorities);
//		userRepository.save(user);

		return SUCCESS;
	}

	@Bean
	public String InitData() {
		logger.info("Context Refreshed!");

		User user = userRepository.findOne("18366116016");

		for (int i = 0; i < 2; i++) {
			Item item = new Item();
			item.setContent("学友即在一起共同学习、读书的人，是指学习上的朋友。在同学中感情较深的并建立了友谊的学生、学员对同伴的称呼。它不同于一般的同学。它的圈子是在学生和学员中，有学习情感交往的人。");
			item.setOwner(user);
			item.setStarNumber(0);

			itemRepository.save(item);

			for (int j = 0; j < 4; j++) {
				CommentItem commentItem = new CommentItem();
				commentItem.setContent("真不错的资源");
				commentItem.setOwner(user);
				commentItem.setItem(item);
				commentRepository.save(commentItem);
				item.getComments().add(commentItem);
			}
		}

		return SUCCESS;
	}

	private static final String SUCCESS = "INIT_OK";

}
