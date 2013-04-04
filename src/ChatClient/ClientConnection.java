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

	ClientConnection(Socket client) throws Exception 
	{
		request = client;
		in=new ObjectInputStream(request.getInputStream());
		out=new ObjectOutputStream(request.getOutputStream());

		//ServerSide.clients.put(request.getInetAddress().getHostAddress(), request);
		start();
	}

	public void run()
	{
		while (true)
		{
			try {
				Message message = (Message)in.readObject();
				System.out.println("received from client");
				if (message.getType() == Message.msgType.TEXT_MESSAGE) {
					TextMessage tm = (TextMessage)message;
					//out.writeObject(message);
					System.out.println("server sent: '" + tm.getContent() + "' to " + tm.getReceiver());
					if (ServerSide.clients.containsKey(tm.getReceiver())) {
						System.out.println("printing to client");
						request = ServerSide.clients.get(tm.getReceiver());
						((ObjectOutputStream)(request.getOutputStream())).writeObject(message);
					}
				}
			} catch (EOFException e) {
				try {
					request.close();
					ServerSide.clients.remove(request.getInetAddress().getHostAddress());
					break;
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}
}
