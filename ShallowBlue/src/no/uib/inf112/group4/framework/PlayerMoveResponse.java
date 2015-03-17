package no.uib.inf112.group4.framework;

import no.uib.inf112.group4.interfaces.IMove;

/**
 * Used by the Player classes to tell Game what move they want to make.
 */
public class PlayerMoveResponse {
	/**
	 * The move that should be executed by Game.
	 */
	public final IMove move;
	
	/**
	 * True if the player wants to claim a draw. (From fifty moves rule or
	 * threefold repetition).
	 * 
	 * If the player can not claim a draw according to the rules,
	 * game should ask the other player if they want to draw.
	 */
	public final boolean requestDraw;
	
	public PlayerMoveResponse(IMove move) {
		this(move, false);
	}
	
	public PlayerMoveResponse(IMove move, boolean requestDraw) {
		this.move = move;
		this.requestDraw = requestDraw;
	}
}
