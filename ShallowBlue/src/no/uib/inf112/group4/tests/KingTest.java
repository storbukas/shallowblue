package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.CastlingMove;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.pieces.King;
import no.uib.inf112.group4.pieces.Pawn;
import no.uib.inf112.group4.pieces.Queen;
import no.uib.inf112.group4.pieces.Rook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KingTest {

	King king;
	IBoard board;
	final Coordinate kingStartingCoordinate = new Coordinate("E1");

	@Before
	public void setUp() {
		king = new King(PlayerColor.WHITE);
		board = new Board();
		board = board.addPiece(king, kingStartingCoordinate);
		board = board.addPiece(new King(PlayerColor.BLACK), new Coordinate("H8"));
	}

	@After
	public void tearDown() {
		king = null;
		board = null;
	}

	@Test
	public void testKingIsWhite() {
		assertEquals(PlayerColor.WHITE, king.getColor());
	}

	@Test
	public void testMoveKingOneStepWithNoWithCollision() {
		List<IMove> listOfKingMoves = king.getMoves(board,
				kingStartingCoordinate);
		IMove testMove = new Move(kingStartingCoordinate, new Coordinate("D2"));
		assertTrue(listOfKingMoves.contains(testMove));
	}

	@Test
	public void testMoveKingOneStepWithCollitionShouldNotWork() {
		board = board.addPiece(new Pawn(PlayerColor.WHITE),
				new Coordinate("D2"));
		List<IMove> kingMoveList = king.getMoves(board, kingStartingCoordinate);
		IMove testMove = new Move(kingStartingCoordinate, new Coordinate("D2"));
		assertFalse(kingMoveList.contains(testMove));
	}

	@Test
	public void TestMoveKingOnEnemyPieceShouldWork() {
		board = board.addPiece(new Queen(PlayerColor.BLACK), new Coordinate("D2"));
		List<IMove> kingMoveList = king.getMoves(board, kingStartingCoordinate);
		IMove testMove = new Move(kingStartingCoordinate, new Coordinate("D2"));
		assertTrue(kingMoveList.contains(testMove));
	}

	@Test
	public void TestMoveKingTwoStepsShouldNotWork() {
		List<IMove> kingMoveList = king.getMoves(board, kingStartingCoordinate);
		IMove testMove = new Move(kingStartingCoordinate, new Coordinate(3, 2));
		assertFalse(kingMoveList.contains(testMove));
	}

	@Test
	public void testKingRepresentation() {
		assertEquals("K", king.getRepresentation());
	}

	@Test
	public void testThatTheKingHasNoMaterialValue() {
		assertEquals(0, king.getPieceValue());
	}

	@Test
	public void testQueenSideCastling() {
		board = board.addPiece(new Rook(PlayerColor.WHITE),
				new Coordinate("A1"));
		List<IMove> kingMoveList = king.getMoves(board, kingStartingCoordinate);
		IMove kingMove = new Move(kingStartingCoordinate, new Coordinate("C1"));
		IMove rookMove = new Move(new Coordinate("A1"), new Coordinate("D1"));
		IMove castlingMove = new CastlingMove(kingMove, rookMove);
		assertTrue(kingMoveList.contains((castlingMove)));
	}

	@Test
	public void testKingSideCastling() {
		board = board.addPiece(new Rook(PlayerColor.WHITE),
				new Coordinate("H1"));
		List<IMove> kingMoveList = king.getMoves(board, kingStartingCoordinate);
		IMove kingMove = new Move(kingStartingCoordinate, new Coordinate("G1"));
		IMove rookMove = new Move(new Coordinate("H1"), new Coordinate("F1"));
		IMove castlingMove = new CastlingMove(kingMove, rookMove);
		assertTrue(kingMoveList.contains((castlingMove)));
	}
	
	@Test
	public void testPawnNearPromotionCanStopCastling() {
		board = board.addPiece(new Rook(PlayerColor.WHITE),
				new Coordinate("H1"));
		board = board.addPiece(new Pawn(PlayerColor.BLACK), new Coordinate("G2"));
		List<IMove> kingMoveList = king.getMoves(board, kingStartingCoordinate);
		IMove kingMove = new Move(kingStartingCoordinate, new Coordinate("G1"));
		IMove rookMove = new Move(new Coordinate("H1"), new Coordinate("F1"));
		IMove castlingMove = new CastlingMove(kingMove, rookMove);
		assertFalse(kingMoveList.contains((castlingMove)));
	}
}