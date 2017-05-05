package server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
/**
 * The Server used by the messaging application
 * @author Morgan</br>
 * http://docs.oracle.com/javase/tutorial/networking/sockets/index.html - The Oracle Java(tm) Tutorials
 */
public class ConnectionServer {
	private ArrayList<Client> clients;
	private String serverName;
	private ServerInterface inter;
	public ConnectionServer(ServerInterface inter) {
		this.inter = inter;
	}
	public void start() {
		Thread server = new Thread() {
			public void run() {
				clients = new ArrayList<>();
				serverName = JOptionPane.showInputDialog("Enter a name for the server");
				try (ServerSocket connection = new ServerSocket(Constants.PORTNUM)) {
					sayToAll("Starting up on port " + Constants.PORTNUM + " ...",null);
					Thread connectUsers = new Thread() {
						public void run() {
							sayToAll("Waiting for connections...",null);
							while (true) {
								try {
									clients.add(new Client(connection.accept()));
									inter.addToLog("[SERVER] Client Accepted");
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
											String cMessage = c.getReader().readLine();
											if (cMessage.startsWith(Constants.COMMANDCHAR+"")) {
												//TODO Interpret as command
												switch (cMessage.charAt(1)) {
												case Constants.CommandCodes.DISCONNECT: 
													sayToAll(c.getName() + " has disconnected",null);
													clients.remove(c);
													break;
												}
											} else {
												sayToAll(cMessage,c);
											}
										}
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
					};
					sayToAll("User-connection thread created",null);
					connectUsers.start();
					sayToAll("User-connection thread started",null);
					listenToUsers.run();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		server.start();
	}
	public void sayToAll(String message,Client source) {
		String sMessage = source==null?"[SERVER] " + message:"["+source.getName()+"] " + message;
		inter.addToLog(sMessage);
		for (Client c : clients.toArray(new Client[0])) {
			c.getWriter().println(sMessage);
		}
	}
}
