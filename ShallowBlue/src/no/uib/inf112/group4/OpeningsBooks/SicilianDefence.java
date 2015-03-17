package no.uib.inf112.group4.OpeningsBooks;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IMove;

public class SicilianDefence {

	public static final Node sicilianDefence = 
			new Node(createMove("E2", "E4"),
					new Node(createMove("C7", "C5"),
						new Node(createMove("G1", "F3"),
							new Node(createMove("D7", "D6"),
								new Node(createMove("F1", "B5")
									// TODO
								),
								new Node(createMove("D2", "D4"),
									new Node(createMove("C5", "D4")
									  // TODO		
									)
								)
							),
							new Node(createMove("B8", "C6"),
								new Node(createMove("D2", "D4")
											
								)
							),
							new Node(createMove("E7", "E6"),
								new Node(createMove("D2", "D4")
											
								)	
							)
						)
					)
				);

	private static IMove createMove(String from, String to) {
		return new Move(new Coordinate(from), new Coordinate(to));
	}
}
