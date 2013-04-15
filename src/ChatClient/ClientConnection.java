package ChatClient;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class ClientConnection extends Thread {
	private Socket request;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String userName;

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

	void setUserName(String userName)
	{
		this.userName = userName;
	}

	String getUserName()
	{
		return userName;
	}

	public void run()
	{
		boolean keepGoing = true;
		Message message = null;
		Map<String, ClientConnection> clients = ServerSide.clients;
		while(keepGoing) {
			try {
				try {
					message = (Message)in.readObject();
				} catch (EOFException e) {
					try {
						request.close();
						clients.remove(userName);
						//System.out.println("client has disconnected");
//						System.out.println("Clients size after disconnect: " + ServerSide.clients.size() + " keys:" + ServerSide.clients.keySet().toString() + " values: " + ServerSide.clients.values().toString());
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

			if (message.getType() == Message.msgType.SYSTEM_MESSAGE) {
				SystemMessage sm = (SystemMessage) message;
				System.out.println(sm.getUserName() + " " + sm.getContent());
				if (sm.getSytemMessageType() == SystemMessage.systemMsgType.SYSTEM_LOGIN_MESSAGE) {
					userName = sm.getUserName();
//					System.out.println("Username is: " + userName);
//					System.out.println("Enter Clients logging in: " + ServerSide.clients.size() + " keys:" + ServerSide.clients.keySet().toString());
					ClientConnection c = (ClientConnection) clients.get(sm.getSender());
					clients.remove(sm.getSender());
					clients.put(userName, c);
					//FIXME: updateOnlineUsers();
//					System.out.println("Exit Clients logging in: " + ServerSide.clients.size() + " keys:" + ServerSide.clients.keySet().toString());
				}
				if (sm.getSytemMessageType() == SystemMessage.systemMsgType.SYSTEM_LOGOUT_MESSAGE) {
						clients.remove(userName);
						//updateOnlineUsers();
//						System.out.println("Clients size: " + clients.keySet().toString());
						System.out.println("Clients size: " + clients.size() + " on disconnect values: " + clients.values().toString() + " " + clients.keySet().toString());
				}
			}
			if (message instanceof P2PMessage) {
				P2PMessage tm = (P2PMessage)message;
				//System.out.println("server sent: '" + tm.getContent() + "' to " + userName);

				if (clients.containsKey(tm.getReceiver())) {
					ClientConnection c = clients.get(tm.getReceiver());
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

	public synchronized String getOnlineUsers()
	{
		String userList = "";
		for (String user : ServerSide.clients.keySet())
			userList += user + ",";
		return userList;
	}
	
	public synchronized void updateOnlineUsers() {
		String userList = getOnlineUsers();

		for (ClientConnection connection : ServerSide.clients.values()) {
			SystemMessage m = new SystemMessage("", connection.getUserName(), connection.getUserName(), userList, SystemMessage.systemMsgType.SYSTEM_USERLIST_MESSAGE);
			connection.writeMessage(m);
		}
	}
}
