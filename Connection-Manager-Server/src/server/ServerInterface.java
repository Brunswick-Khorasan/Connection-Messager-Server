package server;

import java.awt.FlowLayout;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;

/**
 * The GUI of the server
 * @author Morgan
 *
 */
public class ServerInterface extends JFrame {
	private static final long serialVersionUID = -5657260030042209306L;
	private JTextArea log;
	private JTextArea users;
	private ArrayList<String> userList;
	private ConnectionServer server;
	public static void main(String[] args) {
		new ServerInterface();
	}
	public ServerInterface() {
		final int HEIGHT = 40;
		server = new ConnectionServer(this);
		log = new JTextArea(HEIGHT,50);
		userList = new ArrayList<>();
		users = new JTextArea(HEIGHT,10);
		JScrollPane scrollbar = new JScrollPane(log);
		scrollbar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		log.setEditable(false);
		users.setEditable(false);
		setTitle("Connection Server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel userPanel = new JPanel();
		userPanel.add(new JLabel("Active Users:"));
		userPanel.add(users);
		userPanel.setLayout(new BoxLayout(userPanel,BoxLayout.PAGE_AXIS));
		JPanel talkPanel = new JPanel();
		talkPanel.setLayout(new BoxLayout(talkPanel,BoxLayout.PAGE_AXIS));
		try {
			talkPanel.add(new JLabel("Your IP Address is: "+InetAddress.getLocalHost().getHostAddress()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		talkPanel.add(scrollbar);
		add(userPanel);
		add(new JSeparator(SwingConstants.VERTICAL));
		add(talkPanel);
		setLayout(new BoxLayout(this.getContentPane(),BoxLayout.LINE_AXIS));
		setResizable(false);
		server.start();
		pack();
		setVisible(true);
	}
	public void addToLog(String message) {
		log.append("\n"+message);
	}
	public void setServerName(String name) {
		setTitle("Connection Server - " + name);
	}
	public void addUser(String username) {
		userList.add(username);
		users.setText(userList.toString().replace('[', ' ').replace(']', ' ').replaceAll(", ", "\n").trim());
	}
	public void removeUser(String username) {
		userList.remove(userList.indexOf(username));
		users.setText(userList.toString().replace('[', ' ').replace(']', ' ').replaceAll(", ", "\n").trim());
	}
}
