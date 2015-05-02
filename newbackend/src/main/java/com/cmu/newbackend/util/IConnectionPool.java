package com.cmu.newbackend.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This interface will define the methods that any ConnectionPool implementation
 * should implement.
 * 
 */
public interface IConnectionPool {
	Connection getConnection() throws SQLException;
}
