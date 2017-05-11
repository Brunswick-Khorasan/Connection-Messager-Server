package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;
/**
 * The Server used by the messaging application
 * @author Morgan</br>
 * http://docs.oracle.com/javase/tutorial/networking/sockets/index.html - The Oracle Java(tm) Tutorials
 */
public class ConnectionServer {
	private ArrayList<Client> clients;
	private String serverName;
	private ServerGUI inter;
	private Queue<String> logText;
	private HashMap<String, String> emoticons;
	public ConnectionServer(ServerInterface inter) {
		this.inter = inter;
		logText = new LinkedList<String>();
		emoticons = new HashMap<>(); //Emoticons are specified by ":key:", which is replaced with the key's value
		emoticons.put("smile", "☺");
		emoticons.put("smile_black", "☻");
		emoticons.put("heart", "♥");
		emoticons.put("thorn", "þ");
		emoticons.put("thorn_capital", "Þ");
		emoticons.put("ae", "æ");
		emoticons.put("ae_capital", "Æ");
		emoticons.put("spade", "♠");
		emoticons.put("club", "♣");
		emoticons.put("diamond", "♦");
		emoticons.put("paragraph", "¶");
		emoticons.put("section", "§");
		emoticons.put("mu", "µ");
		emoticons.put("table_flip", "(ノ°Д°)ノ︵ ┻━┻");
		emoticons.put(":pi:", "π");
		emoticons.put(":alpha:", "α");
		emoticons.put(":beta:", "β");
	}
	/**
	 * Starts the server, making it ready to receive connections
	 */
	public void start() {
		Thread server = new Thread() {
			public void run() {
				clients = new ArrayList<>();
				serverName = JOptionPane.showInputDialog("Enter a name for the server");
				inter.setServerName(serverName);
				try (ServerSocket connection = new ServerSocket(Constants.PORTNUM)) {
					sayToAll("Starting up on port " + Constants.PORTNUM + " ...",null);
					Thread connectUsers = new Thread() {
						public void run() {
							sayToAll("Waiting for connections...",null);
							while (true) {
								try {
									clients.add(new Client(connection.accept()));
									inter.addToLog("[SERVER<internal>] A Client has connected.");
									clients.get(clients.size()-1).getWriter().println("[SERVER] You have connected to "+serverName+"!");
									clients.get(clients.size()-1).getWriter().println("[SERVER] Enter a name to be called by: ");
									clients.get(clients.size()-1).setName(clients.get(clients.size()-1).getReader().readLine());
									sayToAll("User " + clients.get(clients.size()-1).getName() + " has connected!",null);
									inter.addUser(clients.get(clients.size()-1).getName());
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
											if (cMessage.startsWith(Constants.COMMANDCHAR + "")) { //Interpret as command
												switch (cMessage.charAt(1)) {
												case Constants.CommandCodes.DISCONNECT:
													sayToAll(c.getName() + " has disconnected", null);
													clients.remove(c);
													inter.removeUser(c.getName());
													break;
												case Constants.CommandCodes.DOWNLOADLOG:
													String text = "";
													Iterator<String> it = logText.iterator();
													while (it.hasNext()) {
														text = text + "¬" + it.next(); //Packaged with ¬ to get multiline in one
													}
													c.getWriter().println(text);
													break;
												}
											} else { //Display the message
												if (cMessage.contains(":")) { // Emoticon changing
													for (int i=0;i<cMessage.length()-1;i++) {
														if (cMessage.charAt(i) == ':') {
															//find the next one
															int next = cMessage.indexOf(":", i+1);
															if (next == -1) {
																break;
															}
															//check if it is in emoticons
															if (emoticons.containsKey(cMessage.substring(i+1, next))) {
															 //replace
																cMessage = cMessage.substring(0, i) +
																		emoticons.get(cMessage.substring(i+1, next)) +
																		cMessage.substring(next+1);
															}
														}
													}
												}
												sayToAll(cMessage, c);
											}
										}
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
					};
					connectUsers.start();
					sayToAll("User-connection thread started",null);
					sayToAll("User-listening thread started",null);
					listenToUsers.run();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		server.start();
	}
	/**
	 * Tells all users connected to the server, and displays to console
	 * @param message the message to send
	 * @param source the client who sent the message (or null for SERVER)
	 */
	public void sayToAll(String message,Client source) {
		String sMessage = source==null?"[SERVER] " + message:"["+source.getName()+"] " + message;
		inter.addToLog(sMessage);
		logText.add(sMessage);
		for (Client c : clients.toArray(new Client[0])) {
			c.getWriter().println(sMessage);
		}
	}
}
