package study.model;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * TODO 使用更简单的用户权限，而不是使用 Authorities 和 User 两个实体 是不是有必要做这样一次变化呢？
 * 
 * @author seal
 *
 */
@JsonIgnoreProperties({ "authorities", "accountNonExpired", "accountNonLocked",
		"credentialsNonExpired", "enabled", "password" })
@Entity
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	private String password;

	private boolean accountNonExpired;

	private boolean accountNonLocked;

	private boolean credentialsNonExpired;

	private boolean enabled;

	private String realname;

	private String nickname;

	private String mail;

	@JoinTable(name = "USER_AUTH")
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Authority> authorities;

	@PrePersist
	protected void onCreated() {
		accountNonExpired = true;
		accountNonLocked = true;
		credentialsNonExpired = true;
		enabled = true;
	}

	protected User() {
	}

	public User(String username, String password, Set<Authority> authorities) {
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Collection<Authority> getAuthorities() {
		return authorities;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getRealname() {
		return realname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMail() {
		return mail;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password
				+ ", accountNonExpired=" + accountNonExpired
				+ ", accountNonLocked=" + accountNonLocked
				+ ", credentialsNonExpired=" + credentialsNonExpired
				+ ", enabled=" + enabled + ", realname=" + realname
				+ ", nickname=" + nickname + ", authorities=" + authorities
				+ "]";
	}
}
