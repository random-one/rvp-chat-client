package ChatClient;

public class SystemMessage extends Message implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String userName;
	private String content;
	private systemMsgType type;
	
	public enum systemMsgType { SYSTEM_LOGIN_MESSAGE, SYSTEM_LOGOUT_MESSAGE, SYSTEM_ERROR_MESSAGE, SYSTEM_USERLIST_MESSAGE };

	public SystemMessage(String sender, String receiver, String userName)
	{
		super(sender, receiver, Message.msgType.SYSTEM_MESSAGE);
		this.userName = userName;
	}

	public SystemMessage(String sender, String receiver, String userName, String content, systemMsgType type)
	{
		super(sender, receiver, Message.msgType.SYSTEM_MESSAGE);
		this.userName = userName;
		this.content = content;
		this.type = type;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getContent()
	{
		return content;
	}
	
	public systemMsgType getSytemMessageType()
	{
		return type;
	}
}
