package study.repository;

import org.springframework.data.repository.CrudRepository;

import study.model.FileItem;

public interface FileItemRepository extends CrudRepository<FileItem, Long>{

}
