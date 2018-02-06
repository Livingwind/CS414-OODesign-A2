package a2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Allow the a2.Chatbot to be accessible over the network.  <br />
 * This class only handles one client at a time.  Multiple instances of a2.ChatbotServer
 * will be run on different ports with a port-based load balancer to handle multiple clients.
 * 
 * @author <Your Name Here>
 */
public class ChatbotServer {
	
	/**
	 * The instance of the {@link Chatbot}.
	 */
	private Chatbot chatbot;

	/**
	 * The instance of the {@link ServerSocket}.
	 */
	private ServerSocket serversocket;

	/**
	 * Constructor for a2.ChatbotServer.
	 * 
	 * @param chatbot The chatbot to use.
	 * @param serversocket The pre-configured ServerSocket to use.
	 */
	public ChatbotServer(Chatbot chatbot, ServerSocket serversocket) {
		this.chatbot = chatbot;
		this.serversocket = serversocket;
	}
	
	/**
	 * Start the a2.Chatbot server.  Does not return.
	 */
	public void startServer() {
		while(true) handleOneClient();
	}

	/**
	 * Handle interaction with a single client.  See assignment description.
	 */
	public void handleOneClient() {
		try {
			Socket sock = serversocket.accept();
			InputStream din = sock.getInputStream();
			OutputStream dout = sock.getOutputStream();

			BufferedReader buffed = new BufferedReader(new InputStreamReader(din));
			PrintWriter writer = new PrintWriter(dout);

			String line;
			do {
				line = buffed.readLine();
				try {
					writer.println(chatbot.getResponse(line));
				} catch (AIException e) {
					writer.println(String.format("Got AIException: <%s>", e.getMessage()));
				}
				writer.flush();
			} while (line != null);

		} catch (Exception e) {
		  e.printStackTrace(System.err);
		}
	}
}