package study.repository;

import org.springframework.data.repository.CrudRepository;

import study.entity.FileItem;

public interface FileItemRepository extends CrudRepository<FileItem, Long>{

}
