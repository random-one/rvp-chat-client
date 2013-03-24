package ChatClient;

public class FileMessage extends Message implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String fileName;
	private byte[] buffer;

	public FileMessage(String sender, String receiver, String fileName) {
		super(sender, receiver, msgType.FILE_MESSAGE);
		this.fileName = fileName;
	}
}
