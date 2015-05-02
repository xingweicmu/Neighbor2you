package com.cmu.newbackend.util;

/**
 * Created by xing on 5/1/15.
 */
public class SQL {

    /*
     * List the USERS table name, and list all queries related to this table
     * here.
     */
    public static final String SSN_USERS = "SSN_USERS";

    /*
     * List the STATUS table name, and list all queries related to this table
     * here.
     */
    public static final String SSN_STATUS = "SSN_STATUS";
    public static final String SSN_MESSAGE = "SSN_MESSAGE";
    public static final String SSN_MEMORY = "SSN_MEMORY";

    /**
     * Query to check if a given table exists in the H2 database.
     */
    public static final String CHECK_TABLE_EXISTS_IN_DB = "SELECT count(1) as rowCount "
            + " FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA() "
            + " AND UPPER(TABLE_NAME) = UPPER(?)";

    /**
     * Drops all objects from the H2 database.
     */
    public static final String DROP_TABLES = "DROP ALL OBJECTS";

    // ****************************************************************
    // All queries related to USERS
    // ****************************************************************
    /**
     * Query to create the USERS table.
     */
    public static final String CREATE_USERS = "create table IF NOT EXISTS "
            + SSN_USERS + " ( user_id IDENTITY PRIMARY KEY,"
            + " user_name VARCHAR(100), active VARCHAR(100), privilege VARCHAR(100)," + " password VARCHAR(512),"
            + " salt VARCHAR(512) )";
    public static final String CREATE_DUMMY_USERS = "create table IF NOT EXISTS "
            + "SSN_DUMMY_USERS ( user_id IDENTITY PRIMARY KEY,"
            + " user_name VARCHAR(100), active VARCHAR(100), privilege VARCHAR(100)," + " password VARCHAR(512),"
            + " salt VARCHAR(512) )";

    /**
     * Query to load all users in the system.
     */
    public static final String FIND_ALL_USERS = "select * " + " from " + SSN_USERS + " order by user_name";

    public static final String FIND_ALL_CITIZEN = "select * " + " from " + SSN_USERS + " where privilege='Citizen' order by user_name";

    public static final String FIND_ADMIN = "select * from SSN_USERS where privilege='Administrator' order by user_name";

    public static final String FIND_ACTIVE_USERS = "select * from " + SSN_USERS
            + " where ACTIVE='True' order by user_name";

    /**
     * Query to find a user details depending on his name. Note that this query
     * does a case insensitive search with the user name.
     */
    public static final String FIND_USER_BY_NAME = "select *"
            + " from "
            + SSN_USERS
            + " where UPPER(user_name) = UPPER(?)";

    /**
     * Query to insert a row into the users table.
     */
    public static final String INSERT_USER = "insert into " + SSN_USERS
            + " (user_name, active, privilege, password, salt) values (?, ?, ?, ?, ?)";

    public static final String UPDATE_USERNAME = "UPDATE SSN_USERS  SET USER_NAME=?  WHERE USER_NAME=?;";
    public static final String UPDATE_USER_ACTIVE = "UPDATE SSN_USERS  SET ACTIVE=?  WHERE USER_NAME=?;";
    public static final String UPDATE_USER_PASSWORD = "UPDATE SSN_USERS  SET PASSWORD=?, SALT=? WHERE USER_NAME=?;";
    public static final String UPDATE_USER_PRIVILEGE = "UPDATE SSN_USERS  SET PRIVILEGE=?  WHERE USER_NAME=?;";
    // ****************************************************************
    // All queries related to STATUS
    // ****************************************************************
    /**
     * Query to create the STATUS table.
     */
    public static final String CREATE_STATUS = "create table IF NOT EXISTS "
            + SSN_STATUS + " ( crumb_id int AUTO_INCREMENT PRIMARY KEY,"
            + " user_name VARCHAR(100)," + " created_at VARCHAR(512),"
            + " status VARCHAR(100) )";
    public static final String CREATE_DUMMY_STATUS = "create table IF NOT EXISTS "
            + "SSN_DUMMY_STATUS ( crumb_id int AUTO_INCREMENT PRIMARY KEY,"
            + " user_name VARCHAR(100)," + " created_at VARCHAR(512),"
            + " status VARCHAR(100) )";

    /**
     * Query to load all status of a user.
     */
    public static final String FIND_STATUS_BREADCRUMB = "select crumb_id, user_name, created_at,"
            + " status " + " from " + SSN_STATUS + " order by user_name";

    /**
     * Query to load status by id.
     */
    public static final String FIND_STATUS_BY_ID = "select crumb_id, user_name, created_at,"
            + " status " + " from " + SSN_STATUS + " where crumb_id = ?";

    /**
     * Query to insert a row into the status table.
     */
    public static final String INSERT_STATUS = "insert into " + SSN_STATUS
            + " (user_name, created_at, status) values (?, ?, ?)";

    public static final String UPDATE_USERNAME_STATUS = "UPDATE SSN_STATUS SET USER_NAME=?  WHERE USER_NAME=?;";


}
