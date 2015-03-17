package no.uib.inf112.group4.framework.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.PlayerType;
import no.uib.inf112.group4.framework.players.FixedDifficultyAI;
import no.uib.inf112.group4.framework.players.HumanPlayer;
import no.uib.inf112.group4.framework.players.TimeBasedAI;
import no.uib.inf112.group4.interfaces.IPlayer;

/**
 * A GUI component used to prompt the user for game settings
 * and then starts a new game.
 */
public class NGPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -371459697762178710L;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private GUI gui;
	private JRadioButton rdbtnPlayerVsPlayer;
	private JRadioButton rdbtnPlayerVsAi;
	private JRadioButton rdbtnAiVsPlayer;
	private JRadioButton rdbtnAiVsAi;
	private JSlider slBlackAI;
	private JSlider slWhiteAI;
	private JLabel lblWhiteDifficulty;
	private JLabel lblWhitePlayer;
	private JLabel lblBlackPlayer;
	private JTextField tfWhiteName;
	private JTextField tfBlackName;
	private JLabel lblBlackDifficulty;
	private final JPanel panel_1 = new JPanel();
	private JRadioButton rdbtnFIDE;
	private JLabel lblRuleSet;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JRadioButton rdbtnBlitz;
	private JLabel lblNewLabel;

	/**
	 * Create the panel.
	 */
	public NGPanel(GUI gui) {
		setPreferredSize(new Dimension(480, 200));
		this.gui = gui;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel lblTitle = new JLabel("Select game mode: (White vs. Black)");
		lblTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitle);

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		rdbtnPlayerVsPlayer = new JRadioButton("Player vs. Player");
		buttonGroup.add(rdbtnPlayerVsPlayer);
		rdbtnPlayerVsPlayer.setSelected(true);
		panel.add(rdbtnPlayerVsPlayer);

		rdbtnPlayerVsAi = new JRadioButton("Player vs. AI");
		buttonGroup.add(rdbtnPlayerVsAi);
		panel.add(rdbtnPlayerVsAi);

		rdbtnAiVsPlayer = new JRadioButton("AI vs. Player");
		buttonGroup.add(rdbtnAiVsPlayer);
		panel.add(rdbtnAiVsPlayer);

		rdbtnAiVsAi = new JRadioButton("AI vs. AI");
		buttonGroup.add(rdbtnAiVsAi);
		panel.add(rdbtnAiVsAi);
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel_1);

		lblNewLabel = new JLabel("            ");
		panel_1.add(lblNewLabel);

		lblRuleSet = new JLabel("Rule set:");
		panel_1.add(lblRuleSet);

		rdbtnFIDE = new JRadioButton("FIDE");
		rdbtnFIDE.setSelected(true);
		buttonGroup_1.add(rdbtnFIDE);
		panel_1.add(rdbtnFIDE);

		rdbtnBlitz = new JRadioButton("Blitz");
		buttonGroup_1.add(rdbtnBlitz);
		panel_1.add(rdbtnBlitz);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setResizeWeight(0.5);
		splitPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		splitPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		add(splitPane);

		JPanel blackplayerpanel = new JPanel();
		splitPane.setRightComponent(blackplayerpanel);
		blackplayerpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		lblBlackPlayer = new JLabel("Black's name:");
		blackplayerpanel.add(lblBlackPlayer);

		tfBlackName = new JTextField("Black player");
		blackplayerpanel.add(tfBlackName);
		tfBlackName.setColumns(15);

		lblBlackDifficulty = new JLabel("AI Difficulty");
		lblBlackDifficulty.setEnabled(false);
		blackplayerpanel.add(lblBlackDifficulty);

		slBlackAI = new JSlider();
		slBlackAI.setEnabled(false);
		slBlackAI.setSnapToTicks(true);
		slBlackAI.setPaintLabels(true);
		slBlackAI.setMajorTickSpacing(1);
		slBlackAI.setMaximum(10);
		slBlackAI.setValue(4);
		blackplayerpanel.add(slBlackAI);

		JPanel whiteplayerpanel = new JPanel();
		splitPane.setLeftComponent(whiteplayerpanel);
		whiteplayerpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		lblWhitePlayer = new JLabel("White's name:");
		whiteplayerpanel.add(lblWhitePlayer);

		tfWhiteName = new JTextField("White player");
		whiteplayerpanel.add(tfWhiteName);
		tfWhiteName.setColumns(15);

		lblWhiteDifficulty = new JLabel("AI Difficulty");
		lblWhiteDifficulty.setEnabled(false);
		whiteplayerpanel.add(lblWhiteDifficulty);

		slWhiteAI = new JSlider();
		slWhiteAI.setEnabled(false);
		slWhiteAI.setSnapToTicks(true);
		slWhiteAI.setPaintLabels(true);
		slWhiteAI.setMajorTickSpacing(1);
		slWhiteAI.setMaximum(10);
		slWhiteAI.setValue(4);
		whiteplayerpanel.add(slWhiteAI);

		rdbtnPlayerVsPlayer.addActionListener(this);
		rdbtnAiVsPlayer.addActionListener(this);
		rdbtnPlayerVsAi.addActionListener(this);
		rdbtnAiVsAi.addActionListener(this);

		setWhite(true);
		setBlack(true);
	}


	public boolean isFIDE() {
		return rdbtnFIDE.isSelected() ? true : false;
	}

	public IPlayer getWhite() {
		PlayerType pt = tfWhiteName.isEnabled() ? PlayerType.LOCAL
				: PlayerType.COMPUTER;
		if (pt == PlayerType.LOCAL) {
			return new HumanPlayer(gui, tfWhiteName.getText(), PlayerColor.WHITE);
		} else {
			if (slWhiteAI.getValue() == 0) {
				return new TimeBasedAI(tfWhiteName.getText(), PlayerColor.WHITE, 3);
			} else {
				return new FixedDifficultyAI(tfWhiteName.getText(), PlayerColor.WHITE, slWhiteAI.getValue());
			}
		}
	}

	public IPlayer getBlack() {
		PlayerType pt = tfBlackName.isEnabled() ? PlayerType.LOCAL
				: PlayerType.COMPUTER;
		if (pt == PlayerType.LOCAL) { //PlayerType.LOCAL
			return new HumanPlayer(gui, tfBlackName.getText(), PlayerColor.BLACK);
		} else {
			if (slBlackAI.getValue() == 0) {
				return new TimeBasedAI(tfBlackName.getText(), PlayerColor.BLACK, 3);
			} else {
				return new FixedDifficultyAI(tfBlackName.getText(), PlayerColor.BLACK, slBlackAI.getValue());
			}
		}
	}

	public void setPVP() {
		setWhite(true);
		setBlack(true);
	}

	public void setPVA() {
		setWhite(true);
		setBlack(false);
	}

	public void setAVP() {
		setWhite(false);
		setBlack(true);
	}

	public void setAVA() {
		setWhite(false);
		setBlack(false);
	}

	private void setWhite(boolean enabled) {
		slWhiteAI.setEnabled(!enabled);
		lblWhiteDifficulty.setEnabled(!enabled);
		tfWhiteName.setEnabled(enabled);
		lblWhitePlayer.setEnabled(enabled);
		if (!enabled) {
			tfWhiteName.setText("White AI");
		}
	}

	private void setBlack(boolean enabled) {
		slBlackAI.setEnabled(!enabled);
		lblBlackDifficulty.setEnabled(!enabled);
		tfBlackName.setEnabled(enabled);
		lblBlackPlayer.setEnabled(enabled);
		if (!enabled) {
			tfBlackName.setText("Black AI");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == rdbtnPlayerVsPlayer)
			setPVP();
		else if (e.getSource() == rdbtnPlayerVsAi)
			setPVA();
		else if (e.getSource() == rdbtnAiVsAi)
			setAVA();
		else if (e.getSource() == rdbtnAiVsPlayer)
			setAVP();
	}
}
