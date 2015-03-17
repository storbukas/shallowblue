package no.uib.inf112.group4.OpeningsBooks;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IMove;

public class QueensGambit {
	
	public static final Node queensGambit = 
			new Node(createMove("D2", "D4"),
				new Node(createMove("D7", "D5"),
					new Node(createMove("C2", "C4"),
						new Node(createMove("D5", "C4"),
							new Node(createMove("G1", "F3"),
								new Node(createMove("G8", "F6"),
									new Node(createMove("D1", "A4")
									),
									new Node(createMove("E2", "E3")
									)
								)
							)
						),
						new Node(createMove("B8", "C6"),
							new Node(createMove("B1", "C3")
						    ),
						    new Node(createMove("G1", "F3")
						    )
						),
						new Node(createMove("C7", "C5"),
							new Node(createMove("C4", "D5"),
								new Node(createMove("G8", "F6")
								)
							)
						),
						new Node(createMove("C7", "C6"),
							new Node(createMove("C4", "D5"),
								new Node(createMove("C6", "D5")
								)
							)
						)
					)
				)
			);
	
	private static IMove createMove(String from, String to) {
		return new Move(new Coordinate(from), new Coordinate(to));
	}
	
}
