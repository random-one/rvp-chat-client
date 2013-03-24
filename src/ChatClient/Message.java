package ChatClient;

public class Message implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String sender;
	private String receiver;
	private boolean endReached;
	public enum msgType {TEXT_MESSAGE, FILE_MESSAGE};
	private msgType type;

	public Message() { }
	public Message(String sender, String receiver, msgType type)
	{
		this.sender = sender;
		this.receiver = receiver;
		this.type = type;
	}

	public String getSender()
	{
		return sender;
	}

	public void setSender(String sender)
	{
		this.sender = sender;
	}

	public String getReceiver()
	{
		return receiver;
	}

	public void setReceiver(String receiver)
	{
		this.receiver = receiver;
	}

	public msgType getType() 
	{
		return type;
	}

	public void setType(msgType type) 
	{
		this.type = type;
	}	
}
