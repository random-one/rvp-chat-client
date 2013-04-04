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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.omg.CORBA.Any;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;


public class ServerSide {

	ServerSocket reply;
	Socket request;
	ObjectInputStream in;
	ObjectOutputStream out;
	Message message;

	static HashMap<String, Socket> clients;

	ServerSide()
	{
		try {
			reply = new ServerSocket(2151, 5);

			clients = new HashMap<String, Socket>();
			System.out.println("Server is waiting to make a connection...!");

			while (true) {
				request = reply.accept();
				clients.put(request.getInetAddress().getHostAddress(), request);
				System.out.println("Server accepted a connection! " + request.getInetAddress().getHostAddress());
				try {
					ClientConnection con = new ClientConnection(request);
				} catch(EOFException eofe)
				{
					request.close();
					break;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(Message msg)
	{
		try
		{
			out.writeObject(msg);
			out.flush();
			if (msg.getType() == Message.msgType.TEXT_MESSAGE) {
				TextMessage tm = (TextMessage) msg;
				System.out.println("server sent: '" + tm.getContent() + "' to " + tm.getReceiver());
			}
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}


	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ServerSide server = new ServerSide();
	}
}


