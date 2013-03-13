package ChatClient;

import java.io.IOException;


public class ChatClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		if (args.length == 0) {
	    	ServerSide server = new ServerSide();
	    	while(true){
	    		server.serverRun();
	    	}
		} else {
			ClientSide client = new ClientSide();
			client.runClient();
		}
	}
}
