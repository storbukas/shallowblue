package no.uib.inf112.group4.interfaces;

import java.util.List;

import no.uib.inf112.group4.framework.BoardAnalyzer;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PerformedMove;
import no.uib.inf112.group4.framework.PlayerColor;

/**
 * A board keeps track of the chess pieces and operations on them.
 */
public interface IBoard {
	/**
	 * Add a piece to the board.
	 */
	IBoard addPiece(IPiece piece, Coordinate coordinate);

	/**
	 * Remove a piece from the board and note it
	 * as captured.
	 */
	IBoard capturePiece(IPiece piece);

	/**
	 * Remove all pieces from the board.
	 */
	IBoard clear();

	/**
	 * Get all pieces still on the board.
	 */
	List<IPiece> getAllPieces();

	/**
	 * Get all valid moves that the current player can do.
	 */
	List<IMove> getAllValidMoves();

	/**
	 * Get all black pieces still on the board.
	 */
	List<IPiece> getBlackPieces();

	/**
	 * Returns a 2D array containing the number of 
	 * captured pieces for each color.
	 * 
	 * Format: array[color][type]
	 */
	int[][] getCaptureCount();
	
	/**
	 * Get the coordinate a piece is on, or null if piece could not be found.
	 */
	Coordinate getCoordinate(IPiece piece);

	/**
	 * Returns a 2D array representing the board.
	 */
	IPiece[][] getData();

	/**
	 * Get the size of the board.
	 * The board is assumed to be square, so only one value is needed.
	 */
	int getDimension();

	/**
	 * Return all the moves that has been performed in this boards history.
	 */
	PerformedMove[] getPerformedMoves();

	/**
	 * Return piece at coordinate or null if unoccupied.
	 */
	IPiece getPieceAt(Coordinate coord);

	/**
	 * Get all pieces with the specified color, that is still on the board.
	 */
	List<IPiece> getPiecesWithColor(PlayerColor color);
	
	/**
	 * 
	 * Get the previous move made by a player or null if no move has been made yet.
	 */
	IMove getPreviousMove();

	/**
	 * Get the color of the player whose turn it is.
	 */
	PlayerColor getTurn();

	/**
	 * Get all white pieces still on the board.
	 */
	List<IPiece> getWhitePieces();

	/**
	 * True if the piece is on the board.
	 */
	boolean hasPiece(IPiece piece);

	/**
	 * True if a piece exists at the specified coordinates.
	 */
	boolean hasPieceAt(Coordinate coord);

	/**
	 * Returns true if the piece has been moved throughout the game.
	 */
	boolean hasPieceMoved(IPiece piece);

	/**
	 * Move a piece.
	 */
	IBoard movePiece(IPiece piece, Coordinate destination);

	/**
	 * Remove a piece from the board.
	 */
	IBoard removePiece(IPiece piece);

	/**
	 * Get all opponents moves without validation.
	 * @param color TODO
	 */
	List<IMove> getAllMovesForColor(PlayerColor color);

	BoardAnalyzer getAnalyzer();
}
