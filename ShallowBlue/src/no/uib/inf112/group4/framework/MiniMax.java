package no.uib.inf112.group4.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import no.uib.inf112.group4.framework.BoardEvaluator.BoardEvaluation;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IBoardEvaluator;
import no.uib.inf112.group4.interfaces.IMove;

public class MiniMax {
	public static final int MAX_THREADS;
	
	private final int difficulty;
	private AtomicInteger activeThreads = new AtomicInteger(0);
	private int globalAlpha;
	private int globalBeta;
	private boolean aborted = false;
	
	static {
		MAX_THREADS = Runtime.getRuntime().availableProcessors();
		System.out.println("Max threads: " + MAX_THREADS);
	}
	
	public MiniMax(int difficulty) {
		this.difficulty = difficulty;
		
	}
	
	public boolean wasAborted() {
		return aborted;
	}

	/**
	 * @param board
	 * @param evaluator
	 * @param depth
	 * @return
	 */
	public static State evaluate(IBoard board,
			IBoardEvaluator evaluator, int depth) {
		MiniMax mm = new MiniMax(depth);
		return mm.threadedMinimax(board, evaluator, depth);
	}

	/**
	 * Returns an optimal move, together with its optimal minimax value
	 * 
	 * @param board
	 * @param player
	 * @param evaluator
	 * @param depth
	 * @return
	 */
	State minimax(IBoard board, IBoardEvaluator evaluator,
			int depth, int alpha, int beta) {
		if (Thread.interrupted()) {
			aborted = true;
		}
		
		// Determine if we should minimize or maximize
		// TODO: Write a comment on why this is correct.
		PlayerColor color = board.getTurn();
		boolean maximizingPlayer = (color == PlayerColor.WHITE);

		// Return if this is a terminal node (depth == 0 || game over)
		BoardEvaluation eval = evaluator.evaluate(board);
		int boardValue = getBoardValue(depth, eval);
		if (aborted || eval.checkmate || eval.draw || depth <= 0) {
			return new State(boardValue, wasAborted());
		}
		
		State bestState = null;
		IMove bestMove = null;
		int bestValue = (maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE);
		List<IMove> allMoves = board.getAllValidMoves();
		Collections.shuffle(allMoves);
		for (IMove move : allMoves) {	
			// Run minimax
			IBoard nextBoard = move.execute(board);
			State s = minimax(nextBoard, evaluator, depth - 1, alpha, beta);

			if (maximizingPlayer) {
				if (s.value > bestValue) {
					bestState = s;
					bestMove = move;
					bestValue = s.value;
					alpha = Math.max(bestValue, alpha);
				}
			} else {
				if (s.value < bestValue) {
					bestState = s;
					bestMove = move;
					bestValue = s.value;
					beta = Math.min(bestValue, beta);
				}
			}
			
			if (beta <= alpha) {
				break;
			}
			
			if (aborted) {
				break;
			}
		}

		return new State(bestState, bestMove, bestValue, boardValue, wasAborted());
	}

	private int getBoardValue(int depth, BoardEvaluation eval) {
		// Value our opponent's move as much as our own.
		int exponent;
		if (difficulty % 2 == 1) {
			exponent = (depth + 1) / 2;
		} else {
			exponent = depth / 2;
		}
		
		int boardValue = (int)Math.pow(2, exponent) * eval.value;
		return boardValue;
	}

	private State threadedMinimax(IBoard board,
			IBoardEvaluator evaluator, int depth) {	
		if (activeThreads.get() > 0) {
			throw new RuntimeException("Tried to run two threaded simulations at once.");
		}
		
		globalAlpha = Integer.MIN_VALUE;
		globalBeta = Integer.MAX_VALUE;
		
		// Determine if we should minimize or maximize
		PlayerColor color = board.getTurn();
		final boolean maximizingPlayer = (color == PlayerColor.WHITE);

		// Return if this is a terminal node (depth == 0 || game over)
		BoardEvaluation eval = evaluator.evaluate(board); 
		int boardValue = getBoardValue(depth, eval);
		if (eval.checkmate || eval.draw || depth <= 0) {
			return new State(boardValue, wasAborted());
		}

		
		State bestState = null;
		IMove bestMove = null;
		int bestValue = (maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE);
		List<IMove> allMoves = board.getAllValidMoves();
		Collections.shuffle(allMoves);
		
		activeThreads.set(0);
		MinimaxThread.IListener listener = new MinimaxThread.IListener() {
			@Override
			public synchronized void threadCompleted(MinimaxThread thread) {
				if (thread.state != null) {
					if (maximizingPlayer) {
						globalAlpha = Math.max(globalAlpha, thread.getMMState().value);
					} else {
						globalBeta = Math.min(globalBeta, thread.getMMState().value);
					}
				}
				activeThreads.decrementAndGet();
			}
		};
		
		List<MinimaxThread> threads = new ArrayList<MinimaxThread>();
		
		assignThreadsLoop:
		for (IMove move : allMoves) {
			IBoard nextBoard = move.execute(board);
			MinimaxThread thread = new MinimaxThread(nextBoard, evaluator, this, depth-1, move, listener, globalAlpha, globalBeta);
			threads.add(thread);
			activeThreads.incrementAndGet();
			thread.start();
			
			while (activeThreads.get() >= MAX_THREADS) {
				try {
					Thread.sleep(0, 10);
				} catch (InterruptedException e) {
					interruptThreads(threads);
					break assignThreadsLoop;
				}
			}
		}
		
		// Wait until all threads are done
		while (activeThreads.get() > 0) {
			try {
				Thread.sleep(0, 10);
			} catch (InterruptedException e) {
				interruptThreads(threads);
			}
		}

		// Evaluate states
		for (MinimaxThread thread : threads) {
			State s = thread.getMMState();
			if (maximizingPlayer) {
				if (s.value > bestValue) {
					bestState = s;
					bestMove = thread.getMove();
					bestValue = s.value;
				}
			} else {
				if (s.value < bestValue) {
					bestState = s;
					bestMove = thread.getMove();
					bestValue = s.value;
				}
			}
		}

		return new State(bestState, bestMove, bestValue, eval.value, wasAborted());
	}
	
	private void interruptThreads(List<MinimaxThread> threads) {
		for (Thread thread : threads) {
			thread.interrupt();
		}
	}

	public class State {
		public final IMove move;
		public final int value;
		public final int currentValue;
		public final State childState;
		public final boolean wasAborted;

		public State(int value, boolean aborted) {
			this.value = value;
			this.currentValue = value;
			this.move = null;
			this.childState = null;
			this.wasAborted = aborted;
		}
		
		public State(State childState, IMove move, int value, int currentValue, boolean aborted) {
			this.childState = childState;
			this.move = move;
			this.value = value;
			this.currentValue = currentValue;
			this.wasAborted = aborted;
		}
	}

}
