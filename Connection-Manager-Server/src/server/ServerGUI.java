package server;
/**
 * Class for a Server GUI. Must use a ConnectionServer to connect a server
 * @author Morgan
 *
 */
public interface ServerGUI {
	public void addToLog(String s);
	public void setServerName(String name);
	public void addUser(String username);
	public void removeUser(String username);
}
