package com.cmu.newbackend.dao;

/**
 * Created by xing on 4/16/15.
 */
/**
 * Singleton Factory pattern class to fetch all DAO implementations.
 */
public class DAOFactory {
    private static DAOFactory instance;

    /**
     * Singleton instance access method to get the instance of the class to
     * request a specific DAO implementation.
     *
     * @return - DAOFactory instance
     */
    public static final DAOFactory getInstance() {
        if (instance == null) {
//            Log.info("Creating a new DAOFactory singleton instance.");
            instance = new DAOFactory();
        }

        return instance;
    }

    /**
     * Method to get a new object implementing IUserDAO
     *
     * @return - Object implementing IUserDAO
     */
    public IBaseDAO getUserDAO() {
        return new UserDAOImpl();
    }

    /**
     * Method to get a new object implementing IStatusDAO
     *
     * @return - Object implementing IStatusDAO
     */
    public IBaseDAO getStatusDAO() {
        return new RequestDAOImpl();
    }

}