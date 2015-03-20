package study.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BaseItem {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String content;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private User owner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
	private Set<FileItem> files;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Set<FileItem> getFiles() {
		return files;
	}

	public void setFiles(Set<FileItem> files) {
		this.files = files;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@PrePersist
	void onUpdate() {
		createdTime = new Date();
	}
}
