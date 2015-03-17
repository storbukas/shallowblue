package no.uib.inf112.group4.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uib.inf112.group4.framework.PerformedMove.ModifiedPiece;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.pieces.King;
import no.uib.inf112.group4.pieces.Pawn;
import no.uib.inf112.group4.pieces.Rook;

/**
 * This class provides utility methods for analyzing a board according to chess rules.
 */
public class BoardAnalyzer {
	IBoard board;
	private Map<PlayerColor, Boolean> checkedState = new HashMap<PlayerColor, Boolean>();
	
	public BoardAnalyzer(IBoard board) {
		if (board == null) {
			throw new IllegalArgumentException("Board can not be null.");
		}
		this.board = board;
	}

	public IBoard getBoard() {
		return board;
	}

	/**
	 * Get the king for the specified player.
	 */
	public IPiece getKing(PlayerColor color) {
		assert (color != null);

		for (IPiece piece : board.getPiecesWithColor(color)) {
			if (piece instanceof King) {
				return piece;
			}
		}
		throw new IllegalStateException("Could not find king!");
	}

	/**
	 * True if the king is checked and there are no moves to save him.
	 */
	public boolean isCheckmate(PlayerColor color) {
		assert (color != null);

		if (!isChecked(color)) {
			return false;
		}

		return !hasMoveWhereKingIsNotChecked(color);
	}

	/**
	 * True if the king is not checked and can not move.
	 */
	public boolean isStalemate() {
		return isStalemate(board.getTurn());
	}

	private boolean isStalemate(PlayerColor color) {
		assert (color != null);
		if (isChecked(color)) {
			return false;
		}
		return !hasMoveWhereKingIsNotChecked(color);
	}

	/**
	 * Check that there is a move such that the king is not threatened.
	 */
	public boolean hasMoveWhereKingIsNotChecked(PlayerColor color) {
		assert (color != null);
		for (IMove move : board.getAllMovesForColor(color)) {
			if (!move.isValid(board, color))
				continue;

			// If enemy could not attack king, the king can move.
			if (!willBeCheckedAfterMove(color, move)) {
				return true;
			}
		}
		return false;
	}

	public boolean willBeCheckedAfterMove(PlayerColor color,
			IMove move) {
		assert (color != null);
		assert (move != null);

		// Check if any enemy pieces can attack the king after the move.
		IBoard newBoard = move.execute(board, color);
		BoardAnalyzer analyzer = newBoard.getAnalyzer();
		return analyzer.isChecked(color);
	}

	/**
	 * True if the player's king is threatened.
	 */
	public boolean isChecked(PlayerColor color) {
		assert (color != null);
		if (checkedState.containsKey(color) == false) {
			_isChecked(color);
			checkedState.put(color, _isChecked(color));
		}
		return checkedState.get(color);
	}

	private boolean _isChecked(PlayerColor color) {
		IPiece king = getKing(color);
		Coordinate kingPos = board.getCoordinate(king);
		
		for (IMove move : board.getAllMovesForColor(color.other())) {
			if (move.getDestination().equals(kingPos)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * True if the fifty move rule or the threefold repetition rule is triggered.
	 */
	public boolean canClaimDraw() {
		return hasThreefoldRepetition() || checkFiftyMovesRule();
	}

	/**
	 * The game is drawn, upon a correct claim by the player having the move, 
	 * when the same position, for at least the third time (not necessarily 
	 * by a repetition of moves) has just appeared, and the player claiming 
	 * the draw has the move.
	 * 
	 * Positions are considered the same, 
	 *   - pieces of the same kind and color occupy the same squares
     *   - and the possible moves of all the pieces of both players are the same. 
     *   
     * Positions are not the same if a pawn that could have been captured en passant 
     * can no longer in this manner be captured or if the right to castle has been 
     * changed temporarily or permanently.
	 */
	public boolean hasThreefoldRepetition() {
		int count = 1;
		for (PerformedMove move : board.getPerformedMoves()) {
			IBoard earlierBoard = move.board;
			if (board.getTurn() != earlierBoard.getTurn()) {
				continue;
			}
			if (piecesHaveSamePositions(earlierBoard)
					&& boardsHaveSameMoves(earlierBoard)
					&& rightToCastleIsUnchanged(earlierBoard)) {
				count++;
				if (count >= 3) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean rightToCastleIsUnchanged(IBoard otherBoard) {
		BoardAnalyzer analyzer = otherBoard.getAnalyzer();
		boolean[] rights1 = getCastlingRights();
		boolean[] rights2 = analyzer.getCastlingRights();
		
		for (int i = 0; i < rights1.length; i++) {
			if (rights1[i] != rights2[i]) {
				return false;
			}
		}
		return true;
	}
	
	private boolean[] getCastlingRights() {
		boolean rights[] = {true, true, true, true};
		
		// IMPORTANT: The order must be preserved!
		Coordinate[] keyCoordinates = {
				new Coordinate("E1"),
				new Coordinate("E8"),
				new Coordinate("A1"),
				new Coordinate("H1"),
				new Coordinate("A8"),
				new Coordinate("H8")
		};
		
		for (int i = 0; i < keyCoordinates.length; i++) {
			IPiece piece = board.getPieceAt(keyCoordinates[i]);
			
			if (!isRookOrKing(piece) || board.hasPieceMoved(piece)) {
				if (i == 0) {
					// White king
					rights[0] = false;
					rights[1] = false;
				} else if (i == 1) {
					rights[2] = false;
					rights[3] = false;
				} else {
					rights[i - 2] = false;
				}
			}
		}
		
		return rights;
	}

	private boolean isRookOrKing(IPiece piece) {
		return piece instanceof King || piece instanceof Rook;
	}

	private boolean boardsHaveSameMoves(IBoard otherBoard) {
		List<IMove> moves1 = board.getAllValidMoves();
		List<IMove> moves2 = otherBoard.getAllValidMoves();
		if (moves1.size() != moves2.size()) {
			return false;
		}
		
		for (IMove move : moves1) {
			if (!moves2.contains(move)) {
				return false;
			}
		}
		
		return true;
	}

	private boolean piecesHaveSamePositions(IBoard otherBoard) {
		if (board.getAllPieces().size() != otherBoard.getAllPieces().size()) {
			return false;
		}
		
		for (IPiece piece : board.getAllPieces()) {
			Coordinate c = board.getCoordinate(piece);
			IPiece otherPiece = otherBoard.getPieceAt(c);
			if (piece.equals(otherPiece) == false) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if the last 50 consecutive moves have been made by each player without the
	 * movement of any pawn and without any capture.
	 */
	public boolean checkFiftyMovesRule() {
		final PerformedMove[] performedMoves = board.getPerformedMoves();
		final int nrMoves = performedMoves.length;
		if (nrMoves < 50) {
			return false;
		}
		
		// Check the last fify moves
		for (int i = nrMoves - 1; i > nrMoves - 51; i--) {
			PerformedMove move = performedMoves[i];
			for (ModifiedPiece modified : move.modifiedPieces) {
				if (modified.piece instanceof Pawn) {
					return false;
				}
				if (modified.removed == true) {
					return false;
				}
			}
		}
		return true;
	}
}
