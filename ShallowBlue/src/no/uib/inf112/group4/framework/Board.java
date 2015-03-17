package no.uib.inf112.group4.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import no.uib.inf112.group4.framework.PerformedMove.ModifiedPiece;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.pieces.Bishop;
import no.uib.inf112.group4.pieces.King;
import no.uib.inf112.group4.pieces.Knight;
import no.uib.inf112.group4.pieces.Pawn;
import no.uib.inf112.group4.pieces.Piece;
import no.uib.inf112.group4.pieces.PieceType;
import no.uib.inf112.group4.pieces.Queen;
import no.uib.inf112.group4.pieces.Rook;

public class Board implements IBoard {
	final ImmutableBoardData data;
	final int dimension;
	private List<IMove> allValidMoves;
	private Map<IPiece, Coordinate> allPieces;
	private Map<PlayerColor, List<IMove>> opponentMoves = new HashMap<PlayerColor, List<IMove>>();
	private final BoardAnalyzer analyzer; 

	public Board() {
		this(8);
	}

	public Board(int dimension) {
		this(dimension, null, new IPiece[dimension][dimension], 
				new int[PlayerColor.values().length][PieceType.values().length]);
	}

	public Board(int dimension, PerformedMove[] moves, IPiece[][] pieces, int[][] captured) {
		if (dimension <= 0) {
			throw new IllegalArgumentException(
					"Dimension must be a positive integer.");
		}

		if (moves == null) {
			moves = new PerformedMove[0];
		}

		if (pieces == null || pieces.length <= 0
				|| pieces.length != pieces[0].length) {
			throw new IllegalArgumentException("Inconsistent board.");
		}

		this.dimension = pieces.length;
		this.data = new ImmutableBoardData(pieces, moves, captured);
		this.analyzer = new BoardAnalyzer(this);
	}

	@Override
	public List<IPiece> getAllPieces() {
		return new ArrayList<IPiece>(getPiecesMap().keySet());
	}
	
	@Override
	public List<IMove> getAllMovesForColor(PlayerColor color) {
		if (this.opponentMoves.get(color) == null) {
			List<IPiece> pieces = getPiecesWithColor(color);
			List<IMove> moves = new ArrayList<IMove>();
	
			// Check all moves
			for (IPiece piece : pieces) {
				moves.addAll(piece.getMoves(this, getCoordinate(piece)));
			}
	
			// Cache value to speed up minimax.
			this.opponentMoves.put(color, moves);
		}
		return new ArrayList<IMove>(this.opponentMoves.get(color));
	}

	@Override
	public List<IMove> getAllValidMoves() {
		if (this.allValidMoves == null) {
			PlayerColor color = getTurn();
			List<IMove> moves = getAllMovesForColor(color);
			Piece.removeInvalidMoves(moves, this, color);
	
			// Cache value to speed up minimax.
			this.allValidMoves = moves;
		}
		return new ArrayList<IMove>(this.allValidMoves);
	}

	@Override
	public PlayerColor getTurn() {
		// Added as group leader suggested we add one.
		PerformedMove[] moves = data.getMoves();
		if (moves.length > 0) {
			return moves[moves.length - 1].player.other();
		}
		return PlayerColor.WHITE;
	}

	@Override
	public IPiece getPieceAt(Coordinate coord) {
		return data.get(coord.getX(), coord.getY());
	}

	@Override
	public Coordinate getCoordinate(IPiece piece) {
		return getPiecesMap().get(piece);
	}

	private Map<IPiece, Coordinate> getPiecesMap() {
		
		if (this.allPieces == null) {
			Map<IPiece, Coordinate> allPieces = new IdentityHashMap<IPiece, Coordinate>(32);
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					IPiece piece = this.data.get(x, y); 
					if (piece != null) {
						Coordinate c = new Coordinate(x, y);
						allPieces.put(piece, c);
					}
				}
			}
			this.allPieces = allPieces;
		}
		return this.allPieces;
	}

	@Override
	public boolean hasPiece(IPiece piece) {
		return getCoordinate(piece) != null;
	}

	@Override
	public boolean hasPieceAt(Coordinate coord) {
		return getPieceAt(coord) != null;
	}

	@Override
	public List<IPiece> getPiecesWithColor(PlayerColor color) {
		List<IPiece> colored = new ArrayList<IPiece>();
		List<IPiece> pieces = getAllPieces();
		for (IPiece piece : pieces) {
			if (piece.getColor().equals(color)) {
				colored.add(piece);
			}
		}
		return colored;
	}

	@Override
	public List<IPiece> getWhitePieces() {
		return getPiecesWithColor(PlayerColor.WHITE);
	}

	@Override
	public List<IPiece> getBlackPieces() {
		return getPiecesWithColor(PlayerColor.BLACK);
	}

	@Override
	public IPiece[][] getData() {
		return data.getPieces();
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public IBoard removePiece(IPiece piece) {
		Coordinate c = getCoordinate(piece);
		IPiece[][] pieces = this.data.getPieces();
		pieces[c.getX()][c.getY()] = null;
		return new Board(dimension, data.getMoves(), pieces, this.data.getCaptured());
	}
	
	@Override
	public IBoard capturePiece(IPiece piece) {
		IBoard board = removePiece(piece);
		int[][] capp = board.getCaptureCount();
		capp[piece.getColor().other().ordinal()][piece.getType().ordinal()]++;
		return new Board(dimension, data.getMoves(), board.getData(), capp);
	}

	@Override
	public IBoard addPiece(IPiece piece, Coordinate coordinate) {
		if (hasPiece(piece)) {
			throw new IllegalArgumentException(
					"Tried to add the same piece twice.");
		}
		if (getPieceAt(coordinate) != null) {
			throw new IllegalArgumentException(
					"Tried to place a piece over another piece.");
		}
		IPiece[][] data = this.data.getPieces();
		data[coordinate.getX()][coordinate.getY()] = piece;
		return new Board(dimension, this.data.getMoves(), data, this.data.getCaptured());
	}

	@Override
	public IBoard movePiece(IPiece piece, Coordinate destination) {
		if (!hasPiece(piece)) {
			throw new IllegalArgumentException(
					"Tried to move a piece that is not on the board.");
		}

		IBoard board = this;
		board = board.removePiece(piece);
		board = board.addPiece(piece, destination);
		return board;
	}

	@Override
	public IBoard clear() {
		return new Board(dimension);
	}

	public static IBoard createDefaultBoard() {
		IBoard board = new Board();

		final PlayerColor white = PlayerColor.WHITE;
		final PlayerColor black = PlayerColor.BLACK;
		for (int i = 0; i < 8; i++) {
			board = board.addPiece(new Pawn(white), new Coordinate(i, 1));
			board = board.addPiece(new Pawn(black), new Coordinate(i, 6));
		}

		// Add white pieces
		board = board.addPiece(new Rook(white), new Coordinate(0, 0));
		board = board.addPiece(new Knight(white), new Coordinate(1, 0));
		board = board.addPiece(new Bishop(white), new Coordinate(2, 0));
		board = board.addPiece(new King(white), new Coordinate(4, 0));
		board = board.addPiece(new Queen(white), new Coordinate(3, 0));
		board = board.addPiece(new Bishop(white), new Coordinate(5, 0));
		board = board.addPiece(new Knight(white), new Coordinate(6, 0));
		board = board.addPiece(new Rook(white), new Coordinate(7, 0));

		// Add black pieces
		board = board.addPiece(new Rook(black), new Coordinate(0, 7));
		board = board.addPiece(new Knight(black), new Coordinate(1, 7));
		board = board.addPiece(new Bishop(black), new Coordinate(2, 7));
		board = board.addPiece(new King(black), new Coordinate(4, 7));
		board = board.addPiece(new Queen(black), new Coordinate(3, 7));
		board = board.addPiece(new Bishop(black), new Coordinate(5, 7));
		board = board.addPiece(new Knight(black), new Coordinate(6, 7));
		board = board.addPiece(new Rook(black), new Coordinate(7, 7));

		return board;
	}

	/**
	 * 
	 * @return The previous move made
	 */
	@Override
	public IMove getPreviousMove() {
		if (data.getMoves().length == 0) {
			return null;
		}
		
		return data.getMoves()[data.getMoves().length - 1].move;
	}

	@Override
	public boolean hasPieceMoved(IPiece piece) {
		for (PerformedMove move : getPerformedMoves()) {
			for (ModifiedPiece modPiece : move.modifiedPieces) {
				if (modPiece.piece == piece) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public PerformedMove[] getPerformedMoves() {
		return data.getMoves();
	}

	@Override
	public int[][] getCaptureCount() {
		return data.getCaptured();
	}

	@Override
	public BoardAnalyzer getAnalyzer() {
		return this.analyzer;
	}
}
