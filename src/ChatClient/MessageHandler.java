package ChatClient;

import javax.swing.JTextArea;

public class MessageHandler {
	private JTextArea textArea;

	public MessageHandler(JTextArea textArea)
	{
		this.textArea = textArea;
	}

	protected void handleMessage(Message m)
	{
		if (m.getType() == Message.msgType.TEXT_MESSAGE)
			textArea.append("\n" + m.getSender() + ": "  + ((TextMessage)m).getContent());
	}
}
