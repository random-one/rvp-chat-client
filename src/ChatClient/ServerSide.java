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
	String message;

	ServerSide()
	{

	}

	public void bindServer()
	{
		try {
			reply = new ServerSocket(2151, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void serverRun()
	{
		try
		{
			System.out.println("Server is waiting to make a connection...!");
			request = reply.accept();
			System.out.println("Server accepted a connection! " + request.getInetAddress().getHostAddress());
			out = new ObjectOutputStream(request.getOutputStream());
			out.flush();
			in = new ObjectInputStream(request.getInputStream());
			sendMessage("Connection accepted!");
			do
			{
				try
				{
					message = (String)in.readObject();
					System.out.println("received from client >" + message);
					if(message.equals("Bye!"))
						sendMessage("Bye!");
				}
				catch (ClassNotFoundException e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}
			}while(!message.equals("Bye!"));

		}
		catch (IOException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
//		finally{
//			try{
//				in.close();
//				out.close();
//				reply.close();
//			}
//			catch(IOException ioException){
//				ioException.printStackTrace();
//			}
//		}
	}

	public void sendMessage(String msg)
	{
		try
		{
			out.writeObject(msg);
			out.flush();
			System.out.println("server sent >" + msg);
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}


	public static void main(String[] args) throws IOException, ClassNotFoundException{
		ServerSide server = new ServerSide();
		server.bindServer();
		while(true){
			server.serverRun();
		}
	}
}


