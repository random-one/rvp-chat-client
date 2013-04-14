package ChatClient;

import java.util.Arrays;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;

public class MessageHandler {
	private JTextArea textArea;
	private DefaultListModel userModel;

	public MessageHandler(JTextArea textArea, DefaultListModel userModel)
	{
		this.textArea = textArea;
		this.userModel = userModel;
	}

	protected void handleMessage(Message m)
	{
		if (m.getType() == Message.msgType.TEXT_MESSAGE)
			textArea.append("\n" + m.getSender() + ": "  + ((TextMessage)m).getContent());
		if (m.getType() == Message.msgType.SYSTEM_MESSAGE) {
			SystemMessage sm = (SystemMessage)m;
			if (sm.getSytemMessageType() == SystemMessage.systemMsgType.SYSTEM_USERLIST_MESSAGE) {
				String[] users = sm.getContent().split(",");
				Arrays.sort(users);
				for (String user : users)
					if (!userModel.contains(user))
						userModel.addElement(user);
			}
		}
	}
}
