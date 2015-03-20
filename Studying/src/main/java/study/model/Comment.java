package study.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comment {

	private String username;
	private String userFigure;
	private Date createdTime;
	private String content;
	private List<FileItem> files = new ArrayList<>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserFigure() {
		return userFigure;
	}

	public void setUserFigure(String userFigure) {
		this.userFigure = userFigure;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<FileItem> getFiles() {
		return files;
	}

	public void setFiles(List<FileItem> files) {
		this.files = files;
	}

}
