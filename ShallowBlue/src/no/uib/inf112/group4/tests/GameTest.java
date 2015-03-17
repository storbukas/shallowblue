package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.Game;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IGame;
import no.uib.inf112.group4.interfaces.IPlayer;
import no.uib.inf112.group4.interfaces.IUserInterface;
import no.uib.inf112.group4.pieces.King;
import no.uib.inf112.group4.pieces.Pawn;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

	private IGame game;
	private IUserInterface ui;
	Random random = new Random();

	@Before
	public void setUp() throws Exception {
		ui = createUserInterface();
		game = new Game(createRandomPlayer(), createRandomPlayer(), ui);
	}

	private IUserInterface createUserInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Test
	public void setPlayerThrowsExceptionWhenPlayerIsNull() {
		try {
			game.setPlayer(PlayerColor.BLACK, null);
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("Expected an exception.");
	}

	@Test
	public void setSamePlayerToBothColorsShouldThrowException() {
		IPlayer player = createRandomPlayer();
		game.setPlayer(PlayerColor.WHITE, player);
		try {
			game.setPlayer(PlayerColor.BLACK, player);
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("Expected an exception.");
	}

	@Test
	public void getPlayerReturnsTheCorrectColour() {
		IPlayer player = createRandomPlayer();
		game.setPlayer(PlayerColor.WHITE, player);

		assertEquals(player, game.getPlayer(PlayerColor.WHITE));
	}

	@Test
	public void getPlayersReturnsWhiteAndBlackPlayersInOrder() {
		IPlayer players[] = game.getPlayers();

		assertEquals(players[0], game.getPlayer(PlayerColor.WHITE));
		assertEquals(players[1], game.getPlayer(PlayerColor.BLACK));
	}

	@Test
	public void testWhitePlayerShouldStart() {
		IPlayer activePlayer = game.getActivePlayer();
		assertSame(PlayerColor.WHITE, game.getPlayerColor(activePlayer));
	}

	@Test
	public void testPlayerIsAssignedTheCorrectColor() {
		PlayerColor color = getRandomColor();
		IPlayer player = createRandomPlayer();

		game.setPlayer(color, player);

		assertEquals(color, game.getPlayerColor(player));
	}

	private PlayerColor getRandomColor() {
		if (random.nextInt(2) == 0) {
			return PlayerColor.WHITE;
		} else {
			return PlayerColor.BLACK;
		}
	}

	private IPlayer createRandomPlayer() {
		return new Mocks.PlayerTestClass();
	}

	@Test
	public void testIsCheckedWhenPieceThreatensKing() {
		IBoard board = game.getBoard();
		board = board.clear();
		board = board.addPiece(new King(PlayerColor.BLACK),
				new Coordinate("H8"));
		board = board.addPiece(new King(PlayerColor.WHITE),
				new Coordinate("A1"));
		board = board.addPiece(new Pawn(PlayerColor.BLACK),
				new Coordinate("B2"));
		game.setBoard(board);

		IPlayer player = game.getPlayer(PlayerColor.WHITE);
		assertTrue(game.isChecked(player));
	}

	@Test
	public void testIsNotCheckedWhenNoPieceThreatensKing() {
		IBoard board = game.getBoard();
		board = board.clear();
		board = board.addPiece(new King(PlayerColor.BLACK),
				new Coordinate("H8"));
		board = board.addPiece(new King(PlayerColor.WHITE),
				new Coordinate(0, 0));
		board = board.addPiece(new Pawn(PlayerColor.BLACK),
				new Coordinate(1, 2));
		game.setBoard(board);

		IPlayer player = game.getPlayer(PlayerColor.WHITE);
		assertFalse(game.isChecked(player));
	}
}
