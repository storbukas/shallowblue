package no.uib.inf112.group4.pieces;

import java.util.List;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;

/**
 * This is the superclass for all chess pieces.
 * 
 */
public abstract class Piece implements IPiece {
	// VARIABLES
	final String piece;
	final PlayerColor color;
	final int board_size = 8;

	// CONSTRUCTOR
	public Piece(String piece, PlayerColor color) {
		this.piece = piece;
		this.color = color;
	}
	
	public static Piece create(PieceType pt, PlayerColor color) {
		switch(pt) {
		case PAWN:
			return new Pawn(color);
		case BISHOP:
			return new Bishop(color);
		case KNIGHT:
			return new Knight(color);
		case KING:
			return new King(color);
		case QUEEN:
			return new Queen(color);
		case ROOK:
			return new Rook(color);
		default:
			return null;
		}
	}

	// METHODS
	@Override
	public String getRepresentation() {
		return piece.substring(0, 1);
	}

	@Override
	public PlayerColor getColor() {
		return this.color;
	}

	/**
	 * Add a move to the list only if there is no pieces at target location.
	 * 
	 * @param listOfMoves
	 *            List to add move to.
	 * @param x
	 *            Target X-pos.
	 * @param y
	 *            Target Y-pos.
	 */
	protected void addMove(IBoard board, Coordinate from,
			List<IMove> listOfMoves, int x, int y) {
		if (!isInBoard(x, y))
			return;
		Coordinate location;
		location = new Coordinate(x, y);
		if (board.getPieceAt(location) == null) {
			listOfMoves.add(new Move(from, location));
		}
	}

	/**
	 * Add a move to the list only if there is an enemy piece at target
	 * location.
	 * 
	 * @param listOfMoves
	 *            List to add move to.
	 * @param x
	 *            Target X-pos.
	 * @param y
	 *            Target Y-pos.
	 */
	protected void addAttackMove(IBoard board, Coordinate from,
			List<IMove> listOfMoves, int x, int y) {
		if (!isInBoard(x, y))
			return;
		Coordinate location = new Coordinate(x, y);
		IPiece piece = board.getPieceAt(location);
		if (piece != null && piece.getColor() != getColor()) {
			listOfMoves.add(new Move(from, location));
		}
	}

	/**
	 * True if the coords are within the board. (0 <= x < 8)
	 */
	protected boolean isInBoard(int x, int y) {
		return x >= 0 && x < board_size && y >= 0 && y < board_size;
	}

	/**
	 * Add all moves in a diagonal line.
	 * 
	 * @see addMovesInDirection()
	 */
	protected void addDiagonalMoves(List<IMove> listOfMoves, IBoard board,
			Coordinate from, final int xDir, final int yDir) {
		if ((xDir != -1 && xDir != +1) || (yDir != -1 && yDir != +1)) {
			throw new IllegalArgumentException(
					"xDir and yDir must be either -1 or +1");
		}

		addMovesInDirection(listOfMoves, board, from, xDir, yDir);
	}

	/**
	 * 
	 * Add all moves in a vertical or horizontal line.
	 * 
	 * @see addMovesInDirection()
	 */
	protected void addStraightLineMoves(List<IMove> listOfMoves, IBoard board,
			Coordinate from, final int xDir, final int yDir) {
		if (xDir != 0 && yDir != 0) {
			throw new IllegalArgumentException("Either xDir or yDir must be 0.");
		}

		addMovesInDirection(listOfMoves, board, from, xDir, yDir);
	}

	/*
	 * Stops if it hits the edges of the board or a piece. Adds moves to attack
	 * enemy pieces if any are encountered.
	 * 
	 * @param listOfMoves
	 * 
	 * @param board
	 * 
	 * @param from Piece location
	 * 
	 * @param xDir X direction: -1, 0 or +1
	 * 
	 * @param yDir Y direction: -1, 0 or +1
	 */
	private void addMovesInDirection(List<IMove> listOfMoves, IBoard board,
			Coordinate from, final int xDir, final int yDir) {
		if ((xDir != -1 && xDir != +1 && xDir != 0)
				|| (yDir != -1 && yDir != +1 && yDir != 0)) {
			throw new IllegalArgumentException(
					"xDir and yDir must be either -1, 0 or +1");
		}
		if (xDir == 0 && yDir == 0) {
			throw new IllegalArgumentException(
					"xDir and yDir can not both be 0");
		}
		PlayerColor opponentColor = getColor().other();

		int x = from.getX();
		int y = from.getY();

		while (true) {
			x += xDir;
			y += yDir;

			if (!isInBoard(x, y)) {
				break;
			}

			Coordinate destination = new Coordinate(x, y);
			IPiece piece = board.getPieceAt(destination);
			if (piece == null) {
				listOfMoves.add(new Move(from, destination));
			} else {
				if (piece.getColor() == opponentColor) {
					listOfMoves.add(new Move(from, destination));
				}
				break;
			}
		}
	}

	/**
	 * Remove invalid moves from the list.
	 */
	public static void removeInvalidMoves(List<IMove> listOfMoves,
			IBoard board, PlayerColor color) {
		for (int i = listOfMoves.size() - 1; i >= 0; i--) {
			IMove move = listOfMoves.get(i);
			if (!move.isValid(board, color)) {
				listOfMoves.remove(i);
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + board_size;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((piece == null) ? 0 : piece.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (color != other.color)
			return false;
		return true;
	}
}