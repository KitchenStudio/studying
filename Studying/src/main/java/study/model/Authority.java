package study.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

/**
 * 权限管理 实体
 * @author seal
 *
 */
@Entity
public class Authority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	public Authority() {
	}

	public Authority(String authority) {
		this.authority = authority;
	}

	@Id
	private String authority;

	@ManyToMany(mappedBy = "authorities")
	private Set<User> users;

	@Override
	public String getAuthority() {
		return authority;
	}

	public Set<User> getUsers() {
		return users;
	}

}
