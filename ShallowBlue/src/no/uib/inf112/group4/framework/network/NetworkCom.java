package no.uib.inf112.group4.framework.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public class NetworkCom {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private boolean connected = false;

	public NetworkCom(ObjectInputStream in, ObjectOutputStream out) {
		this.in = in;
		this.out = out;
		this.connected = true;
	}
	
	public void setConnected(boolean value) {
		this.connected = value;
	}
	
	public boolean getConnected() {
		return this.connected;
	}
	
	public void send(Object obj) {
		System.out.println("Sending object "
                + obj.getClass());
		
		try {
			out.writeObject(obj);
		}
		
		catch (SocketException e) {
			System.out.println("The communication link has failed suddenly");
			connected = false;
			System.exit(0);
			return;
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Finished");
	}
	
	public Object receive() {
		Object object = null;
		
		while(!connected);
		
		try {
			System.out.println("Waiting for new object");
			object = in.readObject();
		} 
		
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	
		catch(EOFException e) {
			System.out.println("The other host disconnected.");
			connected = false;
			System.exit(0);
			return null;
		}
		catch (SocketException e) {
			System.out.println("The communication link has failed suddenly");
			connected = false;
			System.exit(0);
			return null;
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Object received");
		
		return object;
	}
}
