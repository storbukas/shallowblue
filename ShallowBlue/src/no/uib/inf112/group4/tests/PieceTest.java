package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.pieces.Piece;
import no.uib.inf112.group4.pieces.PieceType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PieceTest {
	private class PieceMock extends Piece {
		private int pieceValue;

		public PieceMock(String piece, PlayerColor color, int pieceMaterialValue) {
			super(piece, color);
			pieceValue = pieceMaterialValue;
		}

		@Override
		public int getPieceValue() {
			return pieceValue;
		}

		@Override
		public List<IMove> getMoves(IBoard board, Coordinate from) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PieceType getType() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private String pieceType;
	private PlayerColor color;
	private Piece piece;
	private int pieceMaterialValue;

	@Before
	public void setUp() {
		piece = new PieceMock("Rook", PlayerColor.WHITE, 5);
	}

	@After
	public void tearDown() {
		piece = null;
		pieceType = null;
		color = null;
		pieceMaterialValue = 0;
	}

	@Test
	public void testGetColorWhiteEqualsToWhite() {
		color = PlayerColor.WHITE;
		assertEquals(color, piece.getColor());
	}

	@Test
	public void testGetColorBlackEqualsToBlack() {
		color = PlayerColor.BLACK;
		piece = new PieceMock("King", PlayerColor.BLACK, 0);
		assertEquals(color, piece.getColor());
	}

	@Test
	public void testRepresentationOfAKingEqualsToK() {
		pieceType = "K";
		piece = new PieceMock("King", PlayerColor.WHITE, 0);
		assertEquals(pieceType, piece.getRepresentation());
	}

	@Test
	public void testRookPieceValueEqualsToFive() {
		pieceMaterialValue = 5;
		assertEquals(pieceMaterialValue, piece.getPieceValue());
	}
}