package no.uib.inf112.group4.framework.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import no.uib.inf112.group4.framework.Game;

/**
 * A GUI component for displaying the algebraic notations of
 * the current game.
 */
public class AlgebraicListBox extends JScrollPane implements
		ListCellRenderer<String> {
	private static final long serialVersionUID = 5289252601585905947L;
	private GUI gui;
	private JLabel dummy = new JLabel();
	private JList<String> list = new JList<String>();
	private Color[] colors = new Color[] { new Color(0xDD, 0xDD, 0xDD),
			new Color(0xEE, 0xEE, 0xEE) };

	public AlgebraicListBox(GUI gui) {
		this.gui = gui;
		setPreferredSize(new Dimension(100, 350));
		list.setCellRenderer(this);
		dummy.setOpaque(true);
		setViewportView(list);
	}

	@Override
	public void paint(Graphics g) {
		if (gui.game != null)
			list.setListData(buildList());
		super.paint(g);
	}

	private Stack<String> buildList() {
		Stack<String> result = new Stack<String>();
		if (gui.game == null) {
			return result;
		}

		Object[] nots = ((Game) gui.game).getAlgebraicNotations().toArray();
		String cur = "";
		for (int i = 0; i < nots.length; i++) {
			if (i % 2 == 0) {
				if (i > 0)
					result.push(cur);
				cur = "";
			}
			cur += " " + (String) nots[i];
		}
		result.push(cur);
		return result;
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends String> list,
			String value, int index, boolean isSelected, boolean cellHasFocus) {
		dummy.setText((index + 1) + "." + value);
		dummy.setForeground(Color.black);
		dummy.setBackground(colors[index % 2]);
		return dummy;
	}
}
