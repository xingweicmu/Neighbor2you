package com.cmu.backend.dao;

import com.cmu.backend.model.User;

import java.util.List;

/**
 * Created by xing on 4/16/15.
 */
public class UserDAOImpl implements  IUserDAO {


    @Override
    public void save(User userPO) {}

    @Override
    public List<User> loadUsers() throws Exception {
        return null;
    }

    @Override
    public User findByName(String userName) throws Exception {
        return null;
    }

    @Override
    public List<User> loadActiveUsers() throws Exception {
        return null;
    }

    @Override
    public void updateActive(String userName, String active) throws Exception {

    }

    @Override
    public void updatePassword(String userName, User po) throws Exception {

    }

    @Override
    public void updatePrivilege(String userName, String privilege) throws Exception {

    }

    @Override
    public void updateUserName(String userName, String newName) throws Exception {

    }

    @Override
    public List<User> loadCitizenUsers() throws Exception {
        return null;
    }
}
