package no.uib.inf112.group4.framework.players;

import no.uib.inf112.group4.exceptions.AbortedMoveException;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.PlayerMoveResponse;
import no.uib.inf112.group4.framework.PlayerType;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IPlayer;
import no.uib.inf112.group4.interfaces.IUserInterface;

public class HumanPlayer implements IPlayer {
	IUserInterface ui;
	private PlayerColor color;
	private String name; 
	
	public HumanPlayer(IUserInterface ui, String name, PlayerColor color) {
		this.ui = ui;
		this.color = color;
		this.name = name;
	}

	@Override
	public PlayerMoveResponse getMove(IBoard board) throws AbortedMoveException {
		return ui.promptMove(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public PlayerType getPlayerType() {
		return PlayerType.LOCAL;
	}

	@Override
	public PlayerColor getColor() {
		return color;
	}

}
