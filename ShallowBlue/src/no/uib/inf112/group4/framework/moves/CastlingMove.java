package no.uib.inf112.group4.framework.moves;

import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PerformedMove;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;

public class CastlingMove extends AbstractMove {
	final IMove kingMove;
	final IMove rookMove;

	public CastlingMove(IMove kingMove, IMove rookMove) {
		this.kingMove = kingMove;
		this.rookMove = rookMove;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kingMove == null) ? 0 : kingMove.hashCode());
		result = prime * result
				+ ((rookMove == null) ? 0 : rookMove.hashCode());
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
		CastlingMove other = (CastlingMove) obj;
		if (kingMove == null) {
			if (other.kingMove != null)
				return false;
		} else if (!kingMove.equals(other.kingMove))
			return false;
		if (rookMove == null) {
			if (other.rookMove != null)
				return false;
		} else if (!rookMove.equals(other.rookMove))
			return false;
		return true;
	}

	public IMove getKingMove() {
		return kingMove;
	}

	public IMove getRookMove() {
		return rookMove;
	}
	
	@Override
	public IBoard execute(IBoard board) {
		return execute(board, board.getTurn());
	}

	@Override
	public IBoard execute(IBoard board, PlayerColor color) {
		assert (isValid(board, color));

		PerformedMove[] moves = Move.addPerformedMove(board, this, color);

		board = kingMove.execute(board, color);
		board = rookMove.execute(board, color);

		return new Board(board.getDimension(), moves, board.getData(), board.getCaptureCount());
	}

	@Override
	public Coordinate getLocation() {
		return kingMove.getLocation();
	}

	@Override
	public Coordinate getDestination() {
		return kingMove.getDestination();
	}

	@Override
	public boolean isValid(IBoard board, PlayerColor color) {
		// TODO: Does not check everything.
		boolean ok = kingMove.isValid(board, color)
				&& rookMove.isValid(board, color);
		setValidationMessage("" + kingMove.getValidationMessage() + " "
				+ rookMove.getValidationMessage());
		return ok;
	}

	public boolean isKingSideCastling() {
		return !isQueenSideCastling();
	}

	public boolean isQueenSideCastling() {
		if (getRookMove().getLocation().getX() == 0)
			return true;
		else
			return false;
	}
}
