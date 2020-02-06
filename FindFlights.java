/**
 * <h1>Find Flights</h1>
 * A small Java program FindFlights that accepts database credentials like JdbcCheckup, 
 * and then accepts two cities (Los-Angeles and Chicago for example) and finds the flights 
 * from the first to the second in the flights table. For the selected flights, it prints out 
 * the flight number, distance, and duration of the flight, computed by time difference 
 * (and displayed in hours:min:sec). 
 * <p>
 *
 * @author  Wajeeh Anwar
 * @version 1.0
 * @since   2018-11-20
 */


import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

class FindFlights {
	public static void main(String args[]) {
		String dbSys = null;
		Scanner in = null;
		try {
			in = new Scanner(System.in);
			System.out
			.println("Please enter information to test connection to the database");
			dbSys = readEntry(in, "Using Oracle (o), MySql (m) or HSQLDB (h)? ");

		} catch (IOException e) {
			System.out.println("Problem with user input, please try again\n");
			System.exit(1);
		}
		// Prompt the user for connect information
		String user = null;
		String password = null;
		String connStr = null;
		String yesNo;
		try {
			if (dbSys.equals("o")) {
				user = readEntry(in, "user: ");
				password = readEntry(in, "password: ");
				yesNo = readEntry(in,
						"use canned Oracle connection string (y/n): ");
				if (yesNo.equals("y")) {
					String host = readEntry(in, "host: ");
					String port = readEntry(in, "port (often 1521): ");
					String sid = readEntry(in, "sid (site id): ");
					connStr = "jdbc:oracle:thin:@" + host + ":" + port + ":"
							+ sid;
				} else {
					connStr = readEntry(in, "connection string: ");
				}
			} else if (dbSys.equals("m")) {// MySQL--
				user = readEntry(in, "user: ");
				password = readEntry(in, "password: ");
				yesNo = readEntry(in,
						"use canned MySql connection string (y/n): ");
				if (yesNo.equals("y")) {
					String host = readEntry(in, "host: ");
					String port = readEntry(in, "port (often 3306): ");
					String db = user + "db";
					connStr = "jdbc:mysql://" + host + ":" + port + "/" + db;
				} else {
					connStr = readEntry(in, "connection string: ");
				}
			} else if (dbSys.equals("h")) { // HSQLDB (Hypersonic) db
				yesNo = readEntry(in,
						"use canned HSQLDB connection string (y/n): ");
				if (yesNo.equals("y")) {
					String db = readEntry(in, "db or <CR>: ");
					connStr = "jdbc:hsqldb:hsql://localhost/" + db;
				} else {
					connStr = readEntry(in, "connection string: ");
				}
				user = "sa";
				password = "";
			} else {
				user = readEntry(in, "user: ");
				password = readEntry(in, "password: ");
				connStr = readEntry(in, "connection string: ");
			}
		} catch (IOException e) {
			System.out.println("Problem with user input, please try again\n");
			System.exit(3);
		}
		System.out.println("using connection string: " + connStr);
		System.out.print("Connecting to the database...");
		System.out.flush();
		Connection conn = null;
		// Connect to the database
		// Use finally clause to close connection
		try {
			conn = DriverManager.getConnection(connStr, user, password);
			System.out.println("connected.");
			findFlights(conn);
		} catch (SQLException e) {
			System.out.println("Problem with JDBC Connection\n");
			printSQLException(e);
			System.exit(4);
		} finally {
			// Close the connection, if it was obtained, no matter what happens
			// above or within called methods
			if (conn != null) {
				try {
					conn.close(); // this also closes the Statement and
					// ResultSet, if any
				} catch (SQLException e) {
					System.out
					.println("Problem with closing JDBC Connection\n");
					printSQLException(e);
					System.exit(5);
				}
			}
		}
	}




	// Accepts two cities and finds the flights from the first to the second in the flights table. 
	// For the selected flights, it prints out the flight number, distance, and duration.
	static void findFlights(Connection conn) throws SQLException {
		// Create a statement
		Statement stmt = conn.createStatement();
		ResultSet rset = null;
		try {
			Scanner in = null;
			String origin = null;
			String destination = null;
			try {
				in = new Scanner(System.in);
				origin = readEntry(in, "From: ");
				destination = readEntry(in, "To: ");

			} catch (IOException e) {
				System.out.println("Problem with user input, please try again\n");
				System.exit(1);
			}

			// Use prepared statement for security (injection attack)
			PreparedStatement sqlQuery = conn.prepareStatement("SELECT flno, distance, departs, "
					+ "arrives FROM flights WHERE origin = ? AND destination = ?");			
			sqlQuery.setString(1, origin);
			sqlQuery.setString(2, destination);

			// Run query
			rset = sqlQuery.executeQuery();
			if (!rset.isBeforeFirst()) {
				System.out.println("No flights found.\n");
				// Extract data from result set
			} else {
				System.out.println("\nFlights from " + origin + " to " + destination + "\n");
				// Extract data from result set
				while (rset.next()) {
					//Retrieve by column name
					int flno  = rset.getInt("flno");
					int distance = rset.getInt("distance");
					Timestamp departs = rset.getTimestamp("departs");
					Timestamp arrives = rset.getTimestamp("arrives");

					// Calculate duration.
					long milliseconds = arrives.getTime() - departs.getTime();
					int seconds = (int) milliseconds / 1000;

					// Calculate hours minutes and seconds
					int hours = seconds / 3600;
					int minutes = (seconds % 3600) / 60;
					seconds = (seconds % 3600) % 60;

					//Display values
					System.out.print("Flight #: " + flno + "\tDistance: " + distance + "\tDuration: " 
							+ hours + ":" + minutes+ ":" + seconds + "\n");
				}
			}
		} finally {   // Note: try without catch: let the caller handle
			// any exceptions of the "normal" db actions. 
			stmt.close(); // clean up statement resources, incl. rset
		}
	}




	// print out all exceptions connected to e by nextException or getCause
	static void printSQLException(SQLException e) {
		// SQLExceptions can be delivered in lists (e.getNextException)
		// Each such exception can have a cause (e.getCause, from Throwable)
		while (e != null) {
			System.out.println("SQLException Message:" + e.getMessage());
			Throwable t = e.getCause();
			while (t != null) {
				System.out.println("SQLException Cause:" + t);
				t = t.getCause();
			}
			e = e.getNextException();
		}
	}




	// super-simple prompted input from user
	public static String readEntry(Scanner in, String prompt)
			throws IOException {
		System.out.print(prompt);
		return in.nextLine().trim();
	}
}
