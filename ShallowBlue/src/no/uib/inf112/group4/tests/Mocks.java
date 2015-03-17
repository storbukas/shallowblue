package no.uib.inf112.group4.tests;

import java.util.List;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.PlayerMoveResponse;
import no.uib.inf112.group4.framework.PlayerType;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.interfaces.IPlayer;
import no.uib.inf112.group4.pieces.PieceType;

/**
 * This class contains nested classes for use in unit tests.
 * 
 * They have a minimal amount of methods implemented.
 */
public class Mocks {

	static public class PieceTestClass implements IPiece {
		private PlayerColor color;

		public PieceTestClass() {
			this.color = PlayerColor.WHITE;
		}

		public PieceTestClass(PlayerColor color) {
			this.color = color;
		}

		@Override
		public PlayerColor getColor() {
			return color;
		}

		@Override
		public List<IMove> getMoves(IBoard board, Coordinate location) {
			return null;
		}

		@Override
		public String getRepresentation() {
			return "R";
		}

		@Override
		public int getPieceValue() {
			return 5;
		}

		@Override
		public PieceType getType() {
			return PieceType.ROOK;
		}
	}

	static public class PlayerTestClass implements IPlayer {
		@Override
		public String getName() {
			return "A player implementation used for testing.";
		}

		@Override
		public PlayerMoveResponse getMove(IBoard board) {
			// Not implemented
			return null;
		}

		@Override
		public PlayerType getPlayerType() {
			// Not implemented
			return null;
		}

		@Override
		public PlayerColor getColor() {
			// Not implemented
			return null;
		}
	}

}
