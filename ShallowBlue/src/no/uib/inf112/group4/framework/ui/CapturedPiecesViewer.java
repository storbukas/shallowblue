package no.uib.inf112.group4.framework.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.pieces.PieceType;

/**
 * A GUI component for displaying captured pieces of a player
 */
public class CapturedPiecesViewer extends JPanel {
	private static final long serialVersionUID = 6772100688125206701L;
	private GUI gui;
	private PlayerColor color;

	/**
	 * Creates a CapturedPiecesViewer that displays the pieces that the player
	 * with given color has captured.
	 */
	CapturedPiecesViewer(GUI gui, PlayerColor color) {
		this.gui = gui;
		this.color = color;
		setPreferredSize(new Dimension(100, 150));
	}

	private void drawPiece(Graphics2D g, PieceType pt, int x, int y, int count) {
		// Draw piece's image
		if(count == 0) {
			g.setComposite(AlphaComposite.getInstance(
		            AlphaComposite.SRC_OVER, (float) 0.5));
		}
		g.drawImage(Icons.getImage(color.other(),  pt), x, y, 49, 49, this);
		g.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, (float) 1));
		// Draw counter
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		g.setColor(color==PlayerColor.WHITE?Color.BLACK:Color.BLACK);
		g.drawString(Integer.toString(count), x+5, y + 40);
		g.drawString(Integer.toString(count), x+7, y + 40);
		g.drawString(Integer.toString(count), x+5, y + 42);
		g.drawString(Integer.toString(count), x+7, y + 42);
		g.setColor(color==PlayerColor.WHITE?Color.WHITE:Color.WHITE);
		g.drawString(Integer.toString(count), x+6, y + 41);
	}

	public void paint(Graphics g) {
		// Draw panel
		g.setColor(color == PlayerColor.BLACK ? Color.DARK_GRAY
				: Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth(), getHeight());
		int[] map = gui.game.getBoard().getCaptureCount()[color.ordinal()];
		int xpos = color == PlayerColor.WHITE?3:48;
		drawPiece((Graphics2D)g, PieceType.QUEEN, xpos, 25, map[PieceType.QUEEN.ordinal()]);
		drawPiece((Graphics2D) g, PieceType.KNIGHT, xpos, 75, map[PieceType.KNIGHT.ordinal()]);
		xpos = color == PlayerColor.WHITE?53:-2;
		drawPiece((Graphics2D) g, PieceType.PAWN, xpos, 0, map[PieceType.PAWN.ordinal()]);
		drawPiece((Graphics2D) g, PieceType.BISHOP, xpos, 50, map[PieceType.BISHOP.ordinal()]);
		drawPiece((Graphics2D) g, PieceType.ROOK, xpos, 100, map[PieceType.ROOK.ordinal()]);
	}
}
