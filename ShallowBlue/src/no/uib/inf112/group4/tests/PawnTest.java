package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.EnPassantMove;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.pieces.Pawn;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PawnTest {

	Pawn pawn;
	IBoard board;
	Coordinate pawnStartingCoordinate = new Coordinate(0, 1);

	@Before
	public void setUp() {
		pawn = new Pawn(PlayerColor.WHITE);
		board = new Board();
	}

	@After
	public void tearDown() {
		pawn = null;
		board = null;
	}

	@Test
	public void testPawnIsWhite() {
		assertEquals(PlayerColor.WHITE, pawn.getColor());
	}

	@Test
	public void testAddPawn() {
		board = board.addPiece(pawn, pawnStartingCoordinate);
		assertTrue(board.hasPiece(pawn));
		assertEquals(board.getPieceAt(pawnStartingCoordinate), pawn);
	}

	@Test
	public void testMovePawnOneStepWithNoWithCollision() {
		board = board.addPiece(pawn, pawnStartingCoordinate);
		List<IMove> listOfPawnMoves = pawn.getMoves(board,
				pawnStartingCoordinate);
		IMove testMove = new Move(pawnStartingCoordinate, new Coordinate(0, 2));
		assertTrue(listOfPawnMoves.contains(testMove));
	}

	@Test
	public void testMovePawnOneStepWithCollitionShouldNotWork() {
		board = board.addPiece(pawn, pawnStartingCoordinate);
		board = board.addPiece(new Pawn(PlayerColor.WHITE),
				new Coordinate(0, 2));
		List<IMove> pawnMoveList = pawn.getMoves(board, pawnStartingCoordinate);
		IMove testMove = new Move(pawnStartingCoordinate, new Coordinate(0, 2));
		assertFalse(pawnMoveList.contains(testMove));
	}

	@Test
	public void TestMovePawnTwoStepsWhenInStart() {
		board = board.addPiece(pawn, pawnStartingCoordinate);
		List<IMove> pawnMoveList = pawn.getMoves(board, pawnStartingCoordinate);
		IMove testMove = new Move(pawnStartingCoordinate, new Coordinate(0, 3));
		assertTrue(pawnMoveList.contains(testMove));
	}

	@Test
	public void TestMovePawnTwoStepseWhenNotInStart() {
		Coordinate startCoordinate = new Coordinate(0, 3);
		board = board.addPiece(pawn, startCoordinate);
		List<IMove> pawnMoveList = pawn.getMoves(board, startCoordinate);
		IMove testMove = new Move(startCoordinate, new Coordinate(0, 5));
		assertFalse(pawnMoveList.contains(testMove));
	}

	@Test
	public void TestCantJumpOverPieceWhenInStart() {
		board = board.addPiece(pawn, pawnStartingCoordinate);
		board = board.addPiece(new Pawn(PlayerColor.WHITE),
				new Coordinate(0, 2));
		List<IMove> pawnMoveList = pawn.getMoves(board, pawnStartingCoordinate);
		IMove testMove = new Move(pawnStartingCoordinate, new Coordinate(0, 3));
		assertFalse(pawnMoveList.contains(testMove));
	}

	@Test
	public void TestAttackWhenNoEnemy() {
		board = board.addPiece(pawn, pawnStartingCoordinate);
		List<IMove> pawnMoveList = pawn.getMoves(board, pawnStartingCoordinate);
		IMove testMove = new Move(pawnStartingCoordinate, new Coordinate(1, 2));
		assertFalse(pawnMoveList.contains(testMove));
	}

	@Test
	public void TestWhiteCantMoveDown() {
		board = board.addPiece(pawn, new Coordinate(3, 2));
		List<IMove> pawnMoveList = pawn.getMoves(board, new Coordinate(3, 2));
		IMove testMove = new Move(new Coordinate(3, 2), new Coordinate(3, 1));
		assertFalse(pawnMoveList.contains(testMove));
	}

	@Test
	public void testPawnRepresentation() {
		assertEquals("P", pawn.getRepresentation());
	}

	@Test
	public void testPawnMaterialValueEqualsToOne() {
		assertEquals(1, pawn.getPieceValue());
	}

	@Test
	public void testEnPassantMove() {
		Coordinate enPassantStart = new Coordinate("B5");
		Coordinate pawnStart = new Coordinate("C7");
		
		Pawn blackPawn = new Pawn(PlayerColor.BLACK);
		board = board.addPiece(pawn, pawnStart);
		board = board.addPiece(blackPawn, enPassantStart);
		
		IMove move = new Move(pawnStart, new Coordinate("C5"));
		board = move.execute(board);
		
		IMove enPassantMove = new EnPassantMove(enPassantStart, new Coordinate("C6"));
		
		List<IMove> pawnMoveList = pawn.getMoves(board, enPassantStart);
		assertTrue(pawnMoveList.contains(enPassantMove));
	}
}