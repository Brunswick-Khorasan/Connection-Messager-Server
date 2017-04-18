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
			sayToAll("Starting up on port " + PORTNUM + " ...");
			Thread connectUsers = new Thread() {
				public void run() {
					sayToAll("Waiting for connections...");
					while (true) {
						try {
							clients.add(new Client(connection.accept()));
							clients.get(clients.size()-1).getWriter().println("[SERVER] You have connected to "+serverName+"!");
							clients.get(clients.size()-1).getWriter().print("[SERVER] Enter a name to be called by: ");
							clients.get(clients.size()-1).setName(clients.get(clients.size()-1).getReader().readLine());
							sayToAll("User " + clients.get(clients.size()-1).getName() + " has connected!");
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
									sayToAll(c.getReader().readLine());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			};
			sayToAll("User-connection thread created");
			connectUsers.start();
			listenToUsers.start();
			sayToAll("User-connection thread started");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void sayToAll(String message) {
		System.out.println("[SERVER] " + message);
		for (Client c : clients.toArray(new Client[0])) {
			c.getWriter().println("[SERVER] " + message);
		}
	}
}
