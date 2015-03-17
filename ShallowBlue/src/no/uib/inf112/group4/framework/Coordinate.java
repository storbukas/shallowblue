package no.uib.inf112.group4.framework;

/**
 * Coordinates for the chess board. Immutable and validated.
 */
public final class Coordinate {
	private final int x;
	private final int y;

	public Coordinate(int x, int y) {
		// Validate input
		if (!(x >= 0 && x < 8) || !(y >= 0 && y < 8)) {
			throw new IllegalArgumentException("Invalid coordinate: (" + x
					+ ", " + y + ")");
		}

		this.x = x;
		this.y = y;
	}

	public Coordinate(char x, char y) {
		this(x - 'A', y - '1');
	}

	public Coordinate(String coords) {
		this(coords.charAt(0), coords.charAt(1));
		if (coords.length() != 2) {
			throw new IllegalArgumentException("Invalid coordinate: (" + coords
					+ ")");
		}
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	@Override
	public String toString() {
		return "" + (char) (x + 'A') + (char) (y + '1');
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		Coordinate other = (Coordinate) obj;
		if (x != other.x || y != other.y) {
			return false;
		}
		return true;
	}

	// TODO: Add extra methods as needed
}
