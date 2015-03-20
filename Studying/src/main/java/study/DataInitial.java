package study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import study.model.Item;
import study.model.User;
import study.repository.ItemRepository;
import study.repository.UserRepository;

@Configuration
public class DataInitial {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory
			.getLogger(DataInitial.class);

	@Bean
	public String InitData() {
		logger.info("Context Refreshed!");

		User user = userRepository.findOne("18366116016");

		for (int i = 0; i < 44; i++) {
			Item item = new Item(
					"学友即在一起共同学习、读书的人，是指学习上的朋友。在同学中感情较深的并建立了友谊的学生、学员对同伴的称呼。它不同于一般的同学。它的圈子是在学生和学员中，有学习情感交往的人。");
			item.setOwner(user);
			item.setStarNumber(0L);

			itemRepository.save(item);
		}
		
		return SUCCESS;
	}
	
	private static final String SUCCESS = "INIT_OK";

}
