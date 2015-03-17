package no.uib.inf112.group4.framework;

import no.uib.inf112.group4.exceptions.GameKilledException;
import no.uib.inf112.group4.interfaces.IGame;

/**
 * A thread in which the game logic is executed.
 * 
 * This is required in order for the GUI to be responsive to user input not
 * handled by game logic
 */
public class GameThread extends Thread {
	IGame game = null;

	public GameThread(IGame game) {
		this.game = game;
		this.setName("Game thread");
	}

	@Override
	public void run() {
		try {
			game.start();
		} catch (GameKilledException e) {
			System.out.println("GameThread: Game killed.");
		}
		System.out.println("GameThread: Game thread terminated.");
	}
}
