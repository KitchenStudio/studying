package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	

}
