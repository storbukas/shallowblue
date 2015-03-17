package no.uib.inf112.group4.framework.network;

import java.io.Serializable;
import java.util.Scanner;

/**
 * Class to make simple object to send over network.
 * 
 */
public class SimpleObject implements Serializable {
	private static final long serialVersionUID = 1060215269859259233L;

	public static SimpleObject newInstance() {
		Scanner in = new Scanner(System.in);
		System.out.print("Skriv inn navnet til en person: ");
		String name = in.nextLine();
		in.close();
		return new SimpleObject(name);
	}

	private String name;

	public SimpleObject() {
		// no-arg
	}

	public SimpleObject(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
