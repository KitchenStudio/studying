package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.entity.BaseItem;

public interface BaseItemRepository extends JpaRepository<BaseItem, Long> {

}
