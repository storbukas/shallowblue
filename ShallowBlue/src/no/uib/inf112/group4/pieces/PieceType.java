package no.uib.inf112.group4.pieces;

public enum PieceType {
	PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;
	
	String getRepresentation() {
		switch(this) {
		case PAWN: return "P";
		case KNIGHT: return "N";
		case BISHOP: return "B";
		case ROOK: return "R";
		case QUEEN: return "Q";
		case KING: return "K";
		default: return "";
		}
	}
	
	public String toString() {
		switch(this) {
		case PAWN: return "Pawn";
		case KNIGHT: return "Knight";
		case BISHOP: return "Bishop";
		case ROOK: return "Rook";
		case QUEEN: return "Queen";
		case KING: return "King";
		default: return "";
		}
	}
}
