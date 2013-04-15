package ChatClient;

import java.util.Arrays;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;

public class MessageHandler implements IMessageHandler {
	private JTextArea textArea;
	private DefaultListModel userModel;

	public MessageHandler(JTextArea textArea, DefaultListModel userModel)
	{
		this.textArea = textArea;
		this.userModel = userModel;
	}

	public void handleMessage(Message m)
	{
		if (m instanceof TextMessage) {
			textArea.append("\n" + m.getSender() + ": "  + ((TextMessage)m).getContent());
		}
		if (m instanceof FileMessage) {
			FileMessage fm = (FileMessage) m;
			System.out.println("file message received:" + fm.getFileName());
		}
		if (m instanceof SystemMessage) {
			SystemMessage sm = (SystemMessage)m;
			if (sm.getSytemMessageType() == SystemMessage.systemMsgType.SYSTEM_USERLIST_MESSAGE) {
				String[] users = sm.getContent().split(",");
				Arrays.sort(users);
				userModel.clear();
				for (String user : users)
					userModel.addElement(user);
			}
		}
	}
}
