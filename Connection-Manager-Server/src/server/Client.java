package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client in ConnectionServer. Can be written to and read from.
 * @author Morgan
 *
 */
public class Client {
	private Socket client;
	private PrintWriter out;
	private BufferedReader in;
	private String name;
	public Client(Socket clientSocket) {
		client = clientSocket;
		try {
			out = new PrintWriter(client.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Get the client socket
	 * @return the socket of this client
	 */
	public Socket getClient() {return client;}
	/**
	 * Get the writer associated with this client
	 * @return a PrintWriter object that connects to the client
	 */
	public PrintWriter getWriter() {return out;}
	/**
	 * Get the reader associated with this client
	 * @return a BufferedReader of the client's inputs
	 */
	public BufferedReader getReader() {return in;}
	/**
	 * Set the username of the client
	 * @param name the new name
	 */
	public void setName(String name) {this.name=name;}
	/**
	 * Get the username of the client
	 * @return
	 */
	public String getName() {return name;}
	
}
