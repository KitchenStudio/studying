package study.model;

import java.util.List;

public class DetailItem {

	private String userFigure;

	private List<FileItem> files;

	private List<Comment> comments;

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
