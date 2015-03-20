package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	

}
