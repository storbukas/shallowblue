package no.uib.inf112.group4.OpeningsBooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import no.uib.inf112.group4.framework.PerformedMove;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;

/**
 * Class which chooses an opening strategy from the other classes within
 * 
 * 
 */
public class OpeningBooks {

	private Node plan;
	private Random random = new Random();
	
	public IMove getMove(IBoard board) {
		if (plan == null) {
			plan = choosePlan(board);
		}
		
		// Try to stick with existing plan
		if (plan != null) {
			IMove move = getMove(board, plan);
			if (move != null) {
				return move;
			}
		}
		
		// Our plan didn't work. Try other plans
		List<Node> books = getAllBooks();
		Collections.shuffle(books);
		
		for (Node book : books) {
			IMove move = getMove(board, book);
			if (move != null) {
				plan = book;
				return move;
			}
		}
		
		// None of the plans worked
		return null;
	}
	
	private Node choosePlan(IBoard board) {
		// TODO: Choose a random one, or based on color and opponent's moves!
		List<Node> books = getAllBooks();
		return books.get(random.nextInt(books.size()));
	}
	
	public IMove getMove(IBoard board, Node plan) {
		PerformedMove[] performedMoves = board.getPerformedMoves();
		Node node = new Node(null, plan);
		for (int i = 0; i < performedMoves.length; i++) {
			// Find child node corresponding to performed move
			PerformedMove performedMove = performedMoves[i];
			boolean found = false;
			for (Node child : node.children) {
				if (performedMove.move.equals(child.move)) {
					node = child;
					found = true;
					break;
				}
			}
			
			if (!found) {
				// Look for wildcard
				for (Node child : node.children) {
					if (child.move == null) {
						node = child;
						found = true;
						break;
					}
				}
			}
			
			if (!found) {
				// Game deviated from plan, resort to minimax!
				return null;
			}
			
		}
		
		return randomChild(node);
	}

	private IMove randomChild(Node node) {
		if (node.children.length <= 0) {
			return null;
		}
		int childIndex = random.nextInt(node.children.length);
		return node.children[childIndex].move;
	}
	
	public List<Node> getAllBooks() {
		List<Node> books = new ArrayList<Node>();
		books.add(SicilianDefence.sicilianDefence);
		books.add(ItalianOpening.italianOpening);
		books.add(QueensGambit.queensGambit);
		books.add(CaroKannDefence.CaroKannDefence);
		books.add(QueensPawnGame.queensPawnGame);
		return books;
	}
}
