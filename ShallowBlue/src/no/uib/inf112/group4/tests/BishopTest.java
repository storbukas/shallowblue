package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.pieces.Bishop;
import no.uib.inf112.group4.pieces.Piece;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BishopTest {

	Piece bishop;
	IBoard board;
	Coordinate bishopStartingCoordinate = new Coordinate(2, 0);

	@Before
	public void setUp() {
		bishop = new Bishop(PlayerColor.WHITE);
		board = new Board();
	}

	@After
	public void tearDown() {
		bishop = null;
		board = null;
	}

	@Test
	public void testBipshopIsWhite() {
		assertEquals(PlayerColor.WHITE, bishop.getColor());
	}

	@Test
	public void testAddBishop() {
		board = board.addPiece(bishop, bishopStartingCoordinate);
		assertTrue(board.hasPiece(bishop));
		assertEquals(board.getPieceAt(bishopStartingCoordinate), bishop);
	}

	@Test
	public void testMoveBishopOneStepWithNoCollision() {
		List<IMove> bishopMoveList = bishop.getMoves(board,
				bishopStartingCoordinate);
		IMove testMove = new Move(bishopStartingCoordinate,
				new Coordinate(3, 1));
		assertTrue(bishopMoveList.contains(testMove));
	}

	@Test
	public void testMoveBishopOneStepWithCollitionShouldNotWork() {
		board = board.addPiece(bishop, new Coordinate(3, 1));
		List<IMove> bishopMoveList = bishop.getMoves(board,
				bishopStartingCoordinate);
		IMove testMove = new Move(bishopStartingCoordinate,
				new Coordinate(3, 1));
		assertFalse(bishopMoveList.contains(testMove));
	}

	@Test
	public void TestMoveBishopOnEnemyPieceShouldWork() {
		board = board.addPiece(new Bishop(PlayerColor.BLACK), new Coordinate(3,
				1));
		List<IMove> bishopMoveList = bishop.getMoves(board,
				bishopStartingCoordinate);
		IMove testMove = new Move(bishopStartingCoordinate,
				new Coordinate(3, 1));
		assertTrue(bishopMoveList.contains(testMove));
	}

	@Test
	public void cantGoFromBlackToWhiteSquare() {
		List<IMove> bishopMoveList = bishop.getMoves(board,
				bishopStartingCoordinate);
		IMove testMove = new Move(bishopStartingCoordinate,
				new Coordinate(5, 2));
		assertFalse(bishopMoveList.contains(testMove));
	}

	@Test
	public void cantGoFromBlackToWhiteSquareWhenOnStartingPos() {
		List<IMove> bishopMoveList = bishop.getMoves(board,
				bishopStartingCoordinate);
		IMove testMove = new Move(bishopStartingCoordinate,
				new Coordinate(3, 0));
		assertFalse(bishopMoveList.contains(testMove));
	}

	@Test
	public void canRunAcrossTheDiagonalOnEmptyBoard() {
		Coordinate startCoordinate = new Coordinate(0, 0);
		List<IMove> bishopMoveList = bishop.getMoves(board,
				new Coordinate(0, 0));
		List<IMove> allMovesOnDiagonal = new ArrayList<IMove>();
		for (int i = 1; i < board.getDimension(); i++) {
			allMovesOnDiagonal.add(new Move(startCoordinate, new Coordinate(i,
					i)));
		}
		assertEquals(bishopMoveList, allMovesOnDiagonal);
	}

	@Test
	public void cantRunOverAnotherPlayer() {
		Coordinate startCoordinate = new Coordinate(0, 0);
		board = board.addPiece(bishop, new Coordinate(5, 5));
		List<IMove> bishopMoveList = bishop.getMoves(board,
				new Coordinate(0, 0));
		IMove testMove = new Move(startCoordinate, new Coordinate(6, 6));
		assertFalse(bishopMoveList.contains(testMove));
	}

	@Test
	public void testBishopRepresentation() {
		assertEquals("B", bishop.getRepresentation());
	}

	@Test
	public void testBishopMaterialValueEqualsFour() {
		assertEquals(4, bishop.getPieceValue());
	}
}