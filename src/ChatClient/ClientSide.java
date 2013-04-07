package ChatClient;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientSide {

	private String clientName;
	private Socket request;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Message message;
	//TODO: add Set<FileMessage> receivedFiles;

	ClientSide()
	{
	}

	public boolean start()
	{
		try {
			// TODO: bind each client to the server ip!!
			request = new Socket("192.168.0.105",2151);
		} catch(UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Connected?!");
		try {
			out = new ObjectOutputStream(request.getOutputStream());
			out.flush();
			in = new ObjectInputStream(request.getInputStream());
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
				System.out.println("received from server: " + tm.getContent());
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
			System.out.println("waiting for messages from server");
			while(true) {
				try {
					Message msg = (Message) in.readObject();
					if (msg.getType() == Message.msgType.TEXT_MESSAGE) {
						System.out.println("received from server: " + ((TextMessage)msg).getContent());
					}
				} catch(IOException e) {
					System.out.println("Server has closed the connection: " + e.getMessage());
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
			ClientSide client = new ClientSide();
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
