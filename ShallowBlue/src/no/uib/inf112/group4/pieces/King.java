package no.uib.inf112.group4.pieces;

import java.util.ArrayList;
import java.util.List;

import no.uib.inf112.group4.framework.BoardAnalyzer;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.CastlingMove;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;

/**
 * This class represent the King piece in the chess game.
 * 
 */
public class King extends Piece {
	public static boolean checkingKing = false;

	public King(PlayerColor color) {
		super("King", color);
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

		addKingMove(listOfMoves, board, from, +1, +1); // Up+Right
		addKingMove(listOfMoves, board, from,  0, +1); // Up
		addKingMove(listOfMoves, board, from, -1, +1); // Up+Left
		addKingMove(listOfMoves, board, from, +1,  0); // Right
		addKingMove(listOfMoves, board, from, -1,  0); // Left
		addKingMove(listOfMoves, board, from, +1, -1); // Down+Right
		addKingMove(listOfMoves, board, from,  0, -1); // Down
		addKingMove(listOfMoves, board, from, -1, -1); // Down+Left

		
		// FIXME: This static variable can cause problems with threading!
		
		// Avoid stack overflow errors
		if (!King.checkingKing) {
			King.checkingKing = true;
			
			addCastling(listOfMoves, board, from);
			
			King.checkingKing = false;
		}
		return listOfMoves;
	}

	private void addCastling(List<IMove> listOfMoves, IBoard board,
			Coordinate from) {
		final int y = from.getY();
		
		final Coordinate whiteKingStart = new Coordinate(4, 0);
		final Coordinate blackKingStart = new Coordinate(4, 7);
		if (getColor() == PlayerColor.WHITE && !from.equals(whiteKingStart)) {
			return;
		}
		if (getColor() == PlayerColor.BLACK && !from.equals(blackKingStart)) {
			return;
		}
		if (kinghasMadeAMove(board) || kingIsChecked(board)) {
			return;
		}

		// King side castling
		{
			Coordinate rookLocation = new Coordinate(7, y);
			Coordinate crossedSquare = new Coordinate(5, y);
			Coordinate destinationSquare = new Coordinate(6, y);
			if (squaresAreEmpty(board, crossedSquare, destinationSquare)) {
				addCastlingMove(listOfMoves, board, from, rookLocation,
						crossedSquare, destinationSquare);
			}
		}

		// Queen side castling
		{
			Coordinate rookLocation = new Coordinate(0, y);
			Coordinate crossedSquare = new Coordinate(3, y);
			Coordinate destinationSquare = new Coordinate(2, y);
			if (squaresAreEmpty(board, crossedSquare, destinationSquare, new Coordinate(1, y))) {
				addCastlingMove(listOfMoves, board, from, rookLocation,
						crossedSquare, destinationSquare);
			}
		}
	}

	private void addCastlingMove(List<IMove> listOfMoves, IBoard board,
			Coordinate from, Coordinate rookLocation, Coordinate crossedSquare,
			Coordinate destinationSquare) {
		if ((board.getPieceAt(rookLocation) instanceof Rook) == false) {
			return;
		}
		
		Rook rook = (Rook) board.getPieceAt(rookLocation);
		boolean squaresAreThreathened = areSquaresThreathened(board,
				crossedSquare, destinationSquare);
		if (!rook.hasRookMoved(board) && !squaresAreThreathened) {
			IMove kingMove = new Move(from, destinationSquare);
			IMove rookMove = new Move(rookLocation, crossedSquare);
			listOfMoves.add(new CastlingMove(kingMove, rookMove));
		}
	}

	private boolean squaresAreEmpty(IBoard board, Coordinate... squares) {
		for (Coordinate square : squares) {
			if (board.hasPieceAt(square)) {
				return false;
			}
		}
		return true;
	}

	private boolean areSquaresThreathened(IBoard board, Coordinate... squares) {
		List<IPiece> enemyPieces = board.getPiecesWithColor(getColor().other());
		for (IPiece enemyPiece : enemyPieces) {
			Coordinate enemyCoordinate = board.getCoordinate(enemyPiece);
			List<IMove> moves = enemyPiece.getMoves(board, enemyCoordinate);
			for (Coordinate square : squares) {
				for (IMove move : moves) {
					if (move.getDestination().equals(square)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void addKingMove(List<IMove> listOfMoves, IBoard board,
			Coordinate from, int dirX, int dirY) {
		assert(Math.abs(dirX) <= 1);
		assert(Math.abs(dirY) <= 1);
		
		int x = from.getX() + dirX;
		int y = from.getY() + dirY;
		if (isInBoard(x, y)) {
			Coordinate destination = new Coordinate(x, y);
			if (board.getPieceAt(destination) == null) {
				listOfMoves.add(new Move(from, destination));
			}
			// Testing for enemy piece/attack
			else if (board.getPieceAt(destination) != null) {
				IPiece piece = board.getPieceAt(destination);
				if (piece.getColor() == getColor().other()) {
					listOfMoves.add(new Move(from, destination));
				}
			}
		}
	}

	private boolean kingIsChecked(IBoard board) {
		BoardAnalyzer analyzer = board.getAnalyzer();
		return analyzer.isChecked(getColor());
	}

	private boolean kinghasMadeAMove(IBoard board) {
		return board.hasPieceMoved(this);
	}

	@Override
	/**
	 * The king doesnt have any material value. The method return 0, so if
	 * someone tries to call the function in their calculation it wont do any different.
	 */
	public int getPieceValue() {
		return 0;
	}
	
	@Override
	public PieceType getType() {
		return PieceType.KING;
	}
}