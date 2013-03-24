package ChatClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientSide {

	private Socket request;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Message message;
	//TODO: add Set<FileMessage> receivedFiles;

	ClientSide()
	{

	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
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
	}

	public void runClient()
	{
		try
		{
			request = new Socket("",2151);
			System.out.println("Connected?!");
			out = new ObjectOutputStream(request.getOutputStream());
			out.flush();
			in = new ObjectInputStream(request.getInputStream());
			do
			{
				try
				{
					message = (String)in.readObject();
					System.out.println("server sent >" + message);
					message = "Bye!";
					sendMessage(message);
				}
				catch(ClassNotFoundException cnfe)
				{
					System.out.println(cnfe.getMessage());
				}
			}while(!message.equals("Bye!"));
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		finally{

			try{
				in.close();
				out.close();
				request.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientSide client = new ClientSide();
		client.runClient();
	}
}
