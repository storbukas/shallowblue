package no.uib.inf112.group4.framework;

import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IBoardEvaluator;
import no.uib.inf112.group4.interfaces.IMove;

public class MinimaxThread extends Thread {
	public interface IListener {
		void threadCompleted(MinimaxThread thread);
	}
	
	MiniMax.State state;
	private int depth;
	private IBoardEvaluator evaluator;
	private IBoard board;
	private IMove move;
	private IListener listener;
	public int alpha;
	public int beta;
	private MiniMax minimax;
	
	public MinimaxThread(IBoard board, IBoardEvaluator evaluator, MiniMax minimax,
			int depth, IMove move, IListener listener, int alpha, int beta) {
		super("MiniMaxThread");
		this.board = board;
		this.evaluator = evaluator;
		this.minimax = minimax;
		this.depth = depth;
		this.move = move;
		this.listener = listener;
		this.alpha = alpha;
		this.beta = beta;
	}
	
	@Override
	public void run() {
		state = minimax.minimax(board, evaluator, depth, alpha, beta);

		if (listener != null) {
			listener.threadCompleted(this);
		}
	}
	
	public MiniMax.State getMMState() {
		return state;
	}

	public IMove getMove() {
		return move;
	}
}
