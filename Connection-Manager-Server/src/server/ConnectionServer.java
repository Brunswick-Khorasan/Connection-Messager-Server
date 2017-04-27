package server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * The Server used by the messaging application
 * @author Morgan</br>
 * http://docs.oracle.com/javase/tutorial/networking/sockets/index.html - The Oracle Java(tm) Tutorials
 */
public class ConnectionServer {
	public static final int PORTNUM = 3576;
	private static ArrayList<Client> clients;
	private static String serverName;
	public static void main(String[] args) {
		clients = new ArrayList<>();
		Scanner s = new Scanner(System.in);
		System.out.print("Enter a server name: ");
		serverName = s.nextLine();
		s.close();
		try (ServerSocket connection = new ServerSocket(PORTNUM)) {
			sayToAll("Starting up on port " + PORTNUM + " ...",null);
			Thread connectUsers = new Thread() {
				public void run() {
					sayToAll("Waiting for connections...",null);
					while (true) {
						try {
							clients.add(new Client(connection.accept()));
							System.out.println("Client Accepted");
							clients.get(clients.size()-1).getWriter().println("[SERVER] You have connected to "+serverName+"!");
							clients.get(clients.size()-1).getWriter().print("[SERVER] Enter a name to be called by: ");
							clients.get(clients.size()-1).setName(clients.get(clients.size()-1).getReader().readLine());
							sayToAll("User " + clients.get(clients.size()-1).getName() + " has connected!",null);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			Thread listenToUsers = new Thread() {
				public void run() {
					while (true) {
						for (Client c : clients.toArray(new Client[0])) {
							try {
								while (c.getReader().ready()) {
									sayToAll(c.getReader().readLine(),c);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			};
			Thread checkForDisconnect = new Thread() {
				public void run() {
					for (Client c : clients.toArray(new Client[0])) {
						if (c.getClient().isClosed()) {
							sayToAll(c.getName() + " had disconnected",null);
							clients.remove(c);
						}
					}
				}
			};
			sayToAll("User-connection thread created",null);
			connectUsers.start();
			sayToAll("User-connection thread started",null);
			checkForDisconnect.start();
			listenToUsers.run();
			//while (true); //Workaround so server socket isn't closed
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void sayToAll(String message,Client source) {
		String sMessage = source==null?"[SERVER] " + message:"["+source.getName()+"] " + message;
		System.out.println(sMessage);
		for (Client c : clients.toArray(new Client[0])) {
			c.getWriter().println(sMessage);
		}
	}
}
