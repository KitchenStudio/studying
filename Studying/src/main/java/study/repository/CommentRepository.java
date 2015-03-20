package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.entity.CommentItem;

public interface CommentRepository extends JpaRepository<CommentItem, Long> {

}
