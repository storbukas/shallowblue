package no.uib.inf112.group4.interfaces;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;

/**
 * Describes a chess move. The most basic move is to just transport a piece from
 * one location to another.
 * 
 * More advanced moves include pawn double move, King/Rook castling, piece
 * upgrades, attack moves.
 */
public interface IMove {
	/**
	 * Coordinates to move from.
	 * 
	 * @return Coordinates moved from.
	 */
	Coordinate getLocation();

	/**
	 * Coordinates to move to.
	 * 
	 * @return Coordinates moved to.
	 */
	Coordinate getDestination();

	/**
	 * Executes a desired move.
	 * 
	 * @param board
	 * @param color
	 * @return new Board with updated piece locations.
	 */
	IBoard execute(IBoard board, PlayerColor color);
	
	/**
	 * Executes a desired move.
	 */
	IBoard execute(IBoard board);

	/**
	 * Checks if a desired move is valid.
	 * 
	 * @param board
	 * @param color
	 * @return true if valid.
	 */
	boolean isValid(IBoard board, PlayerColor color);

	/**
	 * Sets a message appropriate for the validation check in isValid.
	 * 
	 * @return A message informing why a move is not valid.
	 */
	String getValidationMessage();
}
