package no.uib.inf112.group4.framework;

import no.uib.inf112.group4.interfaces.IGame;

public class Clock {

	private final long start;
	private long turnStartTime;

	private boolean gameIsBlitz = false;
	private final IGame game;
	private final int[] playerTime;
	private PlayerColor currentPlayer;

	public Clock(IGame game, boolean isBlitz) {
		this.game = game;
		start = System.currentTimeMillis();
		playerTime = new int[2];
		gameIsBlitz = isBlitz;
	}

	// Returns the time

	/*
	 * Returns seconds
	 */
	public long getElapsedSeconds() {
		long now = System.currentTimeMillis();
		return (int) ((now - start) / 1000);
	}

	/*
	 * Returns seconds for display(between 0 and 60)
	 */
	public long getElapsedSecondsForUI() {
		return getElapsedSeconds() % 60;
	}

	/*
	 * Returns Minutes
	 */
	public long getElapsedMinutes() {
		return getElapsedSeconds() / 60;
	}

	public void startTurn(PlayerColor color) {
		currentPlayer = color;
		turnStartTime = getElapsedSeconds();
	}

	public void endTurn() {
		if (currentPlayer == null) {
			return;
		}

		assert (getElapsedTurnTime() >= 0);
		playerTime[getIndex(currentPlayer)] += getElapsedTurnTime();
		currentPlayer = null;
	}

	private long getElapsedTurnTime() {
		return getElapsedSeconds() - turnStartTime;
	}

	public boolean overtime(long time) {
		// Ends game if a player goes over blitz time
		if (gameIsBlitz && time >= 15) {
			return true;
		}
		// Ends game if a player goes over fide time, before 90 minnutes
		int moveCount = game.getBoard().getPerformedMoves().length;
		if (time >= 90 && moveCount < 40) {
			return true;
			// Ends game if a player goes over fide time, after 90 minnutes
		}
		if (moveCount < 40 && time > 120 + moveCount / 2 * 0.5) {
			return true;

		}
		return false;
	}

	public void setGameMode(boolean isBlitz) {
		gameIsBlitz = isBlitz;
	}

	public int getPlayerMinutes(PlayerColor color) {
		return getTotalSeconds(color) / 60;
	}

	public int getPlayerSeconds(PlayerColor color) {
		return getTotalSeconds(color) % 60;
	}

	public int getTotalSeconds(PlayerColor color) {
		long time = playerTime[getIndex(color)];
		if (color == currentPlayer) {
			time += getElapsedTurnTime();
		}
		return (int) time;
	}

	private int getIndex(PlayerColor color) {
		return color == PlayerColor.BLACK ? 0 : 1;
	}
}
