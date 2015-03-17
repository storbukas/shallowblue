package no.uib.inf112.group4.framework;

public enum PlayerColor {
	WHITE, BLACK;

	public PlayerColor other() {
		if (this == PlayerColor.WHITE) {
			return PlayerColor.BLACK;
		}
		return PlayerColor.WHITE;
	}
	
	public String toString() {
		switch(this) {
		case WHITE: return "White";
		case BLACK: return "Black";
		default: return "";
		}
	}
}
