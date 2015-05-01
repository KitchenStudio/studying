package study.model;

/**
 * TODO need verify
 * @author seal
 *
 */
public class Userinfo {

	private String email;
	private String nickname;
	private String password;
	private String realname;
	private String phonenumber;
	private Integer age;
	private String sex;
	private String userFigure;

	public String getUserFigure() {
		return userFigure;
	}

	public void setUserFigure(String userFigure) {
		this.userFigure = userFigure;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
