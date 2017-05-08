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
			out = new PrintWriter(client.getOutputStream());
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Socket getClient() {return client;}
	public PrintWriter getWriter() {return out;}
	public BufferedReader getReader() {return in;}
	public void setName(String name) {this.name=name;}
	public String getName() {return name;}
	
}
