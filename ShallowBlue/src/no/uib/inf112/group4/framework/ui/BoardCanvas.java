package no.uib.inf112.group4.framework.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import no.uib.inf112.group4.exceptions.AbortedMoveException;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.MiniMax.State;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.PlayerMoveResponse;
import no.uib.inf112.group4.framework.players.FixedDifficultyAI;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IGame;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.interfaces.IPlayer;

/**
 * This class is responsible for drawing the visual representation of the game
 * board.
 */
public class BoardCanvas extends JPanel implements MouseListener,
		MouseMotionListener, ComponentListener {
	// Generated serial
	private static final long serialVersionUID = 962349161745375494L;

	// Pretty colors
	public static Color firstColor = new Color(150, 50, 30);
	public static Color secondColor = new Color(255, 200, 100);
	public static Color backgroundColor = new Color(120, 165, 227);
	public static Color moveFromColor = new Color(20, 20, 200, 200);
	public static Color moveToColor = new Color(200, 20, 20, 200);
	public static Color[] borderColors = new Color[] {
			new Color(0xDD, 0xDD, 0xDD), new Color(0xEE, 0xEE, 0xEE) };

	// Images of the pieces.

	// Misc internal variables
	private static final double boardSize = 8.0; // tiles in each direction
	// (type is double for
	// practical issues)
	private int edgeLength;
	private int tileLength;
	private int xOffset;
	private int yOffset;
	private final GUI gui;
	private int lastmx, lastmy;
	private Coordinate start, destination; // dragged
	boolean hasPrompt;
	private boolean labelsVisible = true;
	private boolean showAIPlan = false;

	// Determines how the board is perceived. false,false = from white's view,
	// true,true = from black's view
	private boolean inverseRank = false;
	private boolean inverseFile = false;

	private IMove move;

	private IMove hintMove;

	public BoardCanvas(GUI gui) {
		addComponentListener(this);
		this.gui = gui;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());

		if (labelsVisible)
			drawBorder((Graphics2D) g);
		// draw board
		try {
			drawTiles(g);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (showAIPlan) {
			drawAIPlan(g);
		}
		

		// mark last move
		drawLastMove(g);

		// draw markers
		drawMoves(g);
		if (hintMove != null) {
			drawHint(g, hintMove);
		}

		// draw pieces
		try {
			drawPieces(g);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void drawHint(Graphics g, IMove move) {
		//highlight piece to move;
		Rectangle tile = tileToRect(move.getLocation());
		g.setColor(BoardCanvas.moveFromColor);
		g.fillOval(tile.x + tile.width / 8, tile.y + tile.width / 8, tile.width
				- tile.width / 4, tile.height - tile.height / 4);
		
		//highlight destination to move said piece
		tile = tileToRect(move.getDestination());
		g.setColor(BoardCanvas.moveToColor);
		tile = tileToRect(move.getDestination());
		g.fillOval(tile.x + tile.width / 8, tile.y + tile.width / 8, tile.width
				- tile.width / 4, tile.height - tile.height / 4);
		try {
			this.drawPieces(g);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void drawBorder(Graphics2D g) {
		final String letters = "ABCDEFGH";
		final Font fnt = new Font("Calibri", Font.BOLD, 20);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(fnt);
		// Draw bottom letters
		for (int i = 0; i < 8; i++) {
			Rectangle r = tileToRect(i, inverseRank ? 7 : 0);
			g.setColor(borderColors[i % 2]);
			g.fillRect(r.x, r.y + r.height, r.width, 20);
			g.setColor(Color.black);
			g.drawRect(r.x, r.y + r.height, r.width, 20);
			g.drawString(letters.substring(i, i + 1), r.x + r.width / 2 - 5,
					r.y + r.height + 17);
		}
		// Draw right numbers
		for (int i = 0; i < 8; i++) {
			Rectangle r = tileToRect(inverseFile ? 0 : 7, i);
			g.setColor(borderColors[i % 2]);
			g.fillRect(r.x + r.width, r.y, 20, r.height);
			g.setColor(Color.black);
			g.drawRect(r.x + r.width, r.y, 20, r.height);
			g.drawString("" + (i + 1), r.x + r.width + 5, r.y + r.height / 2
					+ 7);
		}
	}

	private void drawAIPlan(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		for (IPlayer iplayer : gui.game.getPlayers()) {
			if (!(iplayer instanceof FixedDifficultyAI)) {
				continue;
			}
			FixedDifficultyAI player = (FixedDifficultyAI) iplayer;
			if (player.aiPlan == null)
				continue;

			int alpha = 175;
			if (player.getColor() == PlayerColor.BLACK) {
				alpha = 50;
			}
			Color colorWhiteMoveFrom = new Color(150, 150, 255, alpha);
			Color colorWhiteMoveTo = new Color(255, 150, 150, alpha);
			if (player.getColor() == PlayerColor.WHITE) {
				alpha = 50;
			}
			Color colorBlackMoveFrom = new Color(0, 0, 100, alpha);
			Color colorBlackMoveTo = new Color(100, 0, 0, alpha);

			boolean isWhite = player.getColor() == PlayerColor.WHITE;
			boolean isOpponent = false;
			g.setStroke(new BasicStroke(2));
			State aiPlan = player.aiPlan;
			while (aiPlan.move != null) {
				IMove move = aiPlan.move;

				// Mark source tile
				Rectangle tile = tileToRect(move.getLocation());
				tile.translate(tile.width * 1 / 4, tile.height * 1 / 4);
				tile.setSize(tile.width / 2, tile.height / 2);

				g.setColor(isWhite ? colorWhiteMoveFrom : colorBlackMoveFrom);
				g.fillOval(tile.x, tile.y, tile.width, tile.height);

				// Mark destination tile
				Rectangle dTile = tileToRect(move.getDestination());
				dTile.translate(dTile.width * 1 / 3, dTile.height * 1 / 3);
				dTile.setSize(dTile.width / 3, dTile.height / 3);

				g.setColor(isWhite ? colorWhiteMoveTo : colorBlackMoveTo);
				g.fillOval(dTile.x, dTile.y, dTile.width, dTile.height);

				// Draw line between source and destination
				if (isOpponent) {
					g.setColor(isWhite ? new Color(255, 100, 100, 100)
							: new Color(100, 100, 255, 100));
				} else {
					g.setColor(isWhite ? new Color(255, 100, 100, 255)
							: new Color(100, 100, 255, 255));
				}
				int o = player.getColor() == PlayerColor.WHITE ? -1 : +1;
				g.drawLine((int) tile.getCenterX() + o,
						(int) tile.getCenterY(), (int) dTile.getCenterX() + o,
						(int) dTile.getCenterY());

				aiPlan = aiPlan.childState;
				isWhite = !isWhite;
				isOpponent = !isOpponent;
			}
			g.setStroke(new BasicStroke(1));
		}
	}

	private void drawMoves(Graphics g) {
		if (start == null)
			return;
		IPiece fromPiece = gui.game.getBoard().getPieceAt(start);
		if (fromPiece == null)
			return;

		// mark from-tile
		Rectangle tile = tileToRect(start);
		g.setColor(moveFromColor);
		g.fillOval(tile.x + tile.width / 8, tile.y + tile.width / 8, tile.width
				- tile.width / 4, tile.height - tile.height / 4);

		// mark potential moves
		g.setColor(moveToColor);
		for (IMove move : fromPiece.getMoves(gui.game.getBoard(), start)) {
			if (move.isValid(gui.game.getBoard(), fromPiece.getColor()) == false)
				continue;
			tile = tileToRect(move.getDestination());
			g.fillOval(tile.x + tile.width / 8, tile.y + tile.width / 8,
					tile.width - tile.width / 4, tile.height - tile.height / 4);
		}
	}

	private void drawLastMove(Graphics g) {
		IMove lastMove = gui.game.getBoard().getPreviousMove();
		if (lastMove == null) {
			return;
		}
		Color color = new Color(184, 250, 160);
		g.setColor(color);

		// Mark move location tile.
		Rectangle tile = tileToRect(lastMove.getLocation());

		g.fillRect(tile.x + 39, tile.y + 26, 3, 24);
		g.fillRect(tile.x + 28, tile.y + 37, 24, 3);

		// Mark move destination tile.
		tile = tileToRect(lastMove.getDestination());

		// Upper corner
		g.fillRect(tile.x + 5, tile.y + 5, 3, 24);
		g.fillRect(tile.x + 5, tile.y + 5, 24, 3);

		// Lower corner
		g.fillRect(tile.x + (tile.width - 7), tile.y + (tile.height - 29), 3,
				24);
		g.fillRect(tile.x + (tile.width - 28), tile.y + (tile.height - 7), 24,
				3);

	}

	private BufferedImage getImage(IPiece piece) {
		return Icons.getImage(piece.getColor(), piece.getType());
	}

	private void drawPieces(Graphics g) throws IOException {

		if (gui.game == null)
			return;
		IBoard board = gui.game.getBoard();
		Iterable<IPiece> listOfPieces = board.getAllPieces();
		
		for (IPiece piece : listOfPieces) {
			Rectangle r;
			if (start != null && board.getPieceAt(start) == piece) {
				r = new Rectangle(lastmx - tileLength / 4, lastmy + tileLength
						/ 2, tileLength, tileLength);
			} else {
				Coordinate location = board.getCoordinate(piece);
				r = tileToRect(location.getX(), location.getY()
						+ (inverseRank ? 1 : -1));
				r.setBounds((int) r.getX() + tileLength / 4, (int) r.getY()
						- tileLength / 4, (int) r.getWidth(),
						(int) r.getHeight());
			}
			g.drawImage(getImage(piece), r.x - (int) (tileLength / 4.3), r.y
					- (int) (tileLength / 1.3), r.height, r.width, this);
		}

	}

	/**
	 * Draws a square tiled board centered in the canvas
	 * 
	 * @throws IOException
	 * 
	 */
	public void drawTiles(Graphics g) throws IOException {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (j % 2 == i % 2)
					g.setColor(firstColor);
				else
					g.setColor(secondColor);
				Rectangle r = tileToRect(j, i);
				g.fillRect(r.x, r.y, r.width, r.height);
			}
		}
	}

	/**
	 * Converts given tile (by coordinates) in board to pixel position in
	 * canvas.
	 */
	Rectangle tileToRect(int x, int y) {
		return new Rectangle(xOffset + (inverseFile ? 7 - x : x) * tileLength,
				yOffset + (inverseRank ? y : 7 - y) * tileLength, tileLength,
				tileLength);
	}

	/**
	 * Converts given tile (by coordinates) in board to pixel position in
	 * canvas.
	 */
	Rectangle tileToRect(Coordinate c) {
		return tileToRect(c.getX(), c.getY());
	}

	/**
	 * Converts a given pixel point on canvas to a tile on the board, or null if
	 * given point is out of bounds.
	 */
	Coordinate pointToCoord(int x, int y) {
		if (!containsPoint(x, y)) {
			return null;
		}
		int physicalX = (x - xOffset) / tileLength;
		int physicalY = (y - yOffset) / tileLength;
		return new Coordinate(inverseFile ? 7 - physicalX : physicalX,
				inverseRank ? physicalY : 7 - physicalY);
	}

	public boolean containsPoint(int x, int y) {
		return x >= xOffset && y >= yOffset && x <= xOffset + edgeLength
				&& y <= yOffset + edgeLength;
	}

	private void refreshInternals() {
		xOffset = Math.max(0, (getWidth() - getHeight()) / 2);
		yOffset = Math.max(0, (getHeight() - getWidth()) / 2);
		edgeLength = Math.min(getWidth(), getHeight())
				- (labelsVisible ? 30 : 0);
		tileLength = (int) Math.ceil(edgeLength / boardSize);
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// nothing to do here!
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// nothing to do here!
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		refreshInternals();
		gui.repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		refreshInternals();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!hasPrompt || start != null) {
			lastmx = e.getX();
			lastmy = e.getY();
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Not implemented
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Not implemented
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!hasPrompt)
			return;

		// if inside the board
		lastmx = e.getX();
		lastmy = e.getY();
		Coordinate coordinate = pointToCoord(e.getX(), e.getY());
		if (coordinate == null)
			return;

		IPiece piece = gui.game.getBoard().getPieceAt(coordinate);
		if (piece == null
				|| piece.getColor() != gui.game.getPlayerColor(gui.game
						.getActivePlayer())) {
			return;
		}

		start = coordinate;

		System.out.println("Selected start: " + start.getX() + " "
				+ start.getY());
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!hasPrompt || start == null)
			return;
		if (containsPoint(e.getX(), e.getY())) {
			// if inside the board
			if (e.getButton() == MouseEvent.BUTTON1) {

				destination = pointToCoord(e.getX(), e.getY());
				if (destination == null) {
					start = null;
					destination = null;
					repaint();
					return;
				}

				if (destination.getX() != start.getX()
						|| destination.getY() != start.getY()) {
					IGame test = gui.game;
					IBoard b = test.getBoard();
					System.out.println(start.toString());
					IPiece p = b.getPieceAt(start);
					if (p == null) {
						System.out.println("Start x is " + start.getX() + ","
								+ start.getY());
						System.out.println("P was null");
						return;
					}
					List<IMove> moves = p.getMoves(b, start);
					System.out.println(moves);
					for (IMove comparison : moves) {
						if (comparison.getLocation().equals(start)
								&& comparison.getDestination().equals(
										destination)) {
							move = comparison;

							System.out.println("created move");
							System.out.println(move.getLocation().getX() + " "
									+ move.getLocation().getY() + " "
									+ move.getDestination().getX() + " "
									+ move.getDestination().getY());

						}
					}
				}
			}
		}
		start = null;
		destination = null;
		repaint();
	}

	PlayerMoveResponse promptMove(IPlayer player) throws InterruptedException, AbortedMoveException {
		hasPrompt = true;
		move = null;
		while (move == null && hasPrompt) {
			Thread.sleep(10);
		}
		if(!hasPrompt) {
			// Move aborted by GUI
			throw new AbortedMoveException();
		}
		hasPrompt = false;
		return new PlayerMoveResponse(move, false);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Not implemented

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Not implemented
	}

	public void toggleLabels() {
		labelsVisible = !labelsVisible;
		refreshInternals();
		repaint();
	}

	public void togglePerspective() {
		inverseFile = !inverseFile;
		inverseRank = !inverseRank;
		repaint();
	}

	public void toggleAIPlan() {
		showAIPlan = !showAIPlan;
		repaint();
	}

	public void setHint(IMove hintMove) {
		this.hintMove = hintMove;
	}
}
