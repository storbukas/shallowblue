package no.uib.inf112.group4.OpeningsBooks;

import no.uib.inf112.group4.interfaces.IMove;

public class Node {
	public final Node[] children;
	public final IMove move;

	public Node(IMove move, Node... children) {
		this.children = children;
		this.move = move;
	}
}
