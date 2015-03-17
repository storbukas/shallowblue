package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.pieces.Knight;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KnightTest {

	Knight knight;
	IBoard board;
	Coordinate knightStartingCoordinate = new Coordinate(2, 1);

	@Before
	public void setUp() {
		knight = new Knight(PlayerColor.WHITE);
		board = new Board();
	}

	@After
	public void tearDown() {
		knight = null;
		board = null;
	}

	@Test
	public void testKnightIsWhite() {
		assertEquals(PlayerColor.WHITE, knight.getColor());
	}

	@Test
	public void testAddKnight() {
		board = board.addPiece(knight, knightStartingCoordinate);
		assertTrue(board.hasPiece(knight));
		assertEquals(board.getPieceAt(knightStartingCoordinate), knight);
	}

	@Test
	public void TestMoveKnightOnEnemyPieceShouldWork() {
		board.addPiece(new Knight(PlayerColor.BLACK), new Coordinate(3, 3));
		List<IMove> knightMoveList = knight.getMoves(board,
				knightStartingCoordinate);
		IMove testMove = new Move(knightStartingCoordinate,
				new Coordinate(3, 3));
		assertTrue(knightMoveList.contains(testMove));
	}

	@Test
	public void TestMoveKnightOnFriendlyPieceShouldNotWork() {
		board.addPiece(new Knight(PlayerColor.WHITE), new Coordinate(2, 5));
		List<IMove> knightMoveList = knight.getMoves(board,
				knightStartingCoordinate);
		IMove testMove = new Move(knightStartingCoordinate,
				new Coordinate(2, 5));
		assertFalse(knightMoveList.contains(testMove));
	}

	@Test
	public void testKnigtRepresentation() {
		assertEquals("N", knight.getRepresentation());
	}

	@Test
	public void testKnightMaterialValueEqualsToThree() {
		assertEquals(3, knight.getPieceValue());
	}
}