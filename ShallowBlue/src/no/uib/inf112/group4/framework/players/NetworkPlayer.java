package no.uib.inf112.group4.framework.players;

import java.sql.SQLException;

import no.uib.inf112.group4.exceptions.AbortedMoveException;
import no.uib.inf112.group4.framework.EloDatabase;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.PlayerMoveResponse;
import no.uib.inf112.group4.framework.PlayerType;
import no.uib.inf112.group4.framework.network.Client;
import no.uib.inf112.group4.framework.network.NetworkCom;
import no.uib.inf112.group4.framework.network.NetworkHandshake;
import no.uib.inf112.group4.framework.network.Server;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IPlayer;
import no.uib.inf112.group4.interfaces.IUserInterface;

public class NetworkPlayer implements IPlayer {
	IUserInterface ui;
	private PlayerColor color;
	private String name; 
	private boolean isServer;
	public NetworkCom net;
	private String host = "localhost";
	public NetworkHandshake netHandshake;
	private Server server;
	private Client client;
	
	public NetworkPlayer(IUserInterface ui, String name, boolean isServer) {
		this.ui = ui;
		this.name = name;
		this.isServer = isServer;
		EloDatabase database = EloDatabase.getInstance();
		
		if (isServer) {
			this.color = PlayerColor.WHITE;
			startServer();
			try {
				netHandshake = new NetworkHandshake(database.getEloRating(name), name);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			this.color = PlayerColor.BLACK;
			connectHost();
			try {
				netHandshake = new NetworkHandshake(database.getEloRating(name), name);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setHost(String ip){
		host = ip;
	}
	@Override
	public PlayerMoveResponse getMove(IBoard board) throws AbortedMoveException {	
		return ui.promptMove(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public PlayerType getPlayerType() {
		return PlayerType.NETWORK;
	}

	@Override
	public PlayerColor getColor() {
		return color;
	}
	
	public boolean isServer() {
		return this.isServer;
	}
	
	public void startServer() {
		try {
			EloDatabase database = EloDatabase.getInstance();
			server = new Server(new NetworkHandshake(database.getEloRating(getName()), getName()), 1337);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		net = new NetworkCom(server.getFromClient(), server.getToClient());
	}
	
	public void connectHost() {
		try {
			EloDatabase database = EloDatabase.getInstance();
			client = new Client(new NetworkHandshake(database.getEloRating(getName()), getName()), host, 1337);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		net = new NetworkCom(client.getFromServer(), client.getToServer());
	}
	
	public Server getServer() {
		return this.server;
	}
	
	public Client getClient() {
		return this.client;
	}


}
