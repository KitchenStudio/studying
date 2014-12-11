package study.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;

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

	@Column
	private Date createdTime;

	@Override
	public String getAuthority() {
		return authority;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	@PrePersist
	protected void onCreated() {
		createdTime = new Date();
	}

	public Set<User> getUsers() {
		return users;
	}

}
