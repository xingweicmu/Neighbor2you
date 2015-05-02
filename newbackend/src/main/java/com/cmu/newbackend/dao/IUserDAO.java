package com.cmu.newbackend.dao;

import com.cmu.newbackend.model.User;

import java.util.List;

/**
 * Created by xing on 4/16/15.
 */
public interface IUserDAO {

    /**
     * This method will save the information of the user into the database.
     *
     * @param userPO
     *            - User information to be saved.
     */
    void save(User userPO);

    /**
     * This method will load all the users in the
     * database.
     *
     * @return - List of all users.
     */
    List<User> loadUsers() throws Exception;

    /**
     * This method with search for a user by his userName in the database. The
     * search performed is a case insensitive search to allow case mismatch
     * situations.
     *
     * @param userName
     *            - User name to search for.
     *
     * @return - UserPO with the user information if a match is found.
     */
    User findByName(String userName) throws Exception;



}
