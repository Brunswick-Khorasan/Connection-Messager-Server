package server;

import java.awt.FlowLayout;

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
	private ConnectionServer server;
	public static void main(String[] args) {
		new ServerInterface();
	}
	public ServerInterface() {
		final int HEIGHT = 40;
		server = new ConnectionServer(this);
		log = new JTextArea(HEIGHT,50);
		users = new JTextArea(HEIGHT,10);
		log.setEditable(false);
		users.setEditable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel userPanel = new JPanel();
		userPanel.add(new JLabel("Active Users:"));
		userPanel.add(users);
		userPanel.setLayout(new BoxLayout(userPanel,BoxLayout.PAGE_AXIS));
		add(userPanel);
		add(log);
		setLayout(new FlowLayout());
		server.start();
		pack();
		setVisible(true);
	}
	public void addToLog(String message) {
		log.append("\n"+message);
	}
}
