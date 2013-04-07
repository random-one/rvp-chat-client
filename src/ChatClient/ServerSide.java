package ChatClient;
import java.io.BufferedReader;
import java.io.Console;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerSide {

	ServerSocket reply;
	Socket request;
	ObjectInputStream in;
	ObjectOutputStream out;
	Message message;
	boolean keepGoing;
	int port;

	static HashMap<String, ClientConnection> clients;

	ServerSide(int port)
	{
		clients = new HashMap<String, ClientConnection>();
	}

	public void start()
	{
		try {
			reply = new ServerSocket(2151, 5);

			System.out.println("Server is waiting to make a connection...!");

			keepGoing = true;
			while (keepGoing) {
				request = reply.accept();

				if(!keepGoing)
					break;

				System.out.println("Server accepted a connection! " + request.getInetAddress().getHostAddress());

				ClientConnection con = new ClientConnection(request);
				clients.put(request.getInetAddress().getHostAddress(), con);
				con.start();
			}

			try {
				reply.close();
				for (int x = 0; x < clients.size(); x++) {
					ClientConnection s = clients.get(x);
					s.close();
				}
			} catch(Exception e) {
				System.out.println("Exception closing the server and clients: " + e.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(Message msg)
	{
		try
		{
			if (msg.getType() == Message.msgType.TEXT_MESSAGE) {
				TextMessage tm = (TextMessage) msg;
				System.out.println("server sent: '" + tm.getContent() + "' to " + tm.getReceiver());
				if (clients.containsKey(tm.getReceiver())) {
					System.out.println("printing to client");
					out.flush();
					ClientConnection connection = clients.get(tm.getReceiver());
					connection.writeMessage(msg);
				}
			}
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}


	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ServerSide server;
		if (args.length == 2)
			server = new ServerSide(Integer.parseInt(args[0]));
		else {
			System.out.println("No port provided: server started on port 2151");
			server = new ServerSide(2151);
		}
		server.start();
	}
}


