package no.uib.inf112.group4.framework.ui;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;

import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.pieces.PieceType;

/**
 * A GUI component used to ask the player which piece type
 * he wishes the pawn to be promoted to.
 */
public class PromotionFrame extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = 437669531076673435L;

	private static PromotionFrame[] frames = new PromotionFrame[] {
		new PromotionFrame(PlayerColor.WHITE),
		new PromotionFrame(PlayerColor.BLACK),
	};
	
	private JButton[] buttons = new JButton[4];
	private PieceType selected = null;
	
	private PromotionFrame(PlayerColor col) {
		super("Select promotion piece");
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons[0] = new JButton(new ImageIcon(Icons.getImage(col, PieceType.QUEEN).getScaledInstance(49, 49, Image.SCALE_SMOOTH)));
		buttons[1] = new JButton(new ImageIcon(Icons.getImage(col, PieceType.KNIGHT).getScaledInstance(49, 49, Image.SCALE_SMOOTH)));
		buttons[2] = new JButton(new ImageIcon(Icons.getImage(col, PieceType.ROOK).getScaledInstance(49, 49, Image.SCALE_SMOOTH)));
		buttons[3] = new JButton(new ImageIcon(Icons.getImage(col, PieceType.BISHOP).getScaledInstance(49, 49, Image.SCALE_SMOOTH)));
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].setActionCommand(Integer.toString(i));
			buttons[i].addActionListener(this);
			add(buttons[i]);
		}
		pack();
	};
	
	public static PieceType getPromotedPiece(GUI gui, PlayerColor color) {
		PromotionFrame pf = frames[color.ordinal()];
		gui.getLayeredPane().add(frames[color.ordinal()]);
		pf.setLocation(gui.getWidth()/2-pf.getWidth()/2, gui.getHeight()/2-pf.getHeight()/2);
		pf.selected = null;
		pf.setVisible(true);
		while(pf.selected == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				return null;
			}
		}
		pf.setVisible(false);
		gui.getLayeredPane().remove(pf);
		Sounds.playPromotion();
		return pf.selected;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "0": // queen
			selected = PieceType.QUEEN;
			break;
		case "1": // knight
			selected = PieceType.KNIGHT;
			break;
		case "2": // rook
			selected = PieceType.ROOK;
			break;
		case "3": // bishop
			selected = PieceType.BISHOP;
			break;
		}
	}
}
