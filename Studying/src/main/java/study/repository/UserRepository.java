package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.entity.User;
import java.lang.String;

public interface UserRepository extends JpaRepository<User, String> {
	
}
