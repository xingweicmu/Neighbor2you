package com.cmu.neighbor2you.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.cmu.newbackend.userEndpoint.UserEndpoint;
import com.cmu.newbackend.userEndpoint.model.User;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by xing on 4/17/15.
 */
public class UserService extends Service implements IUserService{
    private User user;
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

        new GetUserAsyncTask().execute(userName);
        return user;
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

    private class GetUserAsyncTask extends AsyncTask<String, Void, User> {
        private UserEndpoint myApiService = null;

        @Override
        public User doInBackground(String... params) {
            if (myApiService == null) {
                UserEndpoint.Builder builder = new UserEndpoint.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://n2y-ci-new.appspot.com/_ah/api/");
                myApiService = builder.build();
            }

            try {
                return myApiService.getUserByEmail(params[0]).execute();
            } catch (IOException e) {
                String s = e.getMessage().trim();
                Log.v("UserService", s);
                return null;
            }
        }

        @Override
        public void onPostExecute(User usr) {
            if (usr != null) {
                user = usr;
            }
        }
    }
}
