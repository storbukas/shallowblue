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
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import no.uib.inf112.group4.framework.players.NetworkPlayer;

public class OPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -5160411385971518469L;

	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroupRule = new ButtonGroup();
	private GUI gui;
	private JLabel lblName;
	private JTextField tfName;
	private JLabel lblName2;
	private JTextField tfName2;
	private JLabel lblIP;
	private JTextField tfIP;
	private JRadioButton rbtnServer;
	private JRadioButton rbtnClient;
	private JLabel lblMode;
	private JRadioButton rbtnFIDE;
	private JRadioButton rbtnBlitz;
	private JLabel lblRuleSet;
	
	
	public OPanel(GUI gui) {
		setPreferredSize(new Dimension(480, 200));
		this.gui = gui;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel lblTitle = new JLabel("Select game mode: (White vs. Black)");
		lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitle);
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		
		lblMode = new JLabel("mode:");
		panel.add(lblMode);
		
		rbtnServer = new JRadioButton("server");
		buttonGroup.add(rbtnServer);
		rbtnServer.setSelected(true);
		rbtnServer.addActionListener(this);
		panel.add(rbtnServer);
		
		rbtnClient = new JRadioButton("client");
		rbtnClient.addActionListener(this);
		buttonGroup.add(rbtnClient);
		panel.add(rbtnClient);
		
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setResizeWeight(0);
		splitPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		splitPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		add(splitPane);
		
		JPanel namePanel = new JPanel();
		splitPane.setLeftComponent(namePanel);

		lblName = new JLabel("name:");
		namePanel.add(lblName);
		tfName = new JTextField();
		tfName.setColumns(15);
		namePanel.add(tfName);
		
		JPanel rulePanel = new JPanel();
		lblRuleSet = new JLabel("Rule Set");
		rulePanel.add(lblRuleSet);
		rbtnFIDE = new JRadioButton("FIDE");
		buttonGroupRule.add(rbtnFIDE);
		rulePanel.add(rbtnFIDE);
		rbtnBlitz = new JRadioButton("Blitz");
		buttonGroupRule.add(rbtnBlitz);
		rulePanel.add(rbtnBlitz);
		namePanel.add(rulePanel);
		
		JPanel ipPanel = new JPanel();
		splitPane.setRightComponent(ipPanel);
		
		lblName2 = new JLabel("your name");
		tfName2 = new JTextField();
		tfName2.setColumns(15);
		ipPanel.add(lblName2);
		ipPanel.add(tfName2);
		
		lblIP = new JLabel("host ip:");
		tfIP = new JTextField();
		tfIP.setColumns(15);
		ipPanel.add(lblIP);
		ipPanel.add(tfIP);
		
		setServer();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == rbtnServer) {
			setServer();
		}
		if(e.getSource() == rbtnClient)
			setClient();
	}
	
	public NetworkPlayer getPlayer(){
		if(rbtnServer.isSelected()) {
			NetworkPlayer player = new NetworkPlayer(gui, tfName.getText(), true);
			player.startServer();
			return player;
		}
		else{
			NetworkPlayer player = new NetworkPlayer(gui, tfName2.getText(), false);
			player.setHost(tfIP.getText());
			player.connectHost();
			return player;
		}
	}

	public void setServer() {
		tfIP.setEnabled(false);
		lblIP.setEnabled(false);
		lblName2.setEnabled(false);
		tfName2.setEnabled(false);
		tfName.setEnabled(true);
		lblName.setEnabled(true);
		lblRuleSet.setEnabled(true);
		
		rbtnFIDE.setEnabled(true);
		rbtnBlitz.setEnabled(true);
	}

	public void setClient() {
		tfIP.setEnabled(true);
		lblIP.setEnabled(true);
		lblName2.setEnabled(true);
		tfName2.setEnabled(true);
		tfName.setEnabled(false);
		lblName.setEnabled(false);
		lblRuleSet.setEnabled(false);
		rbtnFIDE.setEnabled(false);
		rbtnBlitz.setEnabled(false);
		
	}

}
