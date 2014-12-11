package study.model;

public class Message {
	
	private long code;
	
	private String content;

	public Message(long code, String content) {
		this.code = code;
		this.content = content;
	}
	
	public void setCode(long code) {
		this.code = code;
	}
	
	public long getCode() {
		return code;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

}
