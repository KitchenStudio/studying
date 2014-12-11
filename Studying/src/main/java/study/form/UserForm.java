package study.form;

import org.hibernate.validator.constraints.Email;

public class UserForm {
	
	private String realname;
	
	private String nickname;
	
	@Email
	private String mail;
	
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	public String getRealname() {
		return realname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
		return "UserForm [realname=" + realname + ", nickname=" + nickname
				+ ", mail=" + mail + "]";
	}
	
}
