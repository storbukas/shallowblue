package no.uib.inf112.group4.framework;

import no.uib.inf112.group4.interfaces.IPiece;

public class ImmutableBoardData {
	private final IPiece[][] pieces;
	private final PerformedMove[] moves;
	private final int[][] captured;
	
	public ImmutableBoardData(IPiece[][] pieces, PerformedMove[] moves, int[][] captured) {
		this.pieces = pieces;
		this.moves = moves;
		this.captured = captured;
	}

	public IPiece get(int x, int y) {
		return pieces[x][y];
	}

	public IPiece[][] getPieces() {
		return clone2D(pieces);
	}

	public PerformedMove[] getMoves() {
		return moves.clone();
	}
	
	public int[][] getCaptured() {
		int[][] r = new int[captured.length][];
		for(int i = 0; i < r.length; i++)
			r[i] = captured[i].clone();
		return r;
	}

	/**
	 * Clone a two-dimensional array.
	 */
	public static IPiece[][] clone2D(IPiece[][] data) {
		IPiece[][] clone = new IPiece[data.length][];
		for (int i = 0; i < data.length; i++) {
			clone[i] = data[i].clone();
		}
		return clone;
	}
}
