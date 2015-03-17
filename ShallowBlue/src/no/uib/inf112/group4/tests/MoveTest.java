package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.pieces.King;

import org.junit.Before;
import org.junit.Test;

public class MoveTest {
	private Coordinate coord_1;
	private Coordinate coord_2;
	private Move move;

	@Before
	public void setUp() {
		this.coord_1 = new Coordinate(0, 1);
		this.coord_2 = new Coordinate(2, 3);
		this.move = new Move(this.coord_1, this.coord_2);
	}

	@Test
	public void testThatInitializedObjectsAreNotNull() {
		assertNotNull(this.coord_1);
		assertNotNull(this.coord_2);
		assertNotNull(this.move);
	}

	@Test
	public void testThatCreatedLocationMatchReturnedLocation() {
		assertEquals(this.coord_1, this.move.getLocation());
	}

	@Test
	public void testThatCreatedDestinationMatchReturnedDestination() {
		assertEquals(this.coord_2, this.move.getDestination());
	}

	@Test
	public void testLocationCoordinateValuesMatchInsertedValues() {
		assertEquals(0, this.move.getLocation().getX());
		assertEquals(1, this.move.getLocation().getY());
	}

	@Test
	public void testDestinationCoordinateValuesMatchInsertedValues() {
		assertEquals(2, this.move.getDestination().getX());
		assertEquals(3, this.move.getDestination().getY());
	}

	@Test
	public void testLocationCoordinateFailWhenInsertedValuesDoNotMatch() {
		assertFalse(2 == this.move.getLocation().getX());
		assertFalse(3 == this.move.getLocation().getY());
	}

	@Test
	public void testDestinationCoordinateFailWhenInsertedValuesDoNotMatch() {
		assertFalse(0 == this.move.getDestination().getX());
		assertFalse(1 == this.move.getDestination().getY());
	}

	@Test
	public void testCannotMoveKingsNextToEachOther() {
		King blackKing = new King(PlayerColor.BLACK);
		King whiteKing = new King(PlayerColor.WHITE);

		IBoard board = new Board();
		board = board.addPiece(blackKing, new Coordinate("A1"));
		board = board.addPiece(whiteKing, new Coordinate("C1"));

		IMove kingMove = new Move(new Coordinate("C1"), new Coordinate("B1"));
		assertFalse(kingMove.isValid(board, PlayerColor.WHITE));
	}
}
