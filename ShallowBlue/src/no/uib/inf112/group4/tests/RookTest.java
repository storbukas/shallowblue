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
import no.uib.inf112.group4.pieces.Rook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RookTest {

	Rook rook;
	IBoard board;
	Coordinate rookStartingCoordinate = new Coordinate(0, 0);

	@Before
	public void setUp() {
		rook = new Rook(PlayerColor.WHITE);
		board = new Board();
	}

	@After
	public void tearDown() {
		rook = null;
		board = null;
	}

	@Test
	public void testRookIsWhite() {
		assertEquals(PlayerColor.WHITE, rook.getColor());
	}

	@Test
	public void testAddRook() {
		board = board.addPiece(rook, rookStartingCoordinate);
		assertTrue(board.hasPiece(rook));
		assertEquals(board.getPieceAt(rookStartingCoordinate), rook);
	}

	@Test
	public void testMoveRookOneStepWithNoWithCollision() {
		List<IMove> listOfRookMoves = rook.getMoves(board,
				rookStartingCoordinate);
		IMove testMove = new Move(rookStartingCoordinate, new Coordinate(0, 1));
		assertTrue(listOfRookMoves.contains(testMove));
	}

	@Test
	public void testMoveRookOneStepWithCollitionShouldNotWork() {
		board = board.addPiece(rook, new Coordinate(0, 1));
		List<IMove> rookMoveList = rook.getMoves(board, rookStartingCoordinate);
		IMove testMove = new Move(rookStartingCoordinate, new Coordinate(0, 1));
		assertFalse(rookMoveList.contains(testMove));
	}

	@Test
	public void TestMoveRookOnEnemyPieceShouldWork() {
		board = board.addPiece(new Rook(PlayerColor.BLACK),
				new Coordinate(0, 1));
		List<IMove> bishopMoveList = rook.getMoves(board,
				rookStartingCoordinate);
		IMove testMove = new Move(rookStartingCoordinate, new Coordinate(0, 1));
		assertTrue(bishopMoveList.contains(testMove));
	}

	@Test
	public void testRookRepresentation() {
		assertEquals("R", rook.getRepresentation());
	}

	@Test
	public void testRookMaterialValueEqualsToFive() {
		assertEquals(5, rook.getPieceValue());
	}
}