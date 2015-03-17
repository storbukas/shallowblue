package no.uib.inf112.group4.interfaces;

import no.uib.inf112.group4.exceptions.AbortedMoveException;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.PlayerMoveResponse;

/**
 * Interface for user interfaces. These are responsible for showing the game and
 * interacting with the player.
 * 
 */
public interface IUserInterface {

	/**
	 * Set up the UI.
	 */
	public void start(IGame game);

	/**
	 * Draw the game.
	 */
	public void update();

	/**
	 * The player tried to perform an invalid move. Display a message to the
	 * user asking him for a new move.
	 * 
	 * @param message
	 */
	public void showInvalidMoveMessage(IMove move, String message);

	/**
	 * Asks the given player (through UI) to perform a move on the board and
	 * returns the move the player provided.
	 * 
	 * @throws AbortedMoveException
	 */
	public PlayerMoveResponse promptMove(IPlayer player) throws AbortedMoveException;

	/**
	 * 
	 * @param player
	 */
	public void showGameOver(IPlayer player);

	/**
	 * <pre>
	 * The UI asks the user what piece type he wants and returns it.
	 * @param color The players color
	 */
	public IPiece promptPromotionPiece(PlayerColor color);

	/**
	 * Called when a move is completed.
	 */
	public void moveCompleted();

	void statusCheck();
}
