package no.uib.inf112.group4.interfaces;

import no.uib.inf112.group4.framework.BoardEvaluator.BoardEvaluation;
import no.uib.inf112.group4.framework.PlayerColor;

public interface IBoardEvaluator {
	/**
	 * Returns value of white player - value of black, Integer.MAX_VALUE, resp.
	 * Integer.MIN_VALUE represents check mate.
	 * 
	 * TODO: What to do about stale mates?
	 * 
	 * @param board
	 * @return
	 */
	public BoardEvaluation evaluate(IBoard board);

	/**
	 * 
	 * @param board
	 * @param color
	 * @return
	 */
	public BoardEvaluation evaluate(IBoard board, PlayerColor color);
}
