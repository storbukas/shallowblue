package no.uib.inf112.group4.framework.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import no.uib.inf112.group4.framework.BoardEvaluator;
import no.uib.inf112.group4.framework.Clock;
import no.uib.inf112.group4.framework.PlayerColor;

/**
 * A GUI component that display a player'ss name,
 * score and time spent.
 */
public class StatusFrame extends JPanel {
	private static final long serialVersionUID = -7670419351938439455L;
	private PlayerColor player;
	private GUI gui;
	private JLabel pname = new JLabel();
	private JLabel score = new JLabel();
	private JLabel timer = new JLabel();
	private JLabel timer2 = new JLabel();

	// Score
	BoardEvaluator eve = new BoardEvaluator();

	/**
	 * Creates a status frame for the player with given 
	 * player color in the given GUI.
	 */
	StatusFrame(GUI gui, boolean white) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		player = white ? PlayerColor.WHITE : PlayerColor.BLACK;
		pname.setAlignmentX(Component.CENTER_ALIGNMENT);
		pname.setForeground(white ? Color.black : Color.white);
		score.setAlignmentX(Component.CENTER_ALIGNMENT);
		score.setForeground(white ? Color.black : Color.white);
		if (white == true) {
			timer.setAlignmentX(Component.CENTER_ALIGNMENT);
			timer.setForeground(Color.white);
			add(timer);

		} else {
			timer2.setAlignmentX(Component.CENTER_ALIGNMENT);
			timer2.setForeground(Color.white);
			add(timer2);
		}

		add(pname);
		add(score);

		this.gui = gui;
		setBackground(white ? Color.LIGHT_GRAY : Color.DARK_GRAY);
		setPreferredSize(new Dimension(0, 50));
		new Timer(delay, taskPerformer).start();
	}

	int delay = 1000; // milliseconds
	ActionListener taskPerformer = new ActionListener() {
		// uppdates the clock every second and keeps the difference between
		// black/white player
		@Override
		public void actionPerformed(ActionEvent evt) {
			if(gui.game == null)
			return;
			Clock clock = gui.game.getClock();
			if (clock == null)
				return;

			PlayerColor playerColor = gui.game.getPlayerColor(gui.game
					.getActivePlayer());
			if (playerColor == PlayerColor.WHITE) {
				timer.setForeground(Color.red);
				timer2.setForeground(Color.white);
				clock.overtime(clock.getPlayerMinutes(playerColor));
				timer.setText("" + clock.getPlayerMinutes(playerColor) + " m "
						+ clock.getPlayerSeconds(playerColor) % 60 + " s");
			} else if (playerColor == PlayerColor.BLACK) {
				timer2.setForeground(Color.red);
				timer.setForeground(Color.white);
				clock.overtime(clock.getPlayerMinutes(playerColor));
				timer2.setText("" + clock.getPlayerMinutes(playerColor) + " m "
						+ clock.getPlayerSeconds(playerColor) % 60 + " s");
			}

		}
	};

	@Override
	/**
	 * TODO: Update score properly
	 */
	public void paint(Graphics g) {
		if (!gui.firstStart) {
			// Update text
			pname.setText(" " + gui.game.getPlayer(player).getName());
			score.setText("Score: "
					+ eve.evaluate(gui.game.getBoard(),
							gui.game.getPlayer(player).getColor()).value);

			new Timer(delay, taskPerformer).start();

		}
		// Draw contents
		super.paint(g);
	}
}
