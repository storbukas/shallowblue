package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.BoardAnalyzer;
import no.uib.inf112.group4.framework.BoardEvaluator;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.MiniMax;
import no.uib.inf112.group4.framework.MiniMax.State;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.pieces.Bishop;
import no.uib.inf112.group4.pieces.King;
import no.uib.inf112.group4.pieces.Knight;
import no.uib.inf112.group4.pieces.Pawn;
import no.uib.inf112.group4.pieces.Queen;
import no.uib.inf112.group4.pieces.Rook;

import org.junit.Before;
import org.junit.Test;

public class MiniMaxTest {

	private BoardEvaluator evaluator;
	private IBoard board;
	public MiniMax.State s;
	public Exception exc;

	@Before
	public void setUp() throws Exception {
		evaluator = new BoardEvaluator();
		board = new Board();
	}

	// @Test // Disabled since it can fail due to other circumstances.
	public void testFastAbort() {
		board = Board.createDefaultBoard();
		Thread t = createMiniMaxThread();

		t.start();
		assertTrue(t.isAlive());

		try {
			Thread.sleep(100);
			t.interrupt();
			Thread.sleep(20);
		} catch (InterruptedException e) {

		}
		assertFalse("Didn't terminate fast enough.", t.isAlive());
	}

	@Test
	public void testReturnsBestMoveWhenAborted() throws Exception {
		board = Board.createDefaultBoard();
		Thread t = createMiniMaxThread();

		t.start();
		assertTrue(t.isAlive());

		try {
			Thread.sleep(100);
			t.interrupt();
			t.join();
		} catch (InterruptedException e) {

		}

		assertFalse(t.isAlive());

		if (exc != null) {
			throw exc;
		}
		assertTrue(s != null);
	}

	private Thread createMiniMaxThread() {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					s = MiniMax.evaluate(board, evaluator, 10);
				} catch (Exception e) {
					exc = e;
				}
			}
		};
		return t;
	}

	@Test
	public void testPreferQueenOverPawn() {
		testPreferQueen(PlayerColor.WHITE);
	}

	@Test
	public void testPreferQueenOverPawn_ColorsSwapped() {
		testPreferQueen(PlayerColor.BLACK);
	}

	private void testPreferQueen(PlayerColor color) {
		IBoard board = new Board();
		board = board.addPiece(new King(color.other()), new Coordinate(2, 0));
		board = board.addPiece(new King(color), new Coordinate(0, 0));

		Coordinate startPos = new Coordinate(7, 7);
		Coordinate targetPos = new Coordinate(7, 6);
		board = board.addPiece(new Queen(color), startPos);
		board = board.addPiece(new Pawn(color.other()), new Coordinate(6, 6));
		board = board.addPiece(new Queen(color.other()), targetPos);

		if (color == PlayerColor.BLACK) {
			// Force black player's turn
			board = board.movePiece(board.getPieceAt(new Coordinate(2, 0)),
					new Coordinate(3, 0));
			board = new Move(new Coordinate(3, 0), new Coordinate(2, 0))
					.execute(board);
		}

		MiniMax.State s = minimaxEvaluate(board, evaluator, 1);
		assertEquals(new Move(startPos, targetPos), s.move);
	}

	@Test
	public void testPreferPawnOverRookIfSuicide() {
		IBoard board = new Board();
		board = board.addPiece(new King(PlayerColor.BLACK),
				new Coordinate("C8"));
		board = board.addPiece(new King(PlayerColor.WHITE),
				new Coordinate("A8"));

		board = board.addPiece(new Pawn(PlayerColor.WHITE),
				new Coordinate("G1"));
		board = board.addPiece(new Queen(PlayerColor.WHITE), new Coordinate(
				"H1"));
		board = board.addPiece(new Pawn(PlayerColor.BLACK),
				new Coordinate("H3"));
		board = board.addPiece(new Rook(PlayerColor.BLACK),
				new Coordinate("G2"));

		MiniMax.State s = minimaxEvaluate(board, evaluator, 2);

		assertEquals(new Move(new Coordinate("H1"), new Coordinate("H3")),
				s.move);
	}

	@Test
	public void testQueensPerformsCheckMate() {
		IBoard board = new Board();
		board = board.addPiece(new King(PlayerColor.WHITE),
				new Coordinate("H7"));
		board = board.addPiece(new King(PlayerColor.BLACK),
				new Coordinate("D1"));
		board = board.addPiece(new Rook(PlayerColor.BLACK),
				new Coordinate("E1"));
		board = board.addPiece(new Queen(PlayerColor.WHITE), new Coordinate(
				"E3"));
		board = board.addPiece(new Rook(PlayerColor.WHITE),
				new Coordinate("E2"));

		MiniMax.State s = minimaxEvaluate(board, evaluator, 4);

		assertEquals(new Move(new Coordinate("E3"), new Coordinate("D2")),
				s.move);
	}

	@Test
	public void testD1_attacksQueenWhenRookCanSetCheckMate() {
		addPiece(new King(PlayerColor.WHITE), "A1");
		addPiece(new King(PlayerColor.BLACK), "A8");

		addPiece(new Pawn(PlayerColor.WHITE), "F4");
		addPiece(new Knight(PlayerColor.WHITE), "E3");

		addPiece(new Rook(PlayerColor.BLACK), "H2");
		addPiece(new Rook(PlayerColor.BLACK), "G4");
		addPiece(new Queen(PlayerColor.BLACK), "F5");

		// Attacks Queen
		MiniMax.State s = minimaxEvaluate(board, evaluator, 1);
		executeMove(s.move);
		assertMove(s);

		// Black puts white in checkmate
		s = minimaxEvaluate(board, evaluator, 1);
		assertEquals(new Move(new Coordinate("G4"), new Coordinate("G1")),
				s.move);
	}

	private void assertMove(MiniMax.State s) {
		assertEquals(new Move(new Coordinate("E3"), new Coordinate("F5")),
				s.move);
	}

	@Test
	public void testD2_attacksRookWhenRookCanSetCheckMate() {
		addPiece(new King(PlayerColor.WHITE), "A1");
		addPiece(new King(PlayerColor.BLACK), "A8");

		addPiece(new Pawn(PlayerColor.WHITE), "D3");
		addPiece(new Pawn(PlayerColor.WHITE), "F4");
		addPiece(new Knight(PlayerColor.WHITE), "E3");

		addPiece(new Rook(PlayerColor.BLACK), "H2");
		addPiece(new Rook(PlayerColor.BLACK), "G4");
		addPiece(new Queen(PlayerColor.BLACK), "F5");

		// Attacks Rook
		MiniMax.State s = minimaxEvaluate(board, evaluator, 2);
		assertEquals(new Move(new Coordinate("E3"), new Coordinate("G4")),
				s.move);
	}

	@Test
	public void testD2_canNotSeeCheckMateInThreeMoves() {
		addPiece(new King(PlayerColor.WHITE), "A1");
		addPiece(new King(PlayerColor.BLACK), "A8");

		addPiece(new Rook(PlayerColor.WHITE), "E4");
		addPiece(new Queen(PlayerColor.WHITE), "F3");

		addPiece(new Pawn(PlayerColor.BLACK), "C4");

		// Attacks Rook
		for (int turns = 0; turns < 3; turns++) {
			MiniMax.State s = minimaxEvaluate(board, evaluator, 2);
			executeMove(s.move);
		}

		BoardAnalyzer analyzer = board.getAnalyzer();
		assertFalse(analyzer.isCheckmate(PlayerColor.BLACK));
	}

	@Test
	public void testD3_canNotSeeCheckMateInThreeMoves() {
		addPiece(new King(PlayerColor.WHITE), "A1");
		addPiece(new King(PlayerColor.BLACK), "A8");

		addPiece(new Rook(PlayerColor.WHITE), "E4");
		addPiece(new Queen(PlayerColor.WHITE), "F3");

		addPiece(new Pawn(PlayerColor.BLACK), "C4");

		// Attacks Rook
		for (int turns = 0; turns < 3; turns++) {
			MiniMax.State s = minimaxEvaluate(board, evaluator, 3);
			executeMove(s.move);
		}

		BoardAnalyzer analyzer = board.getAnalyzer();
		assertTrue(analyzer.isCheckmate(PlayerColor.BLACK));
	}

	@Test
	public void testD4_canNotSeeCheckMateInFiveMoves() {
		addPiece(new King(PlayerColor.WHITE), "A1");
		addPiece(new King(PlayerColor.BLACK), "A8");

		addPiece(new Rook(PlayerColor.WHITE), "H1");
		addPiece(new Rook(PlayerColor.WHITE), "F3");
		addPiece(new Bishop(PlayerColor.WHITE), "G2");

		addPiece(new Pawn(PlayerColor.BLACK), "C3");
		addPiece(new Knight(PlayerColor.BLACK), "E8");

		// Attacks Rook
		for (int turns = 0; turns < 5; turns++) {
			MiniMax.State s = minimaxEvaluate(board, evaluator, 4);
			executeMove(s.move);
		}

		BoardAnalyzer analyzer = board.getAnalyzer();
		assertFalse(analyzer.isCheckmate(PlayerColor.BLACK));
	}

	// @Test // Disabled test since it takes a few seconds to complete
	public void testD5_canNotSeeCheckMateInFiveMoves() {
		addPiece(new King(PlayerColor.WHITE), "A1");
		addPiece(new King(PlayerColor.BLACK), "A8");

		addPiece(new Rook(PlayerColor.WHITE), "H1");
		addPiece(new Rook(PlayerColor.WHITE), "F3");
		addPiece(new Bishop(PlayerColor.WHITE), "G2");

		addPiece(new Pawn(PlayerColor.BLACK), "C3");
		addPiece(new Knight(PlayerColor.BLACK), "E8");

		// Attacks Rook
		for (int turns = 0; turns < 5; turns++) {
			MiniMax.State s = minimaxEvaluate(board, evaluator, 5);
			executeMove(s.move);
		}

		BoardAnalyzer analyzer = board.getAnalyzer();
		assertTrue(analyzer.isCheckmate(PlayerColor.BLACK));
	}

	private State minimaxEvaluate(IBoard board2, BoardEvaluator evaluator2,
			int i) {
		return MiniMax.evaluate(board2, evaluator2, i);
	}

	private void addPiece(IPiece piece, String coord) {
		board = board.addPiece(piece, new Coordinate(coord));
	}

	private void executeMove(IMove move) {
		board = move.execute(board);
	}
}
