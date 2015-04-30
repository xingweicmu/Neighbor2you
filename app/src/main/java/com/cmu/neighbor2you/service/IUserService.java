package com.cmu.neighbor2you.service;

import com.cmu.newbackend.userEndpoint.model.User;
import java.util.List;

/**
 * Created by xing on 4/17/15.
 */
public interface IUserService {
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

    /**
     *
     * @return the list of userPOs
     */
    List<User> loadActiveUsers() throws Exception;

    /**
     *
     * @param userName the username
     * @param active the status
     */
    void updateActive(String userName, String active) throws Exception;

    /**
     *
     * @param userName the username
     * @param po the userPO
     */
    void updatePassword(String userName, User po) throws Exception;

    /**
     *
     * @param userName the username
     * @param privilege the privilege
     */
    void updatePrivilege(String userName, String privilege) throws Exception;

    /**
     *
     * @param userName the username to update
     * @param newName the new name
     */
    void updateUserName(String userName, String newName) throws Exception;

    /**
     *
     * @return the list of userPOs
     */
    List<User> loadCitizenUsers() throws Exception;
}
