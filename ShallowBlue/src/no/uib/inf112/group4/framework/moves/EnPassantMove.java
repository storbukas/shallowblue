package no.uib.inf112.group4.framework.moves;

import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PerformedMove;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IPiece;

public class EnPassantMove extends Move {
	public EnPassantMove(Coordinate location, Coordinate destination) {
		super(location, destination);
	}

	@Override
	public IBoard execute(IBoard board, PlayerColor color) {
		// assert(isValid(board, color));

		PerformedMove[] moves = addPerformedMove(board, this, color);

		board = super.execute(board, color);

		Coordinate coord = getAttackingCoordinate();
		board = board.removePiece(board.getPieceAt(coord));

		return new Board(board.getDimension(), moves, board.getData(), board.getCaptureCount());
	}

	public Coordinate getAttackingCoordinate() {
		int x = destination.getX();
		int y = location.getY();
		return new Coordinate(x, y);
	}

	@Override
	public boolean isValid(IBoard board, PlayerColor color) {
		IPiece piece = board.getPieceAt(getAttackingCoordinate());
		if (piece != null && piece.getColor() == color.other()) {
			setValidationMessage("Can only do en-passant if attacking opponent piece.");
		}
		return super.isValid(board, color);
	}
}
