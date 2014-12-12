package study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import study.model.Item;
import study.model.User;
import study.repository.ItemRepository;
import study.repository.UserRepository;

/**
 * 注册为组件，可以被Spring扫描到
 * 但是，不知道是不是正确的做法
 * 
 * 或许还是应该弄成一个 Bean 放到配置的java类中？
 * 
 * 这个类是当启动的时候，初始化一些内容，用于开发
 * （实际这个会在上下文刷新的时候触发，但是，我们开发中，没有在代码中进行上下文刷新）
 * @author seal
 *
 */
@Component
public class ContextRefreshedEventHandler implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ContextRefreshedEventHandler.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Context Refreshed!");
		
		User user = userRepository.findOne("18366116016");
		Item item = new Item("学友！");
		item.setOwner(user);
		itemRepository.save(item);
	}

}
