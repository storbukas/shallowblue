package no.uib.inf112.group4.framework.moves;

import no.uib.inf112.group4.framework.BoardAnalyzer;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;

public abstract class AbstractMove implements IMove {
	private String validationMessage;

	@Override
	public boolean isValid(IBoard board, PlayerColor color) {
		IPiece piece = board.getPieceAt(getLocation());
		if (piece == null) {
			setValidationMessage("Tried to move a piece which does not exist.");
			return false;
		}

		if (piece.getColor() != color) {
			setValidationMessage("Tried to move opponents pieces.");
			return false;
		}

		BoardAnalyzer analyzer = board.getAnalyzer();
		if (analyzer.willBeCheckedAfterMove(color, this)) {
			setValidationMessage("The move would put you in check.");
			return false;
		}

		return true;
	}

	protected void setValidationMessage(String message) {
		validationMessage = message;
	}

	@Override
	public String getValidationMessage() {
		return validationMessage;
	}
}
