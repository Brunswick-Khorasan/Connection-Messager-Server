package server;

import java.io.IOException;
import java.net.*;
/**
 * The Server used by the messaging application
 * @author Morgan</br>
 * http://docs.oracle.com/javase/tutorial/networking/sockets/index.html - The Oracle Java(tm) Tutorials
 */
public class ConnectionServer {
	public static final int PORTNUM = 3576;
	public static void main(String[] args) {
		try (ServerSocket connection = new ServerSocket(PORTNUM)) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
