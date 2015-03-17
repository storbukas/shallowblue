package no.uib.inf112.group4.OpeningsBooks;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IMove;

public class QueensPawnGame {

	public static final Node queensPawnGame = 
		new Node(createMove("D2", "D4"),
			new Node(createMove("G8", "F6"),
				new Node(createMove("C2", "C4"),
					new Node(createMove("B7", "B6")
					)
				)
			),
			new Node(createMove("D7", "D5"),
				new Node(createMove("C2", "C4"),
					new Node(createMove("E7", "E6")
					)
				)
			),
			new Node(createMove("E7", "E6"),
				new Node(createMove("C2", "C4"),
					new Node(createMove("F8", "B4")
					)
				)
			),
			new Node(createMove("F7", "F5"),
				new Node(createMove("G1", "F3")
				),
				new Node(createMove("C2", "C4")
				),
				new Node(createMove("G2", "G3")
				)
			)
		);
	
	private static IMove createMove(String from, String to) {
		return new Move(new Coordinate(from), new Coordinate(to));
	}
	
}
