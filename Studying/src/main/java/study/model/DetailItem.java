package study.model;

import java.util.ArrayList;
import java.util.List;

public class DetailItem {

	private String userFigure;

	private List<FileItem> files = new ArrayList<>();

	private List<Comment> comments = new ArrayList<>();

	public String getUserFigure() {
		return userFigure;
	}

	public void setUserFigure(String userFigure) {
		this.userFigure = userFigure;
	}

	public List<FileItem> getFiles() {
		return files;
	}

	public void setFiles(List<FileItem> files) {
		this.files = files;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
