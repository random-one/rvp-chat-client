package ChatClient;

public class SystemMessage extends Message implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String userName;
	private String content;

	public SystemMessage(String sender, String receiver, String userName)
	{
		super(sender, receiver, Message.msgType.SYSTEM_MESSAGE);
		this.userName = userName;
	}

	public SystemMessage(String sender, String receiver, String userName, String content)
	{
		super(sender, receiver, Message.msgType.SYSTEM_MESSAGE);
		this.userName = userName;
		this.content = content;
	}

	public String userName()
	{
		return userName;
	}

	public String content()
	{
		return content;
	}
}
