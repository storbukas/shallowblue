package no.uib.inf112.group4.framework;

import java.util.Stack;

import no.uib.inf112.group4.framework.moves.CastlingMove;
import no.uib.inf112.group4.framework.moves.EnPassantMove;
import no.uib.inf112.group4.framework.moves.PromotionMove;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.pieces.Pawn;

/**
 * This class is used by the UI to display algebraic notations. These notations
 * follow FIDE rules and cover all the possible moves in chess.
 */
public class AlgebraicNotation {
	private class NotationParameters {
		public IMove move;
		public boolean isChecked;
		public boolean isCheckmate;
		public boolean isStalemate;

		public Coordinate from;
		public Coordinate to;
		public IPiece movingPiece;
		public String pieceName;
		public IPiece targetPiece;

		public boolean samePieceSameMove;
		public boolean samePieceSameMoveSameFile;

		public String fromLocation;
		public String toLocation;
	}

	private Stack<String> notations;
	private Stack<String> redoNotation;

	public AlgebraicNotation() {
		notations = new Stack<String>();
		redoNotation = new Stack<String>();
	}

	/**
	 * Add a notation to the stack.
	 * 
	 * @param move
	 * @param board
	 * @param isChecked
	 * @param isCheckmate
	 * @param isStalemate
	 */
	public void addNotation(IMove move, IBoard board, boolean isChecked,
			boolean isCheckmate, boolean isStalemate) {
		// file = a - h, rank = 1 - 8.

		NotationParameters parameters = setUpParameters(move, board, isChecked,
				isCheckmate, isStalemate);

		// Pawn
		if (parameters.movingPiece instanceof Pawn) {
			addPawnNotation(parameters);
		} else {
			// Piece
			if (move instanceof CastlingMove) {
				addCastlingNotation(move);
				return;
			}
			if (parameters.targetPiece != null) {
				addPieceAttackNotation(parameters);
			} else {
				addPieceMoveNotation(parameters);
			}
		}
	}

	/**
	 * This private local class is used to set up the parameters used in
	 * algebraic notations.
	 * 
	 * @param move
	 * @param board
	 * @param isChecked
	 * @param isCheckmate
	 * @param isStalemate
	 * @return
	 */
	private NotationParameters setUpParameters(IMove move, IBoard board,
			boolean isChecked, boolean isCheckmate, boolean isStalemate) {

		NotationParameters parameters = new NotationParameters();
		parameters.move = move;
		parameters.isChecked = isChecked;
		parameters.isCheckmate = isCheckmate;
		parameters.isStalemate = isStalemate;
		parameters.from = move.getLocation();
		parameters.to = move.getDestination();

		parameters.movingPiece = board.getPieceAt(parameters.from);
		parameters.pieceName = parameters.movingPiece.getRepresentation();
		parameters.targetPiece = board.getPieceAt(parameters.to);

		parameters.samePieceSameMove = identicalPieceCanMoveTo(
				parameters.movingPiece, board, parameters.to);
		parameters.samePieceSameMoveSameFile = identicalPieceCanMoveToAndOnSameFile(
				parameters.movingPiece, board, parameters.to, parameters.from);

		parameters.fromLocation = parameters.from.toString().toLowerCase();
		parameters.toLocation = parameters.to.toString().toLowerCase();

		return parameters;
	}

	/**
	 * Method used by addNotation to add a piece notation.
	 * 
	 * @param p
	 */
	private void addPieceMoveNotation(NotationParameters p) {
		if (p.samePieceSameMove == true) {
			addSamePieceSameMoveNotation(p);
		} else {
			addRegularPieceMoveNotation(p);
		}
	}

	/**
	 * Method used by addNotation to add a standard move notation for a piece.
	 * 
	 * @param p
	 */
	private void addRegularPieceMoveNotation(NotationParameters p) {

		String to = p.pieceName
				+ p.move.getDestination().toString().toLowerCase();

		if (!p.isChecked && !p.isCheckmate && !p.isStalemate) {
			notations.add(to);
		}
		// Move into check. (example: Bd5+)
		if (p.isChecked) {
			notations.add(to + "+");
		}
		// Move into checkmate. (example: Bd5#)
		if (p.isCheckmate) {
			notations.add(to + "#");
		}
		// Move into stalemate. (example Bd5 (=) )
		if (p.isStalemate) {
			notations.add(to + " (=)");
		}
	}

	/**
	 * If a piece is on the same file or rank as its clone piece (two horses on
	 * the same file), the notation is slighty different, this method takes care
	 * of these cases.
	 * 
	 * @param p
	 */
	private void addSamePieceSameMoveNotation(NotationParameters p) {

		String from;
		// Not on same file. (example: Nce5)
		if (!p.samePieceSameMoveSameFile) {
			from = p.pieceName + p.fromLocation.substring(0, 1);
			notations.add(from + p.toLocation);
		} else {
			// Same file (example: N6e5)
			from = p.pieceName + p.fromLocation.substring(1, 2);
			notations.add(from + p.toLocation);
		}
	}

	/**
	 * Adds an attacking piece notation.
	 * 
	 * @param p
	 */
	private void addPieceAttackNotation(NotationParameters p) {

		// If identical pieces can move and capture same square.
		if (p.samePieceSameMove) {
			// Not on same file. (example: Ncxe5)
			if (!p.samePieceSameMoveSameFile) {
				String file = p.fromLocation.substring(0, 1);
				notations.add(p.pieceName + file + "x" + p.toLocation);
			} else {
				// Same file (example: N6xe5)
				String rank = p.fromLocation.substring(1, 2);
				notations.add(p.pieceName + rank + "x" + p.toLocation);
			}
		} else { // Regular attack (example: Qxa5)
			String attackNotation = p.pieceName + "x" + p.toLocation;
			if (!p.isChecked && !p.isCheckmate && !p.isStalemate) {
				notations.add(attackNotation);
			}
			if (p.isChecked) {
				notations.add(attackNotation + "+");
			}
			if (p.isCheckmate) {
				notations.add(attackNotation + "#");
			}
			if (p.isStalemate) {
				notations.add(attackNotation + " (=)");
			}
		}
	}

	/**
	 * Adds castling notations.
	 * 
	 * @param move
	 */
	private void addCastlingNotation(IMove move) {
		// If castling move. (Queenside = 0-0-0, Kingside 0-0)
		if (((CastlingMove) move).isQueenSideCastling()) {
			notations.add("0-0-0");
		} else {
			notations.add("0-0");
		}
	}

	/**
	 * Adds pawn notation, this method covers pawn movement, special moves and
	 * pawn attacing.
	 * 
	 * @param p
	 */
	private void addPawnNotation(NotationParameters p) {

		// If EnPassant move. (example: exd6 e.p.)
		if (p.move instanceof EnPassantMove) {
			notations.add(createPawnAttackNotation(p.move) + " e.p.");
			return;
		}
		if (p.move instanceof PromotionMove) {
			IPiece promotedTo = ((PromotionMove) p.move).getPromotionPiece();
			addPromotionNotation(promotedTo, p.move.getDestination());
			return;
		}
		// If pawn is capturing.
		if (p.targetPiece != null) {
			// Attacking with pawn. (example: dxd7)
			if (!p.isChecked && !p.isCheckmate && !p.isStalemate) {
				notations.add(createPawnAttackNotation(p.move));
			}
			// Attacking with pawn into check. (example: dxd7+)
			if (p.isChecked) {
				notations.add(createPawnAttackNotation(p.move) + "+");
			}
			// Attacking with pawn into checkmate. (example: dxd7#)
			if (p.isCheckmate) {
				notations.add(createPawnAttackNotation(p.move) + "#");
			}
			// Pawn attack into stalemate (example: dxd7 (=) )
			if (p.isStalemate) {
				notations.add(createPawnAttackNotation(p.move) + " (=)");
			}
		} else { // Pawn regular move. (example: d5)
			String pawnDestination = p.move.getDestination().toString()
					.toLowerCase();
			if (!p.isChecked && !p.isCheckmate && !p.isStalemate) {
				notations.add(pawnDestination);
			}
			// Move pawn into check (example: d5+)
			if (p.isChecked) {
				notations.add(pawnDestination + "+");
			}
			// Move pawn into checkmate. (example: d5#)
			if (p.isCheckmate) {
				notations.add(pawnDestination + "#");
			}
			// Move pawn into staleMate. (example: d5 (=))
			if (p.isStalemate) {
				notations.add(pawnDestination + " (=)");
			}
		}
	}

	/**
	 * Used by addPawnNotation when adding pawn attacking notation.
	 * 
	 * @param move
	 * @return
	 */
	private String createPawnAttackNotation(IMove move) {
		String fromLocation = move.getLocation().toString().toLowerCase();
		String toLocation = move.getDestination().toString().toLowerCase();
		return fromLocation.substring(0, 1) + "x" + toLocation;
	}

	/**
	 * Two piece of the same type can move to the same location.
	 * 
	 * @param movingPiece
	 * @param board
	 * @param destination
	 * @return
	 */
	private boolean identicalPieceCanMoveTo(IPiece movingPiece, IBoard board,
			Coordinate destination) {
		for (IPiece piece : board.getAllPieces()) {
			if (movingPiece == piece) {
				continue;
			}
			if (movingPiece.getClass() == piece.getClass()
					&& movingPiece.getColor() == piece.getColor()) {
				for (IMove move : piece.getMoves(board,
						board.getCoordinate(piece))) {
					if (!move.isValid(board, piece.getColor()))
						continue;
					if (move.getDestination().equals(destination)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Two pieces of the same type can move to the same location and are on the
	 * same file. Example, two knights on same file, c6 and c4 and can move to
	 * same location, e5.
	 * 
	 * @param movingPiece
	 * @param board
	 * @param destination
	 * @param movingPieceLocation
	 * @return
	 */
	private boolean identicalPieceCanMoveToAndOnSameFile(IPiece movingPiece,
			IBoard board, Coordinate destination, Coordinate movingPieceLocation) {
		for (IPiece piece : board.getAllPieces()) {
			if (movingPiece == piece) {
				continue;
			}
			if (movingPiece.getClass() == piece.getClass()
					&& movingPiece.getColor() == piece.getColor()) {
				for (IMove move : piece.getMoves(board,
						board.getCoordinate(piece))) {
					if (!move.isValid(board, piece.getColor()))
						continue;
					if (move.getDestination().equals(destination)) {
						String pieceFrom = move.getLocation().toString()
								.substring(0, 1);
						String movingPieceFrom = movingPieceLocation.toString()
								.substring(0, 1);
						if (pieceFrom.equals(movingPieceFrom)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Used when adding a pawn promotion notaion.
	 * 
	 * @param promotedTo
	 * @param to
	 */
	public void addPromotionNotation(IPiece promotedTo, Coordinate to) {
		notations.add(to.toString().toLowerCase()
				+ promotedTo.getRepresentation().substring(0, 1));
	}

	public Stack<String> getNotations() {
		return notations;
	}

	public String getNotation(int index) {
		return notations.get(index);
	}

	public void removeNotation() {
		redoNotation.add(notations.peek());
		notations.pop();
	}

	/**
	 * If redo is used during a game, this methods adds the notation back to
	 * notation stack.
	 */
	public void addNotationFromRedoStack() {
		if (redoNotation.size() <= 0) {
			return;
		}
		notations.add(redoNotation.peek());
		redoNotation.pop();
	}

	public void printAll() {
		for (int i = 0; i < notations.size(); i++) {
			System.out.println(i + 1 + ". " + notations.get(i));
		}
	}

	public void printLast() {
		System.out.println(notations.size() + ". "
				+ notations.get(notations.size() - 1));

	}
	
}
