package study.model;

import java.util.ArrayList;
import java.util.List;

import study.entity.FileItem;

public class DetailItem {

	private String userFigure;

	private List<study.entity.FileItem> files = new ArrayList<>();

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

	public void setFiles(List<study.entity.FileItem> list) {
		this.files = list;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
