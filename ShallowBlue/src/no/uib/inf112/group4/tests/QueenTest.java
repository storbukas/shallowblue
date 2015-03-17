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
import no.uib.inf112.group4.pieces.Queen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QueenTest {

	Queen queen;
	IBoard board;
	Coordinate queenStartingCoordinate = new Coordinate(3, 0);

	@Before
	public void setUp() {
		queen = new Queen(PlayerColor.WHITE);
		board = new Board();
	}

	@After
	public void tearDown() {
		queen = null;
		board = null;
	}

	@Test
	public void testQueenIsWhite() {
		assertEquals(PlayerColor.WHITE, queen.getColor());
	}

	@Test
	public void testAddQueen() {
		board = board.addPiece(queen, queenStartingCoordinate);
		assertTrue(board.hasPiece(queen));
		assertEquals(board.getPieceAt(queenStartingCoordinate), queen);
	}

	@Test
	public void testMoveQueenOneStepWithNoCollision() {
		List<IMove> queenMoveList = queen.getMoves(board,
				queenStartingCoordinate);
		IMove testMove = new Move(queenStartingCoordinate, new Coordinate(3, 1));
		assertTrue(queenMoveList.contains(testMove));
	}

	@Test
	public void testMoveQueenOneStepWithCollitionShouldNotWork() {
		board = board.addPiece(queen, new Coordinate(3, 1));
		List<IMove> queenMoveList = queen.getMoves(board,
				queenStartingCoordinate);
		IMove testMove = new Move(queenStartingCoordinate, new Coordinate(3, 1));
		assertFalse(queenMoveList.contains(testMove));
	}

	@Test
	public void TestMoveQueenOnEnemyPieceShouldWork() {
		board = board.addPiece(new Queen(PlayerColor.BLACK), new Coordinate(3,
				1));
		List<IMove> queenMoveList = queen.getMoves(board,
				queenStartingCoordinate);
		IMove testMove = new Move(queenStartingCoordinate, new Coordinate(3, 1));
		assertTrue(queenMoveList.contains(testMove));
	}

	@Test
	public void cantRunOverAnotherPlayer() {
		Coordinate startCoordinate = new Coordinate(0, 0);
		board = board.addPiece(queen, new Coordinate(5, 5));
		List<IMove> queenMoveList = queen.getMoves(board, new Coordinate(0, 0));
		IMove testMove = new Move(startCoordinate, new Coordinate(6, 6));
		assertFalse(queenMoveList.contains(testMove));
	}

	@Test
	public void testQueenRepresentation() {
		assertEquals("Q", queen.getRepresentation());
	}

	@Test
	public void testQueenMaterialValueEqualsToNine() {
		assertEquals(9, queen.getPieceValue());
	}
}