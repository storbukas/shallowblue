package no.uib.inf112.group4.tests;

import java.util.List;
import java.util.Random;

import no.uib.inf112.group4.OpeningsBooks.Node;
import no.uib.inf112.group4.OpeningsBooks.OpeningBooks;
import no.uib.inf112.group4.framework.Board;
import no.uib.inf112.group4.interfaces.IBoard;
import no.uib.inf112.group4.interfaces.IMove;

import org.junit.Assert;
import org.junit.Test;

public class OpeningBooksTest {
	Random random = new Random();
	
	@Test
	public void testMovesAreValid() {
		IBoard board = Board.createDefaultBoard();
		List<Node> books = new OpeningBooks().getAllBooks();
		for (Node book : books) {
			int index = books.indexOf(book);
			assertMovesAreValid("Book at index #" + index, book, board);
		}
	}
	
	private void assertMovesAreValid(String message, Node node, IBoard board) {
		IMove move = node.move;
		if (move == null) {
			move = getRandomMove(board);
		}
		List<IMove> allValidMoves = board.getAllValidMoves();
		Assert.assertTrue(message + " Move: " + move, allValidMoves.contains(move));
		
		board = node.move.execute(board);
		for (Node child : node.children) {
			assertMovesAreValid(message, child, board);
		}
	}

	private IMove getRandomMove(IBoard board) {
		List<IMove> allValidMoves = board.getAllValidMoves();
		int index = random.nextInt(allValidMoves.size());
		return allValidMoves.get(index);
	}

	@Test
	public void testDoesNotContainTwoWildcardsInSameLevel() {
		List<Node> books = new OpeningBooks().getAllBooks();
		for (Node book : books) {
			int index = books.indexOf(book);
			assertDoesNotContainTwoWildcardInSameLevel("Book at index #" + index, book);
		}
	}

	private static void assertDoesNotContainTwoWildcardInSameLevel(String message, Node node) {
		boolean hasWildcard = false;
		for (Node child : node.children) {
			if (child.move == null) {
				if (hasWildcard) {
					Assert.assertFalse(message, hasWildcard);
				} else {
					hasWildcard = true;
				}
			}
			assertDoesNotContainTwoWildcardInSameLevel(message, child);
		}
	}
}
