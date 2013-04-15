package ChatClient;


public class FileMessage extends Message implements java.io.Serializable, P2PMessage {

	private static final long serialVersionUID = 1L;
	private String fileName;
	private int size;
	private byte[] buffer = new byte[10240];

	public FileMessage() {
		setType(Message.msgType.FILE_MESSAGE);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
