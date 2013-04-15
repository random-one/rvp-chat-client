package ChatClient;


public class FileMessage extends Message implements java.io.Serializable, P2PMessage {

	private static final long serialVersionUID = 1L;
	private String fileName;
	private byte[] buffer;
	//TODO: add compareTo to determine if a file with this name is transfered

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public FileMessage(String sender, String receiver, String fileName) {
		super(sender, receiver, msgType.FILE_MESSAGE);
		this.fileName = fileName;
	}
}
