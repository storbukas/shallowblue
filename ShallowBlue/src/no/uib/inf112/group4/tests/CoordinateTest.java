package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import no.uib.inf112.group4.framework.Coordinate;

import org.junit.Test;

public class CoordinateTest {
	@Test
	public void testEquals_trueWhenCoordsTheSame() {
		Random r = new Random();
		int x = r.nextInt(8);
		int y = r.nextInt(8);
		Coordinate c1 = new Coordinate(x, y);
		Coordinate c2 = new Coordinate(x, y);

		assertTrue(c1.equals(c2));
	}

	@Test
	public void testEquals_falseWhenCoordsDiffer() {
		Random r = new Random();
		int x = r.nextInt(8);
		int y = r.nextInt(8);
		Coordinate c1 = new Coordinate(x, y);
		Coordinate c2 = new Coordinate((x + 1) % 8, y);

		assertFalse(c1.equals(c2));
	}
}
