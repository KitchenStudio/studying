package study.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class BaseItem {

	@Id
	@GeneratedValue
	private Long id;

	private String content;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
	private Set<FileItem> files;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<FileItem> getFiles() {
		return files;
	}

	public void setFiles(Set<FileItem> files) {
		this.files = files;
	}

}
