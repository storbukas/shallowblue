package no.uib.inf112.group4.pieces;

import java.util.ArrayList;
import java.util.List;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;

/**
 * This class represent the Bishop piece in the chess game.
 * 
 */
public class Knight extends Piece {
	public Knight(PlayerColor color) {
		super("Knight", color);
	}

	@Override
	public String getRepresentation() {
		return "N";
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
		int board_size = board.getDimension();
		List<IMove> listOfMoves = new ArrayList<IMove>();
		PlayerColor myColor = getColor();
		PlayerColor other = myColor.other();
		int x;
		int y;
		// Two up and one right
		x = from.getX() + 1;
		y = from.getY() + 2;
		if (x < board_size) {
			if (y < board_size) {
				Coordinate destination = new Coordinate(x, y);
				if (board.getPieceAt(destination) == null) {
					listOfMoves.add(new Move(from, destination));
				}
				// Testing for enemy piece/attack
				else if (board.getPieceAt(destination) != null) {
					IPiece piece = board.getPieceAt(destination);
					if (piece.getColor() == other) {
						listOfMoves.add(new Move(from, destination));
					}
				}
			}
		}
		// Two up and one left
		x = from.getX() - 1;
		y = from.getY() + 2;
		if (x < board_size && x >= 0) {
			if (y < board_size) {
				Coordinate destination = new Coordinate(x, y);
				if (board.getPieceAt(destination) == null) {
					listOfMoves.add(new Move(from, destination));
				}
				// Testing for enemy piece/attack
				else if (board.getPieceAt(destination) != null) {
					IPiece piece = board.getPieceAt(destination);
					if (piece.getColor() == other) {
						listOfMoves.add(new Move(from, destination));
					}
				}
			}
		}
		// One up and two left
		x = from.getX() - 2;
		y = from.getY() + 1;
		if (x < board_size && x >= 0) {
			if (y < board_size) {
				Coordinate destination = new Coordinate(x, y);
				if (board.getPieceAt(destination) == null) {
					listOfMoves.add(new Move(from, destination));
				}
				// Testing for enemy piece/attack
				else if (board.getPieceAt(destination) != null) {
					IPiece piece = board.getPieceAt(destination);
					if (piece.getColor() == other) {
						listOfMoves.add(new Move(from, destination));
					}
				}
			}
		}
		// One up and two right
		x = from.getX() + 2;
		y = from.getY() + 1;
		if (x < board_size) {
			if (y < board_size) {
				Coordinate destination = new Coordinate(x, y);
				if (board.getPieceAt(destination) == null) {
					listOfMoves.add(new Move(from, destination));
				}
				// Testing for enemy piece/attack
				else if (board.getPieceAt(destination) != null) {
					IPiece piece = board.getPieceAt(destination);
					if (piece.getColor() == other) {
						listOfMoves.add(new Move(from, destination));
					}
				}
			}
		}
		// One down and two right
		x = from.getX() + 2;
		y = from.getY() - 1;
		if (x < board_size) {
			if (y < board_size && y >= 0) {
				Coordinate destination = new Coordinate(x, y);
				if (board.getPieceAt(destination) == null) {
					listOfMoves.add(new Move(from, destination));
				}
				// Testing for enemy piece/attack
				else if (board.getPieceAt(destination) != null) {
					IPiece piece = board.getPieceAt(destination);
					if (piece.getColor() == other) {
						listOfMoves.add(new Move(from, destination));
					}
				}
			}
		}
		// One down and two left
		x = from.getX() - 2;
		y = from.getY() - 1;
		if (x < board_size && x >= 0) {
			if (y < board_size && y >= 0) {
				Coordinate destination = new Coordinate(x, y);
				if (board.getPieceAt(destination) == null) {
					listOfMoves.add(new Move(from, destination));
				}
				// Testing for enemy piece/attack
				else if (board.getPieceAt(destination) != null) {
					IPiece piece = board.getPieceAt(destination);
					if (piece.getColor() == other) {
						listOfMoves.add(new Move(from, destination));
					}
				}
			}
		}

		// Two down and one right
		x = from.getX() + 1;
		y = from.getY() - 2;
		if (x < board_size) {
			if (y < board_size && y >= 0) {
				Coordinate destination = new Coordinate(x, y);
				if (board.getPieceAt(destination) == null) {
					listOfMoves.add(new Move(from, destination));
				}
				// Testing for enemy piece/attack
				else if (board.getPieceAt(destination) != null) {
					IPiece piece = board.getPieceAt(destination);
					if (piece.getColor() == other) {
						listOfMoves.add(new Move(from, destination));
					}
				}
			}
		}
		// Two down and one left
		x = from.getX() - 1;
		y = from.getY() - 2;
		if (x < board_size && x >= 0) {
			if (y < board_size && y >= 0) {
				Coordinate destination = new Coordinate(x, y);
				if (board.getPieceAt(destination) == null) {
					listOfMoves.add(new Move(from, destination));
				}
				// Testing for enemy piece/attack
				else if (board.getPieceAt(destination) != null) {
					IPiece piece = board.getPieceAt(destination);
					if (piece.getColor() == other) {
						listOfMoves.add(new Move(from, destination));
					}
				}
			}
		}
		return listOfMoves;
	}

	@Override
	public int getPieceValue() {
		return 3;
	}
	
	@Override
	public PieceType getType() {
		return PieceType.KNIGHT;
	}
}