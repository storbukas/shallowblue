package no.uib.inf112.group4.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.pieces.Pawn;
import no.uib.inf112.group4.tests.Mocks.PieceTestClass;

import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

public class BoardTest extends TestCase {
	static Random rand = new Random();

	private IBoard board;

	private IPiece addPieceToBoard() {
		return addPieceToBoard(getRandomCoordinate());
	}

	private IPiece addPieceToBoard(Coordinate coordinate) {
		IPiece piece = new PieceTestClass();
		board = board.addPiece(piece, coordinate);
		return piece;
	}

	private Coordinate getRandomCoordinate() {
		return new Coordinate(rand.nextInt(8), rand.nextInt(8));
	}

	@Override
	@Before
	public void setUp() throws Exception {
		board = new Board();
	}

	@Test
	public void testNoPiecesWhenBoardIsCreated() {
		List<IPiece> pieces = board.getAllPieces();
		assertEquals(0, pieces.size());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetAllPieces() {
		IPiece piece1 = addPieceToBoard(new Coordinate(1, 1));
		IPiece piece2 = addPieceToBoard(new Coordinate(1, 2));

		List<IPiece> pieces = board.getAllPieces();

		assertThat(pieces, JUnitMatchers.hasItems(piece1, piece2));
	}

	@Test
	public void testAddPiece() {
		Coordinate coordinate = getRandomCoordinate();
		IPiece piece = addPieceToBoard(coordinate);

		assertThat(board.getPieceAt(coordinate), sameInstance(piece));
	}

	@Test
	public void testEmptySquareIsEmpty() {
		Coordinate c = getRandomCoordinate();
		assertNull(board.getPieceAt(c));
	}

	@Test
	public void testGetCoordinates() {
		Coordinate c = getRandomCoordinate();
		IPiece piece = addPieceToBoard(c);

		assertThat(board.getCoordinate(piece), is(equalTo(c)));
	}

	@Test
	public void testHasPieceReturnsFalseWhenPieceIsNotOnBoard() {
		IPiece piece = new PieceTestClass();
		assertFalse(board.hasPiece(piece));
	}

	@Test
	public void testHasPieceReturnsTrueWhenPieceIsOnBoard() {
		IPiece piece = addPieceToBoard();
		assertTrue(board.hasPiece(piece));
	}

	@Test
	public void testMovePiece() {
		Coordinate coordinate = getRandomCoordinate();
		IPiece piece = addPieceToBoard(coordinate);

		// Get a destination different from source
		Coordinate destination;
		do {
			destination = getRandomCoordinate();
		} while (destination.equals(coordinate));

		board = board.movePiece(piece, destination);

		assertThat(board.getPieceAt(coordinate), nullValue());
		assertThat(board.getPieceAt(destination), sameInstance(piece));
	}

	@Test
	public void testMovingAPieceNotOnBoardThrowsException() {
		IPiece piece = new PieceTestClass();
		try {
			board.movePiece(piece, getRandomCoordinate());
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("Should have thrown exception");
	}

	@Test
	public void testMovingAPieceOverAnotherThrowsException() {
		IPiece piece1 = new PieceTestClass();
		IPiece piece2 = new PieceTestClass();
		Coordinate c1 = new Coordinate(1, 1);
		Coordinate c2 = new Coordinate(1, 2);
		board.addPiece(piece1, c1);
		board.addPiece(piece2, c2);

		try {
			board.movePiece(piece1, c2);
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("Should have thrown exception");
	}

	@Test
	public void testRemovePiece() {
		Coordinate coordinate = getRandomCoordinate();
		IPiece piece = addPieceToBoard(coordinate);

		board = board.removePiece(piece);

		assertThat(board.getPieceAt(coordinate), nullValue());
	}

	@Test
	public void testAddingTheSamePieceTwiceShouldFail() {
		IPiece piece = new PieceTestClass();
		Coordinate c1 = new Coordinate(1, 1);
		Coordinate c2 = new Coordinate(1, 2);

		board = board.addPiece(piece, c1);
		try {
			board.addPiece(piece, c2);
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("Should have thrown an exception.");
	}

	@Test
	public void testAddingAPieceOverAnotherPieceShouldFail() {
		Coordinate coordinate = getRandomCoordinate();
		board = board.addPiece(new PieceTestClass(), coordinate);

		try {
			board.addPiece(new PieceTestClass(), coordinate);
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("Should have thrown an exception.");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetWhitePieces() {
		IPiece blackPiece = new PieceTestClass(PlayerColor.BLACK);
		board = board.addPiece(blackPiece, new Coordinate(2, 2));
		IPiece whitePiece = new PieceTestClass(PlayerColor.WHITE);
		board = board.addPiece(whitePiece, new Coordinate(5, 5));

		List<IPiece> whitePieces = board.getWhitePieces();
		assertThat(whitePieces, not(JUnitMatchers.hasItem(blackPiece)));
		assertThat(whitePieces, JUnitMatchers.hasItem(whitePiece));
		assertContainsSameElements(whitePieces,
				board.getPiecesWithColor(PlayerColor.WHITE));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetBlackPieces() {
		IPiece blackPiece = new PieceTestClass(PlayerColor.BLACK);
		board = board.addPiece(blackPiece, new Coordinate(2, 2));
		IPiece whitePiece = new PieceTestClass(PlayerColor.WHITE);
		board = board.addPiece(whitePiece, new Coordinate(5, 5));

		List<IPiece> blackPieces = board.getBlackPieces();
		assertThat(blackPieces, JUnitMatchers.hasItem(blackPiece));
		assertThat(blackPieces, not(JUnitMatchers.hasItem(whitePiece)));
		assertContainsSameElements(board.getBlackPieces(),
				board.getPiecesWithColor(PlayerColor.BLACK));
	}

	@Test
	public void testAddPieceIsImmutable() {
		Coordinate coord = getRandomCoordinate();
		IPiece piece = new PieceTestClass();
		IBoard board2 = board.addPiece(piece, coord);

		assertFalse(board.hasPiece(piece));
		assertTrue(board2.hasPiece(piece));
	}

	private <T extends Collection<?>> void assertContainsSameElements(T c1, T c2) {
		assertTrue(c1.containsAll(c2) && c1.containsAll(c2));
	}

	@Test
	public void testHasPieceMoved__ShouldReturnTrueWhenPieceHasMoved() {
		IPiece piece = new Pawn(PlayerColor.WHITE);
		board = board.addPiece(piece, new Coordinate(0, 0));
		IMove move = new Move(new Coordinate(0, 0), new Coordinate(0, 2));

		board = move.execute(board);

		assertTrue(board.hasPieceMoved(piece));
	}

	@Test
	public void testHasPieceMoved__ShouldReturnFalseWhenPieceHasNotMoved() {
		IPiece piece = new Pawn(PlayerColor.WHITE);
		IPiece piece2 = new Pawn(PlayerColor.WHITE);
		board = board.addPiece(piece, new Coordinate(0, 0));
		board = board.addPiece(piece2, new Coordinate(3, 0));
		IMove move = new Move(new Coordinate(0, 0), new Coordinate(0, 2));

		board = move.execute(board);

		assertFalse(board.hasPieceMoved(piece2));
	}
}
