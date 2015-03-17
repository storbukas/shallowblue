package no.uib.inf112.group4.OpeningsBooks;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IMove;

public class ItalianOpening {

	public static final Node italianOpening = 
		new Node(createMove("E2", "E4"),
				new Node(createMove("E7", "E5"),
					new Node(createMove("G1", "F3"),
						new Node(createMove("B8", "C6"),
							new Node(createMove("F1", "C4"),
								// TODO
								new Node(createMove("F8", "C5")
								  // TODO		
								),
								
								new Node(createMove("G8", "F6")
										// TODO
								),
								new Node(createMove("F8", "E7")
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
