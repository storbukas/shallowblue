package no.uib.inf112.group4.framework.moves;

import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PerformedMove;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;

public class PromotionMove extends AbstractMove {

	final IMove move;
	IPiece piece;

	public PromotionMove(IMove move, IPiece promotionChoice) {
		this.move = move;
		this.piece = promotionChoice;
	}

	@Override
	public Coordinate getLocation() {
		return move.getLocation();
	}

	@Override
	public Coordinate getDestination() {
		return move.getDestination();
	}

	@Override
	public IBoard execute(IBoard board) {
		return execute(board, board.getTurn());
	}
	
	@Override
	public IBoard execute(IBoard board, PlayerColor color) {
		assert (isValid(board, color));

		Coordinate coordinate = move.getDestination();
		IPiece originalPiece = board.getPieceAt(move.getLocation());

		PerformedMove[] moves = Move.addPerformedMove(board, this, color);

		board = move.execute(board, color);
		board = board.removePiece(originalPiece);
		board = board.addPiece(piece, coordinate);

		return new Board(board.getDimension(), moves, board.getData(), board.getCaptureCount());
	}

	@Override
	public boolean isValid(IBoard board, PlayerColor color) {
		// Check that a valid piece type has been selected
		if (piece == null) {
			return false;
		}

		return move.isValid(board, color);
	}

	public void setPromotionPiece(IPiece promotionPiece) {
		piece = promotionPiece;
	}

	public IPiece getPromotionPiece() {
		return piece;
	}

}
