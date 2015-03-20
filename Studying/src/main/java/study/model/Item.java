package study.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * 类型需要更改成更具体的类型
 * 
 * 比如 PPT、音频、视频等等
 * 
 * 求助的时候，可能需要 图片与语音同时存在
 * 
 * 每一个消息都有可能是 文字、图片、语音 与 其他文件
 * 
 * 那么，我们以文字消息作为基本单位，文字消息可能跟随着多个文件， 文件可以是图片、语音以及其他的文件
 * 
 * @author seal
 *
 */
@SuppressWarnings("unused")
@JsonIgnoreProperties("starBys")
@Entity
public class Item {

	@Id
	@GeneratedValue
	private Long id;

	private String content;
	
	private String subject;

    private Long praiseNumber;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
	private Set<FileItem> fileItems;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@ManyToMany(mappedBy = "stars")
	private Set<User> starBys;

	private Long starNumber;


	@ManyToOne
	@JoinColumn(name = "CUST_ID")
	private User owner;

	/*
	 * 第一次保存实体前
	 */
	@PrePersist
	void onUpdate() {
		starNumber = 0L;
		createdTime = new Date();
		praiseNumber = 0L;
	}

	protected Item() {

	}

	public Item(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 当为文件类型的时候是 URL，
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	public void setStarBys(Set<User> starBys) {
		this.starBys = starBys;
	}

	public Set<User> getStarBys() {
		return starBys;
	}

	public void setStarNumber(Long starNumber) {
		this.starNumber = starNumber;
	}

	public Long getStarNumber() {
		return starNumber;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public Set<FileItem> getFileItems() {
		return fileItems;
	}

	public void setFileItems(Set<FileItem> fileItems) {
		this.fileItems = fileItems;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public User getOwner() {
		return owner;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
    public Long getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(Long praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

}
