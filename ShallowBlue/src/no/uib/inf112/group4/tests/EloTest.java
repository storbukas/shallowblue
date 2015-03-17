package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertEquals;
import no.uib.inf112.group4.framework.ELO;
import no.uib.inf112.group4.framework.Winner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EloTest {
	int playerOneScore;
	int playerTwoScore;
    @Before
	public void setUp(){
    	playerOneScore = 1450;
    	playerTwoScore = 1450;
	}
    @After
    public void tearDown(){
    	playerOneScore = 0;
    	playerTwoScore = 0;
    }
    @Test
    public void testPlayerOneWin(){
    	int array[] = ELO.computeElo(playerOneScore, playerTwoScore, Winner.PLAYER1);
    	assertEquals(array[0],1466);
    	assertEquals(array[1],1434);
    }
    @Test
    public void testPlayerTwoWin(){
    	int array[] = ELO.computeElo(playerOneScore, playerTwoScore, Winner.PLAYER2);
    	assertEquals(array[0],1434);
    	assertEquals(array[1],1466);
    }
    @Test
    public void testDraw(){
    	int array[] = ELO.computeElo(playerOneScore, playerTwoScore, Winner.DRAW);
    	assertEquals(array[0],1450);
    	assertEquals(array[1],1450);
    }
}
