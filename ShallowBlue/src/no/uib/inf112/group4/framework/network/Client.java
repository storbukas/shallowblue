package no.uib.inf112.group4.framework.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Client class for connecting to the sever
 * 
 */
public class Client {
	// private ServerSocket serverSocket;
	private Socket socket;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private NetworkCom net;

	// Handshake
	private NetworkHandshake handshake;
	private NetworkHandshake opponent;

	public Client() {
		// no-arg
	}

	public Client(NetworkHandshake handshake, String host, int port) {
		this.handshake = handshake;

		try {
			// Connect
			System.out.println("Connecting to " + host + " on port " + port);
			socket = new Socket(host, port);
			System.out.println("Just connected to "
					+ socket.getRemoteSocketAddress());

			// Object streams
			toServer = new ObjectOutputStream(socket.getOutputStream());
			fromServer = new ObjectInputStream(socket.getInputStream());

			// Network communication
			this.net = new NetworkCom(fromServer, toServer);

			// Handshake procedure
			this.opponent = (NetworkHandshake) receive();
			send(this.handshake);
		}

		catch (Exception e) {
			System.err.println(e);
		}
	}

	public ObjectOutputStream getToServer() {
		return this.toServer;
	}

	public ObjectInputStream getFromServer() {
		return this.fromServer;
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