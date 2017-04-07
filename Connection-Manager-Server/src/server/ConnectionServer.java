package server;

import java.io.IOException;
import java.net.*;
/**
 * The Server used by the messaging application
 * @author Morgan
 *
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
