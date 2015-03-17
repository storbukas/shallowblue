package no.uib.inf112.group4.framework;

import java.sql.SQLException;
import java.util.Stack;

import no.uib.inf112.group4.exceptions.AbortedMoveException;
import no.uib.inf112.group4.framework.moves.PromotionMove;
import no.uib.inf112.group4.framework.players.NetworkPlayer;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IGame;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.interfaces.IPlayer;
import no.uib.inf112.group4.interfaces.IUserInterface;

public class Game implements IGame {
	// / Two elements: [0] = White player, [1] = Black player
	private final IPlayer[] players;
	IUserInterface ui;
	private IBoard board;
	private AlgebraicNotation algebraicNotation;

	private Clock clock;

	private Stack<IBoard> undoStack;
	private Stack<IBoard> redoStack;
	private boolean canAbortMove;
	private boolean abortComplete;
	private boolean draw;
	private IPlayer resignedPlayer;

	public Game(IPlayer player1, IPlayer player2, IUserInterface ui) {
		this.ui = ui;
		players = new IPlayer[2];
		algebraicNotation = new AlgebraicNotation();

		undoStack = new Stack<IBoard>();
		redoStack = new Stack<IBoard>();

		setPlayer(PlayerColor.WHITE, player1);
		setPlayer(PlayerColor.BLACK, player2);
		startClock(false);
		board = Board.createDefaultBoard();
	}

	private void setupBoard() {
		board = Board.createDefaultBoard();
	}

	@Override
	public IPlayer[] getPlayers() {
		return players;
	}

	@Override
	public Clock getClock() {
		return clock;
	}

	@Override
	public void start() {
		setupBoard();
		algebraicNotation = new AlgebraicNotation();
		undoStack = new Stack<IBoard>();
		redoStack = new Stack<IBoard>();
		draw = false;
		resignedPlayer = null;
		
		//((NetworkPlayer) players[0]).startServer();
		//((NetworkPlayer) players[1]).connectHost();

		while (!isFinished()) {
			ui.update();
			IMove move;
			PlayerMoveResponse response;

			clock.startTurn(getActivePlayerColor());
			canAbortMove = true;
			try {
				response = getValidMoveFromPlayer();
				move = response.move;
				if (getActivePlayer().getPlayerType() == PlayerType.LOCAL) {
					if (move instanceof PromotionMove) {
						PromotionMove promotionMove = (PromotionMove) move;
						promotionMove.setPromotionPiece(ui
								.promptPromotionPiece(getActivePlayerColor()));
					}
				}
			} catch (AbortedMoveException e) {
				// The move was aborted.
				// This usually happens when undo() or redo() has been called.
				clock.endTurn();

				while (!abortComplete) {
					// Wait until the other thread has completed it's work.
				}

				continue;
			}
			clock.endTurn();
			canAbortMove = false;

			IBoard oldBoard = board;

			// Execute move
			undoStack.push(board);
			redoStack.clear();
			board = move.execute(board);
			
			if (response.requestDraw) {
				BoardAnalyzer analyzer1 = oldBoard.getAnalyzer();
				BoardAnalyzer analyzer2 = board.getAnalyzer();
				if (analyzer1.canClaimDraw() || analyzer2.canClaimDraw()) {
					draw = true;
				}
			}

			// Add algebraic notation
			algebraicNotation.addNotation(move, oldBoard,
					isChecked(getInactivePlayer()),
					isCheckmate(getInactivePlayer()), isDraw());
			algebraicNotation.printLast();

			ui.moveCompleted();
			
//			network
			if(getActivePlayer().getPlayerType() == PlayerType.NETWORK) {
//				send move
				((NetworkPlayer)getActivePlayer()).net.send(move);
				move = (IMove) ((NetworkPlayer)getActivePlayer()).net.receive();
//				update board
				board = move.execute(board);
			}
		}
		ui.update();
		ui.showGameOver(getWinner());
		System.out.println("Game over!");
		
		// Update database.
		if(getActivePlayer().getPlayerType() != PlayerType.COMPUTER &&
				getInactivePlayer().getPlayerType() != PlayerType.COMPUTER) {
			
				int eloRatingPlayer1 = 1500;
				int eloRatingPlayer2 = 1500;
				
				EloDatabase db;
				
				try {
					db = EloDatabase.getInstance();
					if (db.getEloRating(players[0].getName()) != 0) {
						eloRatingPlayer1 = db.getEloRating(players[0].getName());
					}
					if (db.getEloRating(players[1].getName()) != 0) {
					eloRatingPlayer2 = db.getEloRating(players[1].getName());
					}
				
				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}
			
				int[] eloRatings = new int[2];
				if (getWinner() == null) {
					System.arraycopy(ELO.computeElo(eloRatingPlayer1, 
							eloRatingPlayer2, Winner.DRAW), 0, eloRatings, 0, 2);
					saveEloRating(db, players[0].getName(), eloRatings[0]);
					saveEloRating(db, players[1].getName(), eloRatings[1]);
				}
				else if (getWinner().getColor() == PlayerColor.WHITE) {
					System.arraycopy(ELO.computeElo(eloRatingPlayer1, 
							eloRatingPlayer2, Winner.PLAYER1), 0, eloRatings, 0, 2);
					saveEloRating(db, players[0].getName(), eloRatings[0]);
					saveEloRating(db, players[1].getName(), eloRatings[1]);
				}
				else if (getWinner().getColor() == PlayerColor.BLACK) {
					System.arraycopy(ELO.computeElo(eloRatingPlayer1, 
							eloRatingPlayer2, Winner.PLAYER2), 0, eloRatings, 0, 2);
					saveEloRating(db, players[0].getName(), eloRatings[0]);
					saveEloRating(db, players[1].getName(), eloRatings[1]);
				}
				try {
				db.closeDatabase();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Failed to close the database.");
				}
		}
	}


	public void saveEloRating(EloDatabase db, String name, int eloRating) {
		
		try {
			db.update(null, name, eloRating);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isDraw() {
		return this.draw || isStalemate();
	}

	public Stack<String> getAlgebraicNotations() {
		return algebraicNotation.getNotations();
	}

	@Override
	public IPlayer getInactivePlayer() {
		return getPlayer(board.getTurn().other());
	}

	@Override
	public void startClock(boolean isBlitz) {
		clock = new Clock(this, isBlitz);
	}

	@Override
	public void startAbortMove() {
		abortComplete = false;
		while (!canAbortMove) {
			;
		}
	}

	@Override
	public void completeAbortMove() {
		abortComplete = true;
	}

	private IPlayer getWinner() {
		IPlayer whitePlayer = getPlayer(PlayerColor.WHITE);
		IPlayer blackPlayer = getPlayer(PlayerColor.BLACK);
		
		if (resignedPlayer != null) {
			if (resignedPlayer == whitePlayer) {
				return blackPlayer;
			} else if (resignedPlayer == blackPlayer) {
				return whitePlayer;
			}
		}
		
		if (isCheckmate(blackPlayer)) {
			return whitePlayer;
		} else if (isCheckmate(whitePlayer)) {
			return blackPlayer;
		} else {
			return null;
		}
	}

	private PlayerMoveResponse getValidMoveFromPlayer() throws AbortedMoveException {
		PlayerMoveResponse response = null;
		while (true) {
			response = getMoveFromPlayer(getActivePlayer());
			IMove move = findCorrespondingMove(response.move);
			if (isValidMove(move)) {
				break;
			}

			ui.showInvalidMoveMessage(move, getValidationMessage(move));
		}

		return response;
	}

	/**
	 * The player may return a Move instance, when a castling move was intended.
	 * Therefore we check the piece, and find a matching move which is
	 * guaranteed to be the correct type.
	 */
	private IMove findCorrespondingMove(IMove move) {
		IPiece piece = board.getPieceAt(move.getLocation());
		if (piece != null) {
			for (IMove otherMove : piece.getMoves(board, move.getLocation())) {
				if (otherMove.getDestination().equals(move.getDestination())) {
					return otherMove;
				}
			}
		}

		// Invalid move!
		System.out.println("Game: Could not find corresponding move.");
		return null;
	}

	private boolean isValidMove(IMove move) {
		if (move == null) {
			System.out.println("Move was null.");
			return false;
		}

		return move.isValid(board, getActivePlayerColor());
	}

	private String getValidationMessage(IMove move) {
		if (move == null) {
			return "Move was null.";
		}

		return move.getValidationMessage();
	}

	@Override
	public PlayerColor getActivePlayerColor() {
		return getPlayerColor(getActivePlayer());
	}

	private PlayerMoveResponse getMoveFromPlayer(IPlayer player) throws AbortedMoveException {
		return player.getMove(board);
	}

	@Override
	public boolean isFinished() {
		return isCheckmate(players[0]) || isCheckmate(players[1])
				|| isDraw() || resignedPlayer != null;
	}

	@Override
	public IPlayer getActivePlayer() {
		return getPlayer(board.getTurn());
	}

	@Override
	public IBoard getBoard() {
		return board;
	}

	@Override
	public IPlayer getPlayer(PlayerColor color) {
		return players[color.equals(PlayerColor.WHITE) ? 0 : 1];
	}

	@Override
	public PlayerColor getPlayerColor(IPlayer player) {
		if (player.equals(players[0])) {
			return PlayerColor.WHITE;
		} else if (player.equals(players[1])) {
			return PlayerColor.BLACK;
		}
		throw new IllegalArgumentException(
				"Provided player is not a player for this board.");
	}

	@Override
	public void setPlayer(PlayerColor color, IPlayer player) {
		if (player == null) {
			throw new IllegalArgumentException("Player can not be null.");
		}
		checkPlayerWillNotBeAssignedToBothColors(color, player);
		players[color.equals(PlayerColor.WHITE) ? 0 : 1] = player;
	}

	private void checkPlayerWillNotBeAssignedToBothColors(PlayerColor color,
			IPlayer player) {
		if (color == PlayerColor.WHITE && player.equals(players[1])) {
			throwSamePlayerException();
		} else if (color == PlayerColor.BLACK && player.equals(players[0])) {
			throwSamePlayerException();
		}
	}

	private void throwSamePlayerException() {
		throw new IllegalArgumentException(
				"Tried to set the same player instance to both colors.");
	}

	@Override
	public void undo() {
		if (undoStack.size() <= 0) {
			return;
		}

		redoStack.push(board);
		IBoard board = undoStack.pop();
		algebraicNotation.removeNotation();
		setBoard(board);
	}

	@Override
	public void redo() {
		if (redoStack.size() <= 0) {
			return;
		}

		undoStack.push(board);
		algebraicNotation.addNotationFromRedoStack();
		IBoard board = redoStack.pop();
		setBoard(board);
	}

	@Override
	public void setBoard(IBoard board) {
		if (board == null) {
			throw new IllegalArgumentException("Board must not be null.");
		}
		this.board = board;
	}

	@Override
	public boolean isChecked(IPlayer player) {
		BoardAnalyzer analyzer = board.getAnalyzer();
		return analyzer.isChecked(getPlayerColor(player));
	}

	@Override
	public boolean isCheckmate(IPlayer player) {
		BoardAnalyzer analyzer = board.getAnalyzer();
		return analyzer.isCheckmate(getPlayerColor(player));
	}

	@Override
	public boolean isStalemate() {
		BoardAnalyzer analyzer = board.getAnalyzer();
		return analyzer.isStalemate();
	}

	@Override
	public boolean claimDraw() {
		BoardAnalyzer ba = board.getAnalyzer();
		if(ba.canClaimDraw())
			draw = true;
		else return false;
		return true;
	}

	@Override
	public boolean resign(IPlayer player) {	
		if (players[0] == player || players[1] == player) {
			resignedPlayer = player;
			return true;
		}
		return false;
	}
}
