package no.uib.inf112.group4.interfaces;

import no.uib.inf112.group4.exceptions.AbortedMoveException;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.PlayerMoveResponse;
import no.uib.inf112.group4.framework.PlayerType;

/**
 * Interface for any player, including computer players.
 */
public interface IPlayer {

	/**
	 * This is where the player does his actions.
	 * 
	 * @param board The game board.
	 * 
	 * @return A move.
	 * @throws AbortedMoveException
	 */
	public PlayerMoveResponse getMove(IBoard board) throws AbortedMoveException;

	/**
	 * @return Player name.
	 */
	public String getName();

	/**
	 * 
	 * @return
	 */
	public PlayerType getPlayerType();

	/**
	 * 
	 * @return
	 */
	public PlayerColor getColor();
}
