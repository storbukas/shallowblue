package no.uib.inf112.group4.pieces;

import java.util.ArrayList;
import java.util.List;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.EnPassantMove;
import no.uib.inf112.group4.framework.moves.PromotionMove;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;

/**
 * This class represent the Pawn piece in the chess game
 * 
 */
public class Pawn extends Piece {
	public Pawn(PlayerColor color) {
		super("Pawn", color);
	}

	/**
	 * This method finds every valid moves for a piece from a location.
	 * 
	 * @param board
	 *            the game board
	 * @param from
	 *            Coordinates from where you want to move a piece from
	 * @return a list of valid moves
	 */
	@Override
	public List<IMove> getMoves(IBoard board, Coordinate from) {
		List<IMove> listOfMoves = new ArrayList<IMove>();
		int x = from.getX();
		int y = from.getY();
		// White piece
		if (getColor() == PlayerColor.WHITE && y < board.getDimension() - 1) {
			Coordinate OneCoordinateUp = new Coordinate(x, y + 1);

			// Move forward one step
			if (board.getPieceAt(OneCoordinateUp) == null) {
				addMove(board, from, listOfMoves, x, y + 1);
			}

			// Move forward two steps
			if (y == 1) {
				Coordinate TwoCoordinateUp = new Coordinate(x, y + 2);
				if ((board.getPieceAt(OneCoordinateUp) == null)
						&& (board.getPieceAt(TwoCoordinateUp) == null)) {
					addMove(board, from, listOfMoves, x, y + 2);
				}
			}

			// Attack move
			diagonalAttack(board, from, listOfMoves, x + 1, y + 1);
			diagonalAttack(board, from, listOfMoves, x - 1, y + 1);

			// En passant
			if (y == 4) {
				IMove prevMove = board.getPreviousMove();
				if (prevMove != null
						&& board.getPieceAt(prevMove.getDestination()) != null) {
					if (board.getPieceAt(prevMove.getDestination())
							.getRepresentation().equals("P")) {
						if (prevMove.getLocation().getY() == 6
								&& prevMove.getDestination().getY() == 4) {
							// To the right
							if (prevMove.getDestination().getX() == x + 1) {
								IMove move = new EnPassantMove(new Coordinate(
										x, y), new Coordinate(prevMove
										.getDestination().getX(), y + 1));
								listOfMoves.add(move);
							} // To the left
							else if (prevMove.getDestination().getX() == x - 1) {
								IMove move = new EnPassantMove(new Coordinate(
										x, y), new Coordinate(prevMove
										.getDestination().getX(), y + 1));
								listOfMoves.add(move);
							}
						}
					}
				}
			}
		}
		// Black piece
		else if (getColor() == PlayerColor.BLACK && y > 0) {
			Coordinate OneCoordinateDown = new Coordinate(x, y - 1);
			// Move forward one step
			if (board.getPieceAt(OneCoordinateDown) == null) {
				addMove(board, from, listOfMoves, x, y - 1);
			}

			// Move forward two steps
			if (y == 6) {
				Coordinate TwoCoordinateDown = new Coordinate(x, y - 2);
				if (board.getPieceAt(OneCoordinateDown) == null
						&& board.getPieceAt(TwoCoordinateDown) == null) {
					addMove(board, from, listOfMoves, x, y - 2);
				}
			}

			// Attack move
			// Diagonal down right attack
			diagonalAttack(board, from, listOfMoves, x + 1, y - 1);
			// Diagonal down left attack
			diagonalAttack(board, from, listOfMoves, x - 1, y - 1);

			// En passant
			if (y == 3) {
				IMove prevMove = board.getPreviousMove();
				if (prevMove != null
						&& board.getPieceAt(prevMove.getDestination()) != null) {
					if (board.getPieceAt(prevMove.getDestination())
							.getRepresentation().equals("P")
							|| board.getPieceAt(prevMove.getDestination())
									.getRepresentation().equals("p")) {
						if (prevMove.getLocation().getY() == 1
								&& prevMove.getDestination().getY() == 3) {
							// To the right
							if (prevMove.getDestination().getX() == x + 1) {
								IMove move = new EnPassantMove(new Coordinate(
										x, y), new Coordinate(prevMove
										.getDestination().getX(), y - 1));
								listOfMoves.add(move);
							} // To the left
							else if (prevMove.getDestination().getX() == x - 1) {
								IMove move = new EnPassantMove(new Coordinate(
										x, y), new Coordinate(prevMove
										.getDestination().getX(), y - 1));
								listOfMoves.add(move);
							}
						}
					}
				}
			}
		}

		// Replace moves with promotionmoves if they move into the first or last
		// row
		for (int i = 0; i < listOfMoves.size(); i++) {
			IMove move = listOfMoves.get(i);
			int destY = move.getDestination().getY();
			if (destY == 0 || destY == 7) {
				IMove promotionMove = createPromotionMove(board, move);
				listOfMoves.remove(i);
				listOfMoves.add(i, promotionMove);
			}
		}

		return listOfMoves;
	}

	private void diagonalAttack(IBoard board, Coordinate from,
			List<IMove> listOfMoves, int x, int y) {
		assert (Math.abs(from.getX() - x) == 1);
		assert (Math.abs(from.getY() - y) == 1);

		addAttackMove(board, from, listOfMoves, x, y);
	}

	private PromotionMove createPromotionMove(IBoard board, IMove move) {
		Coordinate from = move.getLocation();
		PlayerColor color = board.getPieceAt(from).getColor();
		IPiece promotionPiece = new Queen(color);
		PromotionMove promotionMove = new PromotionMove(move, promotionPiece);
		return promotionMove;
	}

	@Override
	public int getPieceValue() {
		return 1;
	}

	@Override
	public PieceType getType() {
		return PieceType.PAWN;
	}
}