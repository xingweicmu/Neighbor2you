package com.cmu.newbackend.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


/**
 * This is a utility class to provide common functions to access and handle
 * Database operations.
 * 
 */
public class DBUtils {
	private static boolean DB_TABLES_EXIST = false;
	private static List<String> CREATE_TABLE_LST;

	/**
	 * This method will initialize the database.
	 * 
	 * @throws java.sql.SQLException
	 */
	public static void initializeDatabase() throws SQLException {
		// Uncomment the next line to clean the DB
		//cleanDatabase();
		createTablesInDB();
	}

	/**
	 * This method will initialize the database.s
	 * 
	 * @throws java.sql.SQLException
	 */
	public static void cleanDatabase() throws SQLException {
		try (Connection conn = getConnection();
			Statement stmt = conn.createStatement();) {
				stmt.execute("DROP_TABLES");
		}
	}

	/**
	 * This method will create necessary tables in the database.
	 * 
	 * @throws java.sql.SQLException
	 */
	protected static void createTablesInDB() throws SQLException {

	}
	
	public static void setDBTableExist(boolean exsit){
		DB_TABLES_EXIST = exsit;
	}

	/**
	 * This method will check if the table exists in the database.
	 * 
	 * @param conn
	 *            - Connection to the database
	 * @param tableName
	 *            - Table name to check.
	 * 
	 * @return - Flag whether the table exists or not.
	 * 
	 * @throws java.sql.SQLException
	 */
	public static boolean doesTableExistInDB(Connection conn, String tableName)
			throws SQLException {

		return true;
	}

	/**
	 * This method returns a database connection from the Hikari CP Connection
	 * Pool
	 * 
	 * @return - Connection to the H2 database
	 * 
	 * @throws java.sql.SQLException
	 */
	public static final Connection getConnection() throws SQLException {
		IConnectionPool cp = ConnectionPoolFactory.getInstance()
				.getH2ConnectionPool();
		return cp.getConnection();
	}
	
	public static void clearTable(String theTable) {

	}
	
	public static void clearData(String theTable, String username) {

	}
	


	
}
