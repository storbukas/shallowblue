package no.uib.inf112.group4.framework.players;

import java.util.List;
import java.util.Random;

import no.uib.inf112.group4.OpeningsBooks.OpeningBooks;
import no.uib.inf112.group4.exceptions.AbortedMoveException;
import no.uib.inf112.group4.framework.BoardAnalyzer;
import no.uib.inf112.group4.framework.BoardEvaluator;
import no.uib.inf112.group4.framework.MiniMax;
import no.uib.inf112.group4.framework.MiniMax.State;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.PlayerMoveResponse;
import no.uib.inf112.group4.framework.PlayerType;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IBoardEvaluator;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.interfaces.IPlayer;

/**
 * 
 * A Coordinate represents a placement on the chessboard
 * 
 */
public class FixedDifficultyAI implements IPlayer {
	// / VARIABLES
	private String name;
	private PlayerColor color;
	private boolean found;
	private Random rand;
	private int difficulty;
	public State aiPlan;
	private OpeningBooks openingBooks;

	public FixedDifficultyAI(String name, PlayerColor color, int difficulty) {
		this.name = name;
		this.color = color;
		this.rand = new Random();
		this.difficulty = difficulty;
		this.openingBooks = new OpeningBooks();
	}

	// / METHODS

	public PlayerMoveResponse getMove(IBoard board) throws AbortedMoveException {
		IBoardEvaluator eve = new BoardEvaluator();
		

		IMove openingMove = openingBooks.getMove(board);
		int openingValue = Integer.MIN_VALUE;
		if (openingMove != null){
			IBoard openingBoard = openingMove.execute(board);
			openingValue = Math.abs(eve.evaluate(openingBoard).value);
			openingValue += 5;
		}
		
		MiniMax.State state;
		state = MiniMax.evaluate(board, eve, difficulty);
		if (state.wasAborted) {
			throw new AbortedMoveException();
		}

		aiPlan = state;
		IMove move = state.move;
		if (move == null) {
			System.out.println("Move was drunk");
			move = getRandomMove(board);
		}
		
		System.out.println(getColor() + ": " + openingValue  + " >= " + state.value);
		if (openingValue >= Math.abs(state.value)) {
			move = openingMove;
		}
		
		boolean requestDraw = false;
		if (color == PlayerColor.BLACK) {
			BoardAnalyzer analyzer1 = board.getAnalyzer();
			BoardAnalyzer analyzer2 = move.execute(board).getAnalyzer();
			requestDraw = analyzer1.canClaimDraw() || analyzer2.canClaimDraw();
		}
		return new PlayerMoveResponse(move, requestDraw);
	}
	
	/**
	 * @param ui
	 * @param board
	 * @return
	 */
	public IMove getRandomMove(IBoard board) {
		List<IPiece> pieces = board.getPiecesWithColor(this.getColor());
		IPiece piece;
		this.found = false;

		// Look for a piece that has valid moves,
		// and pick one random out of them
		while (!this.found) {
			// Keep looking
			piece = pieces.get(this.rand.nextInt(pieces.size()));

			List<IMove> list = piece
					.getMoves(board, board.getCoordinate(piece));

			if (list.size() > 0) { // There exist valid moves
				this.found = true;
				return list.get(this.rand.nextInt(list.size()));
			}
		}
		// Never happens
		return null;
	}

	@Override
	public String getName() {
		return this.name + " [" + difficulty + "]";
	}

	@Override
	public PlayerColor getColor() {
		return this.color;
	}

	@Override
	public PlayerType getPlayerType() {
		return PlayerType.COMPUTER;
	}
}
