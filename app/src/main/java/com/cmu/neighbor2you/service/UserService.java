package com.cmu.neighbor2you.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.cmu.neighbor2you.model.User;

import java.util.List;

/**
 * Created by xing on 4/17/15.
 */
public class UserService extends Service implements IUserService{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void save(User userPO) {

    }

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
