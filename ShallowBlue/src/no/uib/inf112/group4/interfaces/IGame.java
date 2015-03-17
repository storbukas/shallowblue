package no.uib.inf112.group4.interfaces;

import no.uib.inf112.group4.framework.Clock;
import no.uib.inf112.group4.framework.PlayerColor;

/**
 * Controls the game flow.
 */
public interface IGame {
	/**
	 * Returns an array with two elements. Index 0 = White player Index 1 =
	 * Black player
	 */
	IPlayer[] getPlayers();

	/**
	 * Set player with specified color. Should be able to set player even after
	 * game has started.
	 */
	void setPlayer(PlayerColor color, IPlayer player);

	/**
	 * Starts the game loop.
	 */
	void start();

	/**
	 * @return True if any of the players are checkmate.
	 */
	boolean isFinished();

	/**
	 * True if the player's king is threatened.
	 */
	boolean isChecked(IPlayer player);

	/**
	 * True if the player's king is threatened and cannot escape and no moves
	 * exists to save him.
	 */
	boolean isCheckmate(IPlayer player);

	/**
	 * Get the player whose turn it is.
	 */
	IPlayer getActivePlayer();

	/**
	 * Get the game board.
	 */
	IBoard getBoard();

	/**
	 * Get the player with the specified color.
	 */
	IPlayer getPlayer(PlayerColor color);

	/**
	 * Get the color of the specified player.
	 */
	PlayerColor getPlayerColor(IPlayer player);

	/**
	 * True if a player has no valid moves, and the king is not checked.
	 */
	boolean isStalemate();
	
	/**
	 * True if the game ended in a draw.
	 */
	boolean isDraw();

	/**
	 * Undo a move.
	 */
	void undo();

	/**
	 * Redo a move.
	 */
	void redo();

	/**
	 * 
	 * @param board
	 */
	void setBoard(IBoard board);

	/**
	 * 
	 */
	void startAbortMove();

	/**
	 * 
	 */
	void completeAbortMove();

	/**
	 * 
	 * @return
	 */
	IPlayer getInactivePlayer();

	/**
	 * 
	 * @param b
	 */
	void startClock(boolean b);

	/**
	 * 
	 * @return
	 */
	PlayerColor getActivePlayerColor();

	/**
	 * 
	 * @return
	 */
	Clock getClock();
	
	/**
	 * Attempts to claim a draw. If a draw
	 * is possible, the game will end and
	 * true will be returned.
	 */
	boolean claimDraw();
	
	/**
	 * Attempts to end the game, deeming
	 * the given player the loser. If game
	 * fails to end (e.g. given player isn't player)
	 * false is returned.
	 * @return
	 */
	boolean resign(IPlayer player);
}
