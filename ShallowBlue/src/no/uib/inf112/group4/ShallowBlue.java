package no.uib.inf112.group4;

import java.sql.SQLException;

import no.uib.inf112.group4.framework.Game;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.players.HumanPlayer;
import no.uib.inf112.group4.framework.ui.GUI;
import no.uib.inf112.group4.interfaces.IPlayer;
import no.uib.inf112.group4.interfaces.IUserInterface;

public class ShallowBlue {

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		System.out.println("Starting Shallow Blue ...");

		// Create UI
		IUserInterface ui = null;
		// ui = new ConsoleUI(); // Uncomment for console
		ui = new GUI(960, 720); // Uncomment this line to see a magical GUI!

		// Activate UI
		IPlayer player1 = new HumanPlayer(ui, "", PlayerColor.WHITE);
		IPlayer player2 = new HumanPlayer(ui, "", PlayerColor.BLACK);
		//IPlayer player1 = new NetworkPlayer(ui, "",true);
		//IPlayer player2 = new NetworkPlayer(ui, "", false);
		
		ui.start(new Game(player1, player2, ui));

		// set the name of the application menu item
		System.setProperty("no-uib-inf112-group4-ShallowBlue", "Shallow Blue");
	}

}
