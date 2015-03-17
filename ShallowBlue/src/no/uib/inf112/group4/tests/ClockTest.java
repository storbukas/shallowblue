package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import no.uib.inf112.group4.framework.Clock;
import no.uib.inf112.group4.framework.Game;
import no.uib.inf112.group4.interfaces.IGame;
import no.uib.inf112.group4.interfaces.IPlayer;
import no.uib.inf112.group4.interfaces.IUserInterface;

import org.junit.Before;
import org.junit.Test;

public class ClockTest {
	private IGame game;

	@Before
	public void setUp() throws Exception {
		IUserInterface ui = null;
		game = new Game(createRandomPlayer(), createRandomPlayer(), ui);
	}

	@Test
	public void testBlitzRulesUnder15() {
		clock().setGameMode(true);
		assertFalse(clock().overtime(14));
	}

	@Test
	public void testBlitzRulesOver15() {
		clock().setGameMode(true);
		assertTrue(clock().overtime(16));
	}

	@Test
	public void testFIDERulesOver15() {
		clock().setGameMode(false);
		assertFalse(clock().overtime(16));
	}

	@Test
	public void testFIDERulesUnder90() {
		clock().setGameMode(false);
		assertFalse(clock().overtime(89));
	}

	@Test
	public void testFIDERulesOver90() {
		clock().setGameMode(false);

		assertTrue(clock().overtime(91));
	}

	private Clock clock() {
		return game.getClock();
	}

	private IPlayer createRandomPlayer() {
		return new Mocks.PlayerTestClass();
	}
}
