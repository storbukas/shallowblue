package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.BoardAnalyzer;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.pieces.Pawn;

import org.junit.Test;

public class BoardAnalyzerTest {
	@Test
	public void testThreefoldRepeition__falseAtStartUp() {
		IBoard board = Board.createDefaultBoard();
		BoardAnalyzer analyzer = board.getAnalyzer();
		assertFalse(analyzer.hasThreefoldRepetition());
	}
	
	@Test
	public void testThreefoldRepetition__trueWhenMovingPiecesBackAndForthTwice() {
		IBoard board = createThreefoldRepetitionBoard();
		BoardAnalyzer analyzer = board.getAnalyzer();
		assertTrue(analyzer.hasThreefoldRepetition());
	}
	
	@Test
	public void testThreefoldRepetition__rightToCastleMustNotBeChanged() {
		IBoard board = Board.createDefaultBoard();
		IMove blackKingForward = new Move(new Coordinate("E8"), new Coordinate("E7"));
		IMove blackKingBack = new Move(new Coordinate("E7"), new Coordinate("E8"));
		IMove whiteKingForward = new Move(new Coordinate("E1"), new Coordinate("E2"));
		IMove whiteKingBack = new Move(new Coordinate("E2"), new Coordinate("E1"));
		
		// Move pawns so king can move
		board = doMove(board, "E2", "E3");
		board = doMove(board, "E7", "E6");
		
		board = whiteKingForward.execute(board);
		board = blackKingForward.execute(board);
		board = whiteKingBack.execute(board);
		board = blackKingBack.execute(board);
		
		board = whiteKingForward.execute(board);
		board = blackKingForward.execute(board);
		board = whiteKingBack.execute(board);
		board = blackKingBack.execute(board);
		
		BoardAnalyzer analyzer = board.getAnalyzer();
		assertFalse(analyzer.hasThreefoldRepetition());
	}

	@Test
	public void testThreefoldRepetition__falseIfOnePlayerHasDifferentMoves() {
		IBoard board = Board.createDefaultBoard();
		board = board.addPiece(new Pawn(PlayerColor.BLACK), new Coordinate("A4"));
		
		board = new Move(new Coordinate("B2"), new Coordinate("B4")).execute(board);
		
		IMove blackKnightForward = new Move(new Coordinate("B8"), new Coordinate("A6"));
		IMove blackKnightBack = new Move(new Coordinate("A6"), new Coordinate("B8"));
		IMove whiteKnightForward = new Move(new Coordinate("B1"), new Coordinate("A3"));
		IMove whiteKnightBack = new Move(new Coordinate("A3"), new Coordinate("B1"));
		
		board = blackKnightForward.execute(board);
		board = whiteKnightForward.execute(board);
		board = blackKnightBack.execute(board);
		board = whiteKnightBack.execute(board);
		
		board = blackKnightForward.execute(board);
		board = whiteKnightForward.execute(board);
		board = blackKnightBack.execute(board);
		board = whiteKnightBack.execute(board);
		
		BoardAnalyzer analyzer = board.getAnalyzer();
		assertFalse(analyzer.hasThreefoldRepetition());
	}

	private IBoard createThreefoldRepetitionBoard() {
		IBoard board = Board.createDefaultBoard();
		board = moveKnightsBackAndForth(board, 2 * 4);
		return board;
	}

	private IBoard moveKnightsBackAndForth(IBoard board, int numberOfMoves) {
		IMove blackKnightForward = new Move(new Coordinate("B8"), new Coordinate("A6"));
		IMove blackKnightBack = new Move(new Coordinate("A6"), new Coordinate("B8"));
		IMove whiteKnightForward = new Move(new Coordinate("B1"), new Coordinate("A3"));
		IMove whiteKnightBack = new Move(new Coordinate("A3"), new Coordinate("B1"));
		
		IMove[] moves = {
				whiteKnightForward,
				blackKnightForward,
				whiteKnightBack,
				blackKnightBack
		};
		
		for (int i = 0; i < numberOfMoves; i++) {
			board = moves[i % 4].execute(board);
		}
		
		return board;
	}
	
	@Test
	public void testFiftyMoveRule_falseWhenAPawnHasBeenMoved() {
		IBoard board = Board.createDefaultBoard();
		board = moveKnightsBackAndForth(board, 50);
		board = new Move(new Coordinate("D2"), new Coordinate("D3")).execute(board);
		BoardAnalyzer analyzer = board.getAnalyzer();
		assertFalse(analyzer.checkFiftyMovesRule());
	}
	
	@Test
	public void testFiftyMoveRule_trueForFiftyInertMoves() {
		IBoard board = Board.createDefaultBoard();
		board = moveKnightsBackAndForth(board, 50);
		BoardAnalyzer analyzer = board.getAnalyzer();
		assertTrue(analyzer.checkFiftyMovesRule());
	}
	
	@Test
	public void testFiftyMoveRule_falseForFortynineInertMoves() {
		IBoard board = Board.createDefaultBoard();
		board = moveKnightsBackAndForth(board, 49);
		BoardAnalyzer analyzer = board.getAnalyzer();
		assertFalse(analyzer.checkFiftyMovesRule());
	}
	
	@Test
	public void testFiftyMoveRule_falseIfAPieceIsCaptured() {
		IBoard board = Board.createDefaultBoard();
		board = moveKnightsBackAndForth(board,13*4);
		
		// Illegal chess move, but the board does not enforce all chess rules.
		board = doMove(board, "A1", "A8"); 
		
		BoardAnalyzer analyzer = board.getAnalyzer();
		assertFalse(analyzer.checkFiftyMovesRule());
	}

	private IBoard doMove(IBoard board, String from, String to) {
		IMove move = new Move(new Coordinate(from), new Coordinate(to));
		return move.execute(board);
	}
}
