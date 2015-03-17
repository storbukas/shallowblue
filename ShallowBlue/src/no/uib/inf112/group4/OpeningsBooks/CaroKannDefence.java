package no.uib.inf112.group4.OpeningsBooks;

import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.moves.Move;
import no.uib.inf112.group4.interfaces.IMove;

public class CaroKannDefence {
	public static final Node CaroKannDefence = 
			new Node(createMove("E2", "E4"),
					new Node(createMove("C7", "C6"),
						new Node(createMove("D2", "D4"),
							new Node(createMove("D7", "D5"),
								new Node(createMove("E4", "E5")
								),
								new Node(createMove("E4", "D5"),
									new Node(createMove("C6", "D5"),
										new Node(createMove("F1", "D3")
										)
									)
								),
								new Node(createMove("B1", "C3"),
									new Node(createMove("D5", "E4"),
										new Node(createMove("C3", "E4"),
											new Node(createMove("C8", "F5")
											),
											new Node(createMove("B8", "D7")
											)
										)
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
