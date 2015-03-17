package no.uib.inf112.group4.framework.players;

import no.uib.inf112.group4.exceptions.AbortedMoveException;
import no.uib.inf112.group4.framework.BoardAnalyzer;
import no.uib.inf112.group4.framework.BoardEvaluator;
import no.uib.inf112.group4.framework.MiniMax;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.PlayerMoveResponse;
import no.uib.inf112.group4.framework.PlayerType;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IBoardEvaluator;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPlayer;

public class TimeBasedAI implements IPlayer {
	private float maxSeconds;
	private PlayerColor color;
	private String name;
	
	private class TimedThread extends Thread {
		public MiniMax.State state;
		private IBoard board;
		private int difficulty;
		
		public TimedThread(IBoard board, int difficulty) {
			super("TimeBasedAI");
			this.board = board;
			this.difficulty = difficulty;
		}
		
		@Override
		public void run() {
			IBoardEvaluator eve = new BoardEvaluator();
			state = MiniMax.evaluate(board, eve, difficulty);
		}
	}

	public TimeBasedAI(String name, PlayerColor color, float maxSeconds) {
		this.name = name;
		this.color = color;
		this.maxSeconds = maxSeconds;
	}

	@Override
	public PlayerMoveResponse getMove(IBoard board) throws AbortedMoveException {
		long stopTime = System.currentTimeMillis() + (int)(this.maxSeconds * 1000);

		// Guarantee at least one move
		MiniMax.State state;
		state = MiniMax.evaluate(board, new BoardEvaluator(), 2);
		if (state.wasAborted) {
			throw new AbortedMoveException();
		}
		
		// Find better moves while we still have time
		for (int i = 3; i <= 20; i++) {
			System.out.println(getName() + ": Starting difficulty " + i);
			
			long threadStartTime = System.currentTimeMillis();
			MiniMax.State result = createAndExecuteThread(board, stopTime, i);
			if (result == null) {
				break;
			}
			state = result;
			
			// The next level will take longer than this one, so we can 
			// stop early if we see that we won't have enough time.
			long lastThreadTime = System.currentTimeMillis() - threadStartTime;
			long remainingTime = stopTime - System.currentTimeMillis();
			if (lastThreadTime > remainingTime) {
				// Not enough time. Stop processing.
				break;
			}
		}
		
		IMove move = state.move;
		boolean requestDraw = false;
		if (color == PlayerColor.BLACK) {
			BoardAnalyzer analyzer1 = board.getAnalyzer();
			BoardAnalyzer analyzer2 = move.execute(board).getAnalyzer();
			requestDraw = analyzer1.canClaimDraw() || analyzer2.canClaimDraw();
		}
		return new PlayerMoveResponse(move, requestDraw);
	}

	private MiniMax.State createAndExecuteThread(IBoard board, long stopTime, int difficulty)
			throws AbortedMoveException {
		TimedThread t = new TimedThread(board, difficulty);
		t.start();
		
		// Wait for thread to finish
		while (t.isAlive()) {
			if (System.currentTimeMillis() > stopTime) {
				System.out.println("Difficulty " + difficulty + " timed out.");
				t.interrupt();
				break;
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				t.interrupt();
				throw new AbortedMoveException();
			}
		}
		
		return t.state;
	}

	@Override
	public String getName() {
		return name + " [" + maxSeconds + "s]";
	}

	@Override
	public PlayerType getPlayerType() {
		return PlayerType.COMPUTER; 
	}

	@Override
	public PlayerColor getColor() {
		return color;
	}
}
