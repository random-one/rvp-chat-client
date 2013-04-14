package ChatClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSide {

	private String clientName;
	private Socket request;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Message message;
	private String server;
	private int port;
	private MessageHandler messageHandler;
	//TODO: add Set<FileMessage> receivedFiles;

	ClientSide(String server, int port, String userName, MessageHandler messageHandler)
	{
		this.server = server;
		this.port = port;
		this.clientName = userName;
		this.messageHandler = messageHandler;
	}

	public boolean start()
	{
		try {
			// TODO: bind each client to the server ip!!
			request = new Socket(server, port);
		} catch(UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Connected...");
		try {
			out = new ObjectOutputStream(request.getOutputStream());
			out.flush();
			in = new ObjectInputStream(request.getInputStream());
			SystemMessage s = new SystemMessage(request.getLocalAddress().getHostAddress(), server ,clientName, "logged in successfully", SystemMessage.systemMsgType.SYSTEM_LOGIN_MESSAGE);
			out.writeObject(s);
		} catch(IOException e) {
			System.out.println("Exception creating new Input/output Streams: " + e.getMessage());
			return false;
		}

		new ListenFromServer().start();

		return true;
	}

	public void disconnect()
	{
		try {
			this.request.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				this.in.close();
				this.out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getClientName()
	{
		return this.clientName;
	}

	public void sendMessage(Message msg)
	{
		try
		{
			out.writeObject(msg);
			out.flush();
			//TextMessage textMsg = (TextMessage) msg;
			//System.out.println(msg.getReceiver() + " message:" + textMsg.getContent());
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		// TODO: maybe add finally block to close the streams
	}

	public Message receiveMessage()
	{
		try {
			message = (Message)in.readObject();
			if (message.getType() == Message.msgType.TEXT_MESSAGE) {
				TextMessage tm = (TextMessage) message;
				System.out.println("Received from server: " + tm.getContent());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	class ListenFromServer extends Thread {

		public void run() {
			System.out.println("Waiting for messages from server...");
			while(true) {
				try {
					Message msg = (Message) in.readObject();
					if (msg.getType() == Message.msgType.TEXT_MESSAGE) {
						System.out.println("Received from server: " + ((TextMessage)msg).getContent());
						messageHandler.handleMessage(msg);
					}
					if (msg.getType() == Message.msgType.SYSTEM_MESSAGE) {
						messageHandler.handleMessage(msg);
					}
				} catch(IOException e) {
					System.out.println("Server has closed the connection...: " + e.getMessage());
					break;
				} catch(ClassNotFoundException cnfe) {
					System.out.println(cnfe.getMessage());
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ClientSide client = new ClientSide("localhost", 2151, "testUser", null);
			if (!client.start())
				return;
			// TODO: fill sender and receiver IP's of message, empty works only for localhost
			TextMessage tm = new TextMessage("", "","This is a test message that should return to client");
			client.sendMessage(tm);
			client.setMessage(client.receiveMessage());
		}
		catch (NullPointerException e) {
			System.err.println("No connection to server!");
		}
	}
}
