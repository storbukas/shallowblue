package no.uib.inf112.group4.framework;

public class ELO {
	public static void main(String[] args) {
		int[] tab = computeElo(1450, 1450, Winner.PLAYER1);
		for (int i = 0; i < tab.length; i++) {
			System.out.println(tab[i]);
		}
	}

	/**
	 * This method calculate the new elo rating for two players
	 * 
	 * @param playerOneScore
	 *            The old score for the first player
	 * @param playerTwoScore
	 *            The old score for the second player
	 * @param winner
	 *            who won the game. PLAYER1 || PLAYER2 || DRAW
	 * @return Array with new elo scores
	 */
	public static int[] computeElo(int playerOneScore, int playerTwoScore,
			Winner winner) {
		// Calculate the scoreFactor
		// 1.0 for winner | 0.5 for draw | 0.0 for looser
		double scoreFactorPlayerOne = 0.0;
		double scoreFactorPlayerTwo = 0.0;
		if (winner == Winner.PLAYER1) {
			scoreFactorPlayerOne = 1.0;
			scoreFactorPlayerTwo = 0.0;
		} else if (winner == Winner.PLAYER2) {
			scoreFactorPlayerOne = 0.0;
			scoreFactorPlayerTwo = 1.0;
		} else if (winner == Winner.DRAW) {
			scoreFactorPlayerOne = 0.5;
			scoreFactorPlayerTwo = 0.5;
		}
		int eloDiff = playerTwoScore - playerOneScore;
		// Calculate the K-factor
		int kFactor = CalculateKfactor(playerOneScore, playerTwoScore);

		// Calculate for player1
		// EloChange = Kfactor * ( Scorefactor -
		// 1/10^((Elodifference/Ffactor)+1)
		double percentageToWin = 1 / (1 + Math.pow(10.0, eloDiff / 400));
		long pendingElo = Math.round(kFactor
				* (scoreFactorPlayerOne - percentageToWin));
		int newPlayerOneScore = playerOneScore += pendingElo;
		if (playerOneScore < 1015) {
			playerOneScore = 1015;
		}

		// Calculate for player2
		percentageToWin = 1 / (1 + Math.pow(10.0, -eloDiff / 400));
		pendingElo = Math.round(kFactor
				* (scoreFactorPlayerTwo - percentageToWin));
		int newPlayerTwoScore = playerTwoScore += pendingElo;
		if (newPlayerTwoScore < 1015) {
			newPlayerTwoScore = 1015;
		}

		// Save new elo scores in an array and return it
		int[] scores = { newPlayerOneScore, newPlayerTwoScore };
		return scores;
	}

	/**
	 * This method takes two Elo-ratings and calculate the K-value for the
	 * ELO-formula
	 * 
	 * @param playerOneScore
	 *            The Elo-rating for player1
	 * @param playerTwoScore
	 *            The Elo-rating for player2
	 * @return
	 */
	private static int CalculateKfactor(int playerOneScore, int playerTwoScore) {
		int kFactor = 0;
		if (playerOneScore < 2100 || playerTwoScore < 2100) {
			kFactor = 32;
		} else if (playerOneScore < 2401 || playerTwoScore < 2401) {
			kFactor = 24;
		} else if (playerOneScore > 2400 && playerTwoScore > 2400) {
			kFactor = 16;
		}
		return kFactor;
	}
}