package no.uib.inf112.group4.interfaces;

import java.util.List;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.pieces.PieceType;

/**
 * Interface for the chess pieces. The piece class is the superclass for all the
 * pieces.
 * 
 * There should be six different types of pieces: - King, Queen, Knight, Rook,
 * Bishop and Pawn. All this have their own class which extend the Piece class
 */
public interface IPiece {
	/**
	 * Return all moves that this piece could theoretically do. Validation of
	 * the moves is done by IGame.
	 * 
	 * @param location
	 *            Current location of the piece.
	 */
	public List<IMove> getMoves(IBoard board, Coordinate location);

	/**
	 * Returns a character to be used in the GUI to represent this piece.
	 */
	public String getRepresentation();

	/**
	 * Returns the pieces color. Black or White.
	 */
	public PlayerColor getColor();
	
	/**
	 * Returns the piece's type
	 */
	public PieceType getType();

	/**
	 * <pre>
	 * Returns the piece value. Used to evaluate current position.
	 * The king is not a part of the calculation, and return 0.
	 * 	Piece	| Value
	 * 	-----------------
	 * 	pawn	|  1
	 * 	knight	|  3
	 * 	bishop	|  3
	 * 	rook	|  5
	 * 	queen	|  9
	 * </pre>
	 */
	public int getPieceValue();

}
