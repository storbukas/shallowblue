package no.uib.inf112.group4.framework.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server that receives a SimpleObject and prints it to screen
 * 
 * @author lst111
 *
 */
public class Server {
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectOutputStream toClient;
	private ObjectInputStream fromClient;
	private NetworkCom net;

	// Handshake
	private NetworkHandshake handshake;
	private NetworkHandshake opponent;
	
	public Server() {
		// no-arg
	}
	
	public Server(NetworkHandshake handshake, int port) {
		this.handshake = handshake;
		
		try {
			System.out.println("Server started");
			
			// Create socket
			serverSocket = new ServerSocket(port);
			System.out.println("Waiting for client on port " +
		            serverSocket.getLocalPort() + "...");
			
			// Listen for new connection request
			socket = serverSocket.accept();
			
			System.out.println("Just connected to "
	                  + socket.getRemoteSocketAddress());
			
			// Object streams
			fromClient = new ObjectInputStream(socket.getInputStream());
			toClient = new ObjectOutputStream(socket.getOutputStream());
			
			// Network communication
			this.net = new NetworkCom(fromClient, toClient);
			
			// Handshake procedure
			send(this.handshake);
			this.opponent = (NetworkHandshake) receive();
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ObjectInputStream getFromClient() {
		return this.fromClient;
	}
	
	public ObjectOutputStream getToClient() {
		return this.toClient;
	}
	
	public int getStatus() {
		return net.getConnected() ? 1 : 0;
	}
	
	public Object receive() {
		return net.receive();
	}
	
	public void send(Object obj) {
		net.send(obj);
	}
	
	public NetworkHandshake getHandshake() {
		return this.opponent;
	}
}
