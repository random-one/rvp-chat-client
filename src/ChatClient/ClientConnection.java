package ChatClient;
import java.net.Socket;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class ClientConnection extends Thread {
	private Socket request;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	ClientConnection(Socket client) 
	{
		try
		{
			request = client;
			out = new ObjectOutputStream(request.getOutputStream());
			in = new ObjectInputStream(request.getInputStream());
		} catch (IOException e) {
			System.out.println("Exception creating new Input/output Streams: " + e.getMessage());
			return;
		}
	}

	public void run()
	{
		boolean keepGoing = true;
		Message message = null;

		while(keepGoing) {
			try {
				try {
					message = (Message)in.readObject();
				} catch (EOFException e) {
					try {
						request.close();
						ServerSide.clients.remove(request.getInetAddress().getHostAddress());
						keepGoing = false;
						break;
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}

			if (message.getType() == Message.msgType.TEXT_MESSAGE) {
				TextMessage tm = (TextMessage)message;
				System.out.println("server sent: '" + tm.getContent() + "' to " + tm.getReceiver());

				if (ServerSide.clients.containsKey(tm.getReceiver())) {
					ClientConnection c = ServerSide.clients.get(tm.getReceiver());
					c.writeMessage(message);
				}
			}
		}
	}

	public void close()
	{
		try {
			if (out != null)
				out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		try {
			if (in != null)
				in.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		try {
			if (request != null)
				request.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean writeMessage(Message msg)
	{
		if(!request.isConnected()) {
			close();
			return false;
		}
		try {
			out.writeObject(msg);
		}
		catch(IOException e) {
			System.out.println("Error sending message to " + msg.getReceiver());
		}
		return true;
	}
}
