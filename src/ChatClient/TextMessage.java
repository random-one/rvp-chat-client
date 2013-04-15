package ChatClient;

public class TextMessage extends Message implements java.io.Serializable, P2PMessage {

	private static final long serialVersionUID = 1L;
	private String content;

	public TextMessage(String sender, String receiver, String content) {
		super(sender, receiver, msgType.TEXT_MESSAGE);
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}	
}
