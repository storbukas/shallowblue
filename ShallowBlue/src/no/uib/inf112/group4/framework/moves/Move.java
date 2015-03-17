package no.uib.inf112.group4.framework.moves;

import java.util.Arrays;

import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PerformedMove;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;

/**
 * 
 * Contains two coordinates, and represent a move from one location to a
 * destination
 * 
 */
public class Move extends AbstractMove {
	// VARIABLES
	final Coordinate location;
	final Coordinate destination;

	// CONSTRUCTOR
	public Move(Coordinate location, Coordinate destination) {
		this.location = location;
		this.destination = destination;
	}

	// METHODS
	@Override
	public Coordinate getLocation() {
		return this.location;
	}

	@Override
	public Coordinate getDestination() {
		return this.destination;
	}
	
	@Override
	public IBoard execute(IBoard board) {
		return execute(board, board.getTurn());
	}

	@Override
	public IBoard execute(IBoard board, PlayerColor color) {
		// assert(isValid(board, color));
		PerformedMove[] moves = addPerformedMove(board, this, color);

		IPiece destinationPiece = board.getPieceAt(getDestination());
		if (destinationPiece != null) {
			board = board.capturePiece(destinationPiece);
		}
		
		IPiece sourcePiece = board.getPieceAt(getLocation());
		if (sourcePiece.getColor() != color) {
			throw new IllegalArgumentException("Tried to move opponent's pieces.");
		}
		board = board.movePiece(sourcePiece, getDestination());

		return new Board(board.getDimension(), moves, board.getData(), board.getCaptureCount());
	}

	public static PerformedMove[] addPerformedMove(IBoard board, IMove move,
			PlayerColor color) {
		PerformedMove oldMoves[] = board.getPerformedMoves();
		PerformedMove moves[] = Arrays.copyOf(oldMoves, oldMoves.length + 1);
		moves[moves.length - 1] = new PerformedMove(move, board, color);
		return moves;
	}

	// AUXILIARY
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Move other = (Move) obj;
		if (destination == null) {
			if (other.destination != null) {
				return false;
			}
		} else if (!destination.equals(other.destination)) {
			return false;
		}
		if (location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!location.equals(other.location)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Move [" + location + " to " + destination + "]";
	}
}
