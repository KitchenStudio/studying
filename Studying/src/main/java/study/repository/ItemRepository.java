package study.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import study.entity.Item;
import study.model.ListItem;
import java.util.Set;
import java.lang.String;

public interface ItemRepository extends JpaRepository<Item, Long> {
	
	List<Item> findByContentContainsOrSubjectContains(String content,String subject);
	


}
