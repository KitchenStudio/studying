package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.model.User;

public interface UserRepository extends JpaRepository<User, String> {

}
