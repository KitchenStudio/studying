package study.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 * 类型需要更改成更具体的类型
 * 
 * 比如 PPT、音频、视频等等
 * 
 * @author seal
 *
 */
@Entity
public class Item {
	
	@Id
	@GeneratedValue
	private Long id;

	private String type;
	
	private String content;
	
	private String url;
	
	protected Item() {
		
	}
	
	public Item(String type, String content, String url) {
		this.type = type;
		this.content = content;
		this.url = url;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 当为文件类型的时候是 URL，
	 * @return
	 */
	public String getContent() {
		return content;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public static String MESSAGE = "MESSAGE";
	public static String FILE = "FILE";
	
}
