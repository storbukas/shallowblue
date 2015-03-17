package no.uib.inf112.group4.framework;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is uses SQLite JDBC to update, remove and get information from a
 * local database. The database is stored in the projects working directory as
 * EloDatabase.db. The table in the database is called elo_ratings. The columns
 * are id, name, user_name, games_played and elo_rating. The column name may be
 * equal to null, but user_name must have a value since the database primarily
 * uses user_name to find retrieve information about the player.
 * 
 */
public class EloDatabase {
	static private EloDatabase database;
	public static EloDatabase getInstance() {
		if (database == null) {
			try {
				database = new EloDatabase();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return database;
	}

	Connection connection = null;
	
	private EloDatabase() throws SQLException {

		// SQLite creates a new database file on first attempt if it does not
		// exist.
		String workingDirectory = Paths.get("").toAbsolutePath().toString();
		workingDirectory = workingDirectory.replace('\\', '/');

		connection = DriverManager.getConnection("jdbc:sqlite:"
				+ workingDirectory + "/EloDatabase.db");

		createTable();
	}

	/**
	 * Creates a new table in the database. Only creates one type of table
	 * (elo_ratings) since that is the only table needed. If the table exists
	 * from earlier then a new table will not be created.
	 */
	private void createTable() throws SQLException {

		String createQuery = "CREATE TABLE IF NOT EXISTS elo_ratings (id INTEGER PRIMARY KEY, "
				+ " name TEXT, user_name TEXT UNIQUE, games_played TEXT, elo_rating TEXT)";
		Statement statement = null;

		try {
			statement = connection.createStatement();
			statement.executeUpdate(createQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	/**
	 * Returns a boolean value, true if player exists in the table, false
	 * otherwise.
	 * 
	 * @param playerName
	 * @return playerFound
	 * @throws SQLException
	 */
	private boolean doesPlayerExist(String userName) throws SQLException {
		Statement statement = connection.createStatement();
		String query = "SELECT user_name FROM elo_ratings WHERE user_name = '"
				+ userName + "'";

		ResultSet rs = statement.executeQuery(query);
		String player;

		if (rs.next()) {
			player = rs.getString("user_name");
			if (player.equals(userName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the Elo rating of a player.
	 * 
	 * @param playerName
	 * @return eloRating
	 * @throws SQLException
	 */
	public int getEloRating(String userName) throws SQLException {

		Statement statement = null;
		String query = "SELECT name, elo_rating FROM elo_ratings WHERE user_name = '"
				+ userName + "'";
		int eloRating = 0;
		boolean foundPlayer = doesPlayerExist(userName);

		if (foundPlayer) {
			try {
				statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query);
				if (rs.next()) {
					eloRating = rs.getInt("elo_rating");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					statement.close();
				}
			}
		}
		return eloRating;
	}

	/**
	 * Returns the number of games a player has played.
	 * 
	 * @param playerName
	 * @return gamesPlayed
	 * @throws SQLException
	 */
	public int getGamesPlayed(String userName) throws SQLException {

		Statement statement = null;
		String query = "SELECT user_name, games_played FROM elo_ratings WHERE user_name = '"
				+ userName + "'";
		int gamesPlayed = -1;
		boolean foundPlayer = doesPlayerExist(userName);

		if (foundPlayer) {
			try {
				statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query);
				if (rs.next()) {
					gamesPlayed = rs.getInt("games_played");
				}
			} catch (SQLException e) {
				e.printStackTrace();

			} finally {
				if (statement != null) {
					statement.close();
				}
			}
		}
		return gamesPlayed;
	}

	/**
	 * Returns the Name of a player based on their Id. The Id is given to the
	 * player when the player is placed in the database.
	 * 
	 * @param playerName
	 * @return eloRating
	 * @throws SQLException
	 */
	public String getNameById(int Id) throws SQLException {
		Statement statement = null;
		String query = "SELECT id, name FROM elo_ratings WHERE id = " + Id;
		String playerName = "noName";

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next()) {
				playerName = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return playerName;
	}

	/**
	 * Returns the Name of a player based on their Id. The Id is given to the
	 * player when the player is placed in the database.
	 * 
	 * @param playerName
	 * @return eloRating
	 * @throws SQLException
	 */
	public String getUsernameById(int Id) throws SQLException {
		Statement statement = null;
		String query = "SELECT id, user_name FROM elo_ratings WHERE id = " + Id;
		String userName = "noName";

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next()) {
				userName = rs.getString("user_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return userName;
	}

	/**
	 * Returns the Name of a player based on their Id. The Id is given to the
	 * player when the player is placed in the database.
	 * 
	 * @param playerName
	 * @return playerName
	 * @throws SQLException
	 */
	public int getIdByUserName(String userName) throws SQLException {
		Statement statement = null;
		String query = "SELECT id, user_name FROM elo_ratings WHERE user_name = '"
				+ userName + "'";
		int id = -1;

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return id;
	}

	/**
	 * Returns the rank of a player based on their Elo rating.
	 * 
	 * @param playerName
	 * @return players Elo rating
	 */
	public int getRank(String userName) throws SQLException {
		Statement statement = null;
		String query = "SELECT user_name, elo_rating FROM elo_ratings ORDER BY elo_rating DESC";
		int rank = 1;
		boolean foundPlayer = doesPlayerExist(userName);
		String currentPlayer;

		if (foundPlayer) {
			try {
				statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query);
				while (rs.next()) {
					currentPlayer = rs.getString("user_name");
					if (currentPlayer.equals(userName)) {
						return rank;
					}
					rank++;

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/**
	 * Removes a player and their details from the table.
	 * 
	 * @param playerName
	 * @throws SQLException
	 */
	public void remove(String userName) throws SQLException {

		Statement statement = null;
		boolean playerFound = doesPlayerExist(userName);

		if (playerFound) {
			try {
				String query = "DELETE from elo_ratings WHERE user_name = '"
						+ userName + "'";
				statement = connection.createStatement();
				statement.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					statement.close();
				}
			}
		}
	}

	/**
	 * Updates a players details. If a player does not exist then they are added
	 * to the table. When a players details are updated their gamesPlayed value
	 * increments by 1. Name can equal null.
	 * 
	 * @param playerName
	 * @param eloRating
	 * @throws SQLException
	 */
	public void update(String playerName, String userName, int eloRating)
			throws SQLException {

		boolean foundPlayer = doesPlayerExist(userName);
		Statement statement = null;
		String query;
		if (!foundPlayer) { // If player does not exist, add player to table.
			if (playerName == null) {
				query = "INSERT INTO elo_ratings (name, user_name, games_played, elo_rating) values ('no_name'"
						+ ", '" + userName + "', 1, " + eloRating + ")";
			} else {
				query = "INSERT INTO elo_ratings (name, user_name, games_played, elo_rating) values ('"
						+ playerName
						+ "', '"
						+ userName
						+ "', 1, "
						+ eloRating
						+ ")";
			}
			try {
				statement = connection.createStatement();
				statement.executeUpdate(query);
			} catch (SQLException e) {
				System.out.println("Player already exists in database!");
			} finally {
				if (statement != null) {
					statement.close();
				}
			}
		} else { // Update player elo_rating and games_played.
			String queryRating = "UPDATE elo_ratings SET elo_rating = "
					+ eloRating + " WHERE user_name = '" + userName + "'";
			int gamesPlayed = getGamesPlayed(userName) + 1;
			String queryGamesPlayed = "UPDATE elo_ratings SET games_played = "
					+ gamesPlayed + " WHERE user_name = '" + userName + "'";

			try {
				statement = connection.createStatement();
				statement.executeUpdate(queryRating);
				statement.executeUpdate(queryGamesPlayed);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					statement.close();
				}
			}
		}
	}
	
	public void closeDatabase() throws SQLException {
		connection.close();
	}

	/**
	 * Dumps the contents of the table into the console. Used for debugging.
	 */
	public void consoleDump() throws SQLException {
		String query = "SELECT id, name, user_name, games_played, elo_rating from elo_ratings";
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String userName = rs.getString("user_name");
				int gamesPlayed = rs.getInt("games_played");
				int eloRating = rs.getInt("elo_rating");
				System.out.println(id + "\t" + name + "\t" + userName + "\t"
						+ gamesPlayed + "\t" + eloRating);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}

	}
}
