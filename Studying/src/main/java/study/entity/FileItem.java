package study.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("item")
@Entity
public class FileItem {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String filename;
	
	private String url;
	
	private String type;
	
	@ManyToOne
	@JoinColumn(name="ITEM_ID", nullable=false)
	private Item item;

	protected FileItem() {
		
	}
	
	public FileItem(String filename, String url, String type) {
		this.filename = filename;
		this.url = url;
		this.type = type;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}
	
	public static final String PICTURE = "PICTURE";
	public static final String AUDIO = "AUDIO";
	public static final String OTHER = "OTHER";
}
