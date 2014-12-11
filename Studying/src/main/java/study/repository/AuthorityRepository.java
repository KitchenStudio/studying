package study.repository;

import org.springframework.data.repository.CrudRepository;

import study.model.Authority;

public interface AuthorityRepository extends CrudRepository<Authority, String> {

}
