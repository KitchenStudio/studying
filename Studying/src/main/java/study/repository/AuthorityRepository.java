package study.repository;

import org.springframework.data.repository.CrudRepository;

import study.entity.Authority;

public interface AuthorityRepository extends CrudRepository<Authority, String> {

}
