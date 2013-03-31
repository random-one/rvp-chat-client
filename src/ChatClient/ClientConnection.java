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

		ServerSide.clients.add(request);
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
					System.out.println("server sent: '" + tm.getContent() + "' to " + tm.getReceiver());
				}
			} catch (EOFException e) {
				try {
					request.close();
					ServerSide.clients.remove(request);
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
