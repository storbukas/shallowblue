package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertEquals;
import no.uib.inf112.group4.framework.AlgebraicNotation;
import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.CastlingMove;
import no.uib.inf112.group4.framework.moves.EnPassantMove;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.framework.moves.PromotionMove;
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

public class AlgebraicNotationTest {

	AlgebraicNotation notation;
	IBoard board;

	@Before
	public void setUp() {
		notation = new AlgebraicNotation();
		board = new Board();
		board = board.addPiece(new King(PlayerColor.BLACK), new Coordinate("H8"));
	}

	@Test
	public void testPawnMoveFromTo() {
		// Setup
		Coordinate source = new Coordinate('D', '4');
		IPiece piece = new Pawn(PlayerColor.WHITE);
		board = board.addPiece(piece, source);
		Coordinate destination = new Coordinate('D', '5');

		IMove move = new Move(source, destination);
		// Act
		notation.addNotation(move, board, false, false, false);

		// Assert
		assertEquals("d5", notation.getNotation(0));
	}

	@Test
	public void testNonePawnRegularMoveNotation() {
		Coordinate source = new Coordinate('C', '3');
		IPiece piece = new Bishop(PlayerColor.WHITE);
		board = board.addPiece(piece, source);
		Coordinate destination = new Coordinate('D', '4');

		IMove move = new Move(source, destination);

		notation.addNotation(move, board, false, false, false);

		assertEquals("Bd4", notation.getNotation(0));
	}

	@Test
	public void testPawnAttackingMoveNotation() {
		Coordinate WhiteSource = new Coordinate('C', '3');
		Coordinate BlackSource = new Coordinate('B', '4');
		IPiece pieceWhite = new Pawn(PlayerColor.WHITE);
		IPiece pieceBlack = new Pawn(PlayerColor.BLACK);
		board = board.addPiece(pieceWhite, WhiteSource);
		board = board.addPiece(pieceBlack, BlackSource);
		Coordinate destination = new Coordinate('B', '4');

		IMove move = new Move(WhiteSource, destination);

		notation.addNotation(move, board, false, false, false);

		assertEquals("cxb4", notation.getNotation(0));
	}

	@Test
	public void testNonePawnAttackingMoveNotation() {
		Coordinate WhiteSource = new Coordinate('C', '3');
		Coordinate BlackSource = new Coordinate('E', '5');
		IPiece pieceWhite = new Bishop(PlayerColor.WHITE);
		IPiece pieceBlack = new Pawn(PlayerColor.BLACK);
		board = board.addPiece(pieceWhite, WhiteSource);
		board = board.addPiece(pieceBlack, BlackSource);
		Coordinate destination = new Coordinate('E', '5');

		IMove move = new Move(WhiteSource, destination);

		notation.addNotation(move, board, false, false, false);

		assertEquals("Bxe5", notation.getNotation(0));
	}

	@Test
	public void testIdenticalPiecesCanMoveToTheSameSquare() {
		Coordinate WhiteKnight1Source = new Coordinate('G', '1');
		Coordinate WhiteKnight2Source = new Coordinate('E', '1');
		IPiece pieceWhiteKnight1 = new Knight(PlayerColor.WHITE);
		IPiece pieceWhiteKnight2 = new Knight(PlayerColor.WHITE);
		IPiece king = new King(PlayerColor.WHITE);
		board = board.addPiece(king, new Coordinate('F', '1'));
		board = board.addPiece(pieceWhiteKnight1, WhiteKnight1Source);
		board = board.addPiece(pieceWhiteKnight2, WhiteKnight2Source);
		Coordinate destination = new Coordinate('F', '3');

		IMove move = new Move(WhiteKnight1Source, destination);

		notation.addNotation(move, board, false, false, false);

		assertEquals("Ngf3", notation.getNotation(0));
	}

	@Test
	public void testIdenticalPiecesCanMoveToTheSameSquareAndCapture() {
		Coordinate whiteKnight1Source = new Coordinate('G', '1');
		Coordinate whiteKnight2Source = new Coordinate('E', '1');
		Coordinate blackPieceLocation = new Coordinate('F', '3');
		IPiece pieceWhiteKnight1 = new Knight(PlayerColor.WHITE);
		IPiece pieceWhiteKnight2 = new Knight(PlayerColor.WHITE);
		IPiece pieceBlack = new Pawn(PlayerColor.BLACK);
		IPiece king = new King(PlayerColor.WHITE);
		board = board.addPiece(king, new Coordinate('F', '1'));
		board = board.addPiece(pieceWhiteKnight1, whiteKnight1Source);
		board = board.addPiece(pieceWhiteKnight2, whiteKnight2Source);
		board = board.addPiece(pieceBlack, blackPieceLocation);
		Coordinate destination = new Coordinate('F', '3');

		IMove move = new Move(whiteKnight1Source, destination);

		notation.addNotation(move, board, false, false, false);

		assertEquals("Ngxf3", notation.getNotation(0));
	}

	@Test
	public void testIdenticalPiecesCanMoveToTheSameSquareAndCaptureAndOnSameFile() {
		Coordinate whiteKnight1Source = new Coordinate('C', '6');
		Coordinate whiteKnight2Source = new Coordinate('C', '4');
		Coordinate blackPieceLocation = new Coordinate('E', '5');
		IPiece pieceWhiteKnight1 = new Knight(PlayerColor.WHITE);
		IPiece pieceWhiteKnight2 = new Knight(PlayerColor.WHITE);
		IPiece pieceBlack = new Pawn(PlayerColor.BLACK);
		IPiece king = new King(PlayerColor.WHITE);
		board = board.addPiece(king, new Coordinate('F', '1'));
		board = board.addPiece(pieceWhiteKnight1, whiteKnight1Source);
		board = board.addPiece(pieceWhiteKnight2, whiteKnight2Source);
		board = board.addPiece(pieceBlack, blackPieceLocation);
		Coordinate destination = new Coordinate('E', '5');

		IMove move = new Move(whiteKnight1Source, destination);

		notation.addNotation(move, board, false, false, false);

		assertEquals("N6xe5", notation.getNotation(0));
	}

	@Test
	public void testIdenticalPiecesCanMoveToTheSameSquareAndOnSameFile() {
		Coordinate whiteKnight1Source = new Coordinate('C', '6');
		Coordinate whiteKnight2Source = new Coordinate('C', '4');
		IPiece pieceWhiteKnight1 = new Knight(PlayerColor.WHITE);
		IPiece pieceWhiteKnight2 = new Knight(PlayerColor.WHITE);
		IPiece king = new King(PlayerColor.WHITE);
		board = board.addPiece(king, new Coordinate('F', '1'));
		board = board.addPiece(pieceWhiteKnight1, whiteKnight1Source);
		board = board.addPiece(pieceWhiteKnight2, whiteKnight2Source);
		Coordinate destination = new Coordinate('E', '5');

		IMove move = new Move(whiteKnight1Source, destination);

		notation.addNotation(move, board, false, false, false);

		assertEquals("N6e5", notation.getNotation(0));
	}

	@Test
	public void testMovingPawnIntoCheck() {
		Coordinate pawnSource = new Coordinate('D', '6');
		IPiece pawn = new Pawn(PlayerColor.WHITE);
		board = board.addPiece(pawn, pawnSource);
		Coordinate destination = new Coordinate('D', '7');

		IMove move = new Move(pawnSource, destination);

		notation.addNotation(move, board, true, false, false);

		assertEquals("d7+", notation.getNotation(0));
	}

	@Test
	public void testMovingPawnIntoCheckDuringCapture() {
		Coordinate pawnSource = new Coordinate('D', '6');
		IPiece pawn = new Pawn(PlayerColor.WHITE);
		IPiece blackPawn = new Pawn(PlayerColor.BLACK);
		board = board.addPiece(pawn, pawnSource);
		board = board.addPiece(blackPawn, new Coordinate('D', '7'));
		Coordinate destination = new Coordinate('D', '7');

		IMove move = new Move(pawnSource, destination);

		notation.addNotation(move, board, true, false, false);

		assertEquals("dxd7+", notation.getNotation(0));
	}

	@Test
	public void testMovingPieceIntoCheck() {
		Coordinate bishopSource = new Coordinate('D', '4');
		IPiece bishop = new Bishop(PlayerColor.WHITE);
		board = board.addPiece(bishop, bishopSource);
		Coordinate destination = new Coordinate('C', '6');

		IMove move = new Move(bishopSource, destination);

		notation.addNotation(move, board, true, false, false);

		assertEquals("Bc6+", notation.getNotation(0));
	}

	@Test
	public void testMovingPieceIntoCheckAfterCapture() {
		Coordinate bishopSource = new Coordinate('D', '4');
		IPiece bishop = new Bishop(PlayerColor.WHITE);
		IPiece blackPawn = new Pawn(PlayerColor.BLACK);
		board = board.addPiece(bishop, bishopSource);
		board = board.addPiece(blackPawn, new Coordinate('C', '6'));
		Coordinate destination = new Coordinate('C', '6');

		IMove move = new Move(bishopSource, destination);

		notation.addNotation(move, board, true, false, false);

		assertEquals("Bxc6+", notation.getNotation(0));
	}

	@Test
	public void testMovingPawnIntoCheckMate() {
		Coordinate pawnSource = new Coordinate('D', '6');
		IPiece pawn = new Pawn(PlayerColor.WHITE);
		board = board.addPiece(pawn, pawnSource);
		Coordinate destination = new Coordinate('D', '7');

		IMove move = new Move(pawnSource, destination);

		notation.addNotation(move, board, false, true, false);

		assertEquals("d7#", notation.getNotation(0));
	}

	@Test
	public void testMovingPawnIntoCheckmateDuringCapture() {
		Coordinate pawnSource = new Coordinate('D', '6');
		IPiece pawn = new Pawn(PlayerColor.WHITE);
		IPiece blackPawn = new Pawn(PlayerColor.BLACK);
		board = board.addPiece(pawn, pawnSource);
		board = board.addPiece(blackPawn, new Coordinate('D', '7'));
		Coordinate destination = new Coordinate('D', '7');

		IMove move = new Move(pawnSource, destination);

		notation.addNotation(move, board, false, true, false);

		assertEquals("dxd7#", notation.getNotation(0));
	}

	@Test
	public void testMovingPieceIntoCheckmate() {
		Coordinate bishopSource = new Coordinate('D', '4');
		IPiece bishop = new Bishop(PlayerColor.WHITE);
		board = board.addPiece(bishop, bishopSource);
		Coordinate destination = new Coordinate('C', '6');

		IMove move = new Move(bishopSource, destination);

		notation.addNotation(move, board, false, true, false);

		assertEquals("Bc6#", notation.getNotation(0));
	}

	@Test
	public void testMovingPieceIntoCheckmateAfterCapture() {
		Coordinate bishopSource = new Coordinate('D', '4');
		IPiece bishop = new Bishop(PlayerColor.WHITE);
		IPiece blackPawn = new Pawn(PlayerColor.BLACK);
		board = board.addPiece(bishop, bishopSource);
		board = board.addPiece(blackPawn, new Coordinate('C', '6'));
		Coordinate destination = new Coordinate('C', '6');

		IMove move = new Move(bishopSource, destination);

		notation.addNotation(move, board, false, true, false);

		assertEquals("Bxc6#", notation.getNotation(0));
	}

	@Test
	public void testMovingPawnIntoStaleMate() {
		Coordinate pawnSource = new Coordinate('D', '6');
		IPiece pawn = new Pawn(PlayerColor.WHITE);
		board = board.addPiece(pawn, pawnSource);
		Coordinate destination = new Coordinate('D', '7');

		IMove move = new Move(pawnSource, destination);

		notation.addNotation(move, board, false, false, true);

		assertEquals("d7 (=)", notation.getNotation(0));
	}

	@Test
	public void testMovingPawnIntoStalemateDuringCapture() {
		Coordinate pawnSource = new Coordinate('D', '6');
		IPiece pawn = new Pawn(PlayerColor.WHITE);
		IPiece blackPawn = new Pawn(PlayerColor.BLACK);
		board = board.addPiece(pawn, pawnSource);
		board = board.addPiece(blackPawn, new Coordinate('D', '7'));
		Coordinate destination = new Coordinate('D', '7');

		IMove move = new Move(pawnSource, destination);

		notation.addNotation(move, board, false, false, true);

		assertEquals("dxd7 (=)", notation.getNotation(0));
	}

	@Test
	public void testMovingPieceIntoStalemate() {
		Coordinate bishopSource = new Coordinate('D', '4');
		IPiece bishop = new Bishop(PlayerColor.WHITE);
		board = board.addPiece(bishop, bishopSource);
		Coordinate destination = new Coordinate('C', '6');

		IMove move = new Move(bishopSource, destination);

		notation.addNotation(move, board, false, false, true);

		assertEquals("Bc6 (=)", notation.getNotation(0));
	}

	@Test
	public void testMovingPieceIntoStalemateAfterCapture() {
		Coordinate bishopSource = new Coordinate('D', '4');
		IPiece bishop = new Bishop(PlayerColor.WHITE);
		IPiece blackPawn = new Pawn(PlayerColor.BLACK);
		board = board.addPiece(bishop, bishopSource);
		board = board.addPiece(blackPawn, new Coordinate('C', '6'));
		Coordinate destination = new Coordinate('C', '6');

		IMove move = new Move(bishopSource, destination);

		notation.addNotation(move, board, false, false, true);

		assertEquals("Bxc6 (=)", notation.getNotation(0));
	}

	@Test
	public void testEnPassantMove() {
		Coordinate whitePawnSource = new Coordinate('E', '5');
		Coordinate blackPawnSource = new Coordinate('D', '5');
		IPiece whitePawn = new Pawn(PlayerColor.WHITE);
		IPiece blackPawn = new Pawn(PlayerColor.BLACK);
		board = board.addPiece(whitePawn, whitePawnSource);
		board = board.addPiece(blackPawn, blackPawnSource);
		Coordinate destination = new Coordinate('D', '4');

		IMove move = new EnPassantMove(whitePawnSource, destination);

		notation.addNotation(move, board, false, false, false);

		assertEquals("exd4 e.p.", notation.getNotation(0));
	}

	@Test
	public void testQueenSideCastling() {
		Coordinate kingSource = new Coordinate('E', '1');
		Coordinate rookSource = new Coordinate('A', '1');
		IPiece king = new King(PlayerColor.WHITE);
		IPiece rook = new Rook(PlayerColor.WHITE);
		board = board.addPiece(king, kingSource);
		board = board.addPiece(rook, rookSource);
		Coordinate kingDestination = new Coordinate('C', '1');
		Coordinate rookDestination = new Coordinate('D', '1');

		IMove kingMove = new Move(kingSource, kingDestination);
		IMove rookMove = new Move(rookSource, rookDestination);

		IMove castlingMove = new CastlingMove(kingMove, rookMove);

		notation.addNotation(castlingMove, board, false, false, false);

		assertEquals("0-0-0", notation.getNotation(0));
	}

	@Test
	public void testKingSideCastling() {
		Coordinate kingSource = new Coordinate('E', '1');
		Coordinate rookSource = new Coordinate('H', '1');
		IPiece king = new King(PlayerColor.WHITE);
		IPiece rook = new Rook(PlayerColor.WHITE);
		board = board.addPiece(king, kingSource);
		board = board.addPiece(rook, rookSource);
		Coordinate kingDestination = new Coordinate('G', '1');
		Coordinate rookDestination = new Coordinate('F', '1');

		IMove kingMove = new Move(kingSource, kingDestination);
		IMove rookMove = new Move(rookSource, rookDestination);

		IMove castlingMove = new CastlingMove(kingMove, rookMove);

		notation.addNotation(castlingMove, board, false, false, false);

		assertEquals("0-0", notation.getNotation(0));
	}

	@Test
	public void testPromotion() {
		Coordinate pawnSource = new Coordinate('D', '2');
		IPiece pawn = new Pawn(PlayerColor.BLACK);
		IPiece queen = new Queen(PlayerColor.BLACK);
		board = board.addPiece(pawn, pawnSource);
		Coordinate pawnDestination = new Coordinate('D', '1');

		IMove pawnMove = new Move(pawnSource, pawnDestination);
		IMove pawnPromotion = new PromotionMove(pawnMove, queen);
		board = board.addPiece(queen, pawnDestination);

		notation.addNotation(pawnPromotion, board, false, false, false);

		assertEquals("d1Q", notation.getNotation(0));
	}
}
