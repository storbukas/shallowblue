package no.uib.inf112.group4.framework;

import no.uib.inf112.group4.framework.moves.CastlingMove;
import no.uib.inf112.group4.framework.moves.EnPassantMove;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;

public class PerformedMove {
	public class ModifiedPiece {
		public final IPiece piece;
		public final Coordinate source;
		public final Coordinate destination;
		public final boolean removed;

		public ModifiedPiece(IPiece piece, Coordinate source,
				Coordinate destination, boolean removed) {
			assert (piece != null);
			assert (source != null);

			this.piece = piece;
			this.source = source;
			this.destination = destination;
			this.removed = removed;
		}
	}

	public final PlayerColor player;
	public final IMove move;
	public final ModifiedPiece[] modifiedPieces;
	public final IBoard board;

	/**
	 * @param move The move to be executed
	 * @param board The board as it was before the move was executed
	 * @param player The player who performed the move
	 */
	public PerformedMove(IMove move, IBoard board, PlayerColor player) {
		this.move = move;
		this.player = player;
		this.board = board;

		if (move instanceof CastlingMove) {
			IMove rookMove = ((CastlingMove) move).getRookMove();
			IMove kingMove = ((CastlingMove) move).getKingMove();
			IPiece rook = board.getPieceAt(rookMove.getLocation());
			IPiece king = board.getPieceAt(kingMove.getLocation());

			modifiedPieces = new ModifiedPiece[2];
			modifiedPieces[0] = new ModifiedPiece(rook, rookMove.getLocation(),
					rookMove.getDestination(), false);
			modifiedPieces[1] = new ModifiedPiece(king, kingMove.getLocation(),
					kingMove.getDestination(), false);

		} else if (move instanceof EnPassantMove) {
			EnPassantMove epMove = (EnPassantMove) move;
			IPiece moved = board.getPieceAt(epMove.getLocation());
			IPiece attacked = board.getPieceAt(epMove.getAttackingCoordinate());

			modifiedPieces = new ModifiedPiece[2];
			modifiedPieces[0] = new ModifiedPiece(moved, move.getLocation(),
					move.getDestination(), false);
			modifiedPieces[1] = new ModifiedPiece(attacked,
					epMove.getAttackingCoordinate(), null, true);

		} else {
			IPiece moved = board.getPieceAt(move.getLocation());
			IPiece attacked = board.getPieceAt(move.getDestination());

			if (attacked != null) {
				modifiedPieces = new ModifiedPiece[2];
				modifiedPieces[1] = new ModifiedPiece(attacked,
						move.getDestination(), null, true);
			} else {
				modifiedPieces = new ModifiedPiece[1];
			}

			modifiedPieces[0] = new ModifiedPiece(moved, move.getLocation(),
					move.getDestination(), false);
		}
	}
}
