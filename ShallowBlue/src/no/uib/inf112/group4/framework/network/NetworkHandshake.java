package no.uib.inf112.group4.framework.network;

import java.io.Serializable;

public class NetworkHandshake implements Serializable {
	private static final long serialVersionUID = 6374342647812838367L;
	
	int eloRating;
	String name;
	
	public NetworkHandshake(int eloRating, String name) {
		this.name = name;
		this.eloRating = eloRating;
	}
	
	public int getEloRating() {
		return this.eloRating;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEloRating(int eloRating) {
		this.eloRating = eloRating;
	}
	
}
