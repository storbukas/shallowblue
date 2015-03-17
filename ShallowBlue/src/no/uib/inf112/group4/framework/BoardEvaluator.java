package no.uib.inf112.group4.framework;

import java.util.List;

import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IBoardEvaluator;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.pieces.Bishop;
import no.uib.inf112.group4.pieces.Knight;
import no.uib.inf112.group4.pieces.Pawn;

public class BoardEvaluator implements IBoardEvaluator {
	final static int FULL_BOARD_VALUE = 39; // Verdien til alle pieces med nytt
											// brett

	public class BoardEvaluation {
		public int value;
		public boolean checkmate;
		public boolean draw;
	}
	
	private int extraBishopValue(int totalPieceValue, int bishopCount) {
		if (totalPieceValue < 50) {
			return bishopCount * -1;
		}
		return 0;
	}
	
	private int extraKnightValue(int totalPieceValue, int knightCount) {
		if (totalPieceValue < 27) {
			return knightCount * -2;
		}
		return 0;
	}
	
	private int extraPawnValue(int totalPieceValue, int pawnCount) {
		if (totalPieceValue < 27) {
			return pawnCount;
		}
		return 0;
	}

	@Override
	public BoardEvaluation evaluate(IBoard board, PlayerColor color) {
		// TODO: Implement return statement for check mate
		BoardAnalyzer analyzer = board.getAnalyzer();

		BoardEvaluation e = new BoardEvaluation();

		int white = 0;
		int black = 0;

		List<IPiece> pieces = board.getAllPieces();

		int[] pawnCount = new int[2];
		int[] bishopCount = new int[2];
		int[] knightCount = new int[2];
		for (IPiece piece : pieces) {
			if (piece.getColor() == PlayerColor.WHITE) {
				white += piece.getPieceValue();
			} else {
				black += piece.getPieceValue();
			}
			
			int index = piece.getColor().ordinal();
			if (piece instanceof Pawn) {
				pawnCount[index]++;
			} else if (piece instanceof Bishop) {
				bishopCount[index]++;
			} else if (piece instanceof Knight) {
				knightCount[index]++;
			}
		}
		
		int totalPieceValue = white + black;
		int blackIndex = PlayerColor.BLACK.ordinal();
		int whiteIndex = PlayerColor.WHITE.ordinal();
		black += extraBishopValue(totalPieceValue, bishopCount[blackIndex]);
		black += extraKnightValue(totalPieceValue, bishopCount[blackIndex]);
		black += extraPawnValue(totalPieceValue, bishopCount[blackIndex]);
		white += extraBishopValue(totalPieceValue, bishopCount[whiteIndex]);
		white += extraKnightValue(totalPieceValue, bishopCount[whiteIndex]);
		white += extraPawnValue(totalPieceValue, bishopCount[whiteIndex]);

		// Stalemate
		if (analyzer.isStalemate()) {
			e.draw = true;
			black += 90;
		} else if (analyzer.canClaimDraw()) {
			if (board.getTurn() == PlayerColor.WHITE) {
				// White can stop black from claiming draw
				//black += 50;
			} else {
				// Black can claim draw immediately!
				e.draw = true;
				black += 4;
			}
		}

		// Checkmate ?
		if (analyzer.isCheckmate(PlayerColor.BLACK)) {
			white += 100;
			e.checkmate = true;
		}

		if (analyzer.isCheckmate(PlayerColor.WHITE)) {
			black += 100;
			e.checkmate = true;
		}

		if (color == null) {
			e.value = white - black;
		} else if (color == PlayerColor.BLACK) {
			e.value = Math.abs(white - FULL_BOARD_VALUE);
		} else {
			e.value = Math.abs(black - FULL_BOARD_VALUE);
		}

		return e;
	}

	@Override
	public BoardEvaluation evaluate(IBoard board) {
		return evaluate(board, null);
	}
}
