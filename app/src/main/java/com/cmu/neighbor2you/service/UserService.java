package com.cmu.neighbor2you.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.cmu.neighbor2you.ui.BaseActivity;
import com.cmu.neighbor2you.util.PropertyUtil;
import com.cmu.newbackend.userEndpoint.UserEndpoint;
import com.cmu.newbackend.userEndpoint.model.User;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by xing on 4/17/15.
 */
public class UserService extends Service implements IUserService {
    private User user;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void save(Context context, User userPO) {
        new UpdateUserAsyncTask(context).execute(userPO);
    }

    @Override
    public List<User> loadUsers() throws Exception {
        return null;
    }

    @Override
    public User findByName(Context context, String userName) {

        new GetUserAsyncTask(context).execute(userName);
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
    public void updateUserRating(Context context, String userName, String rating) {
        new UpdateUserRatingAsyncTask(context).execute(userName, rating);
    }

    @Override
    public List<User> loadCitizenUsers() throws Exception {
        return null;
    }

    private class GetUserAsyncTask extends AsyncTask<String, Void, User> {
        private UserEndpoint myApiService = null;
        private Context context;
        private BaseActivity baseActivity;

        public GetUserAsyncTask(Context context) {
            this.context = context;
            baseActivity = (BaseActivity) context;
        }

        @Override
        public User doInBackground(String... params) {
            if (myApiService == null) {
                UserEndpoint.Builder builder = new UserEndpoint
                        .Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl(new PropertyUtil(context).getEndPointAddress());
                myApiService = builder.build();
            }

            try {
                return myApiService.getUserByEmail(params[0]).execute();
            } catch (IOException e) {
                String s = e.getMessage().trim();
                Log.v("getuser", s);
                return null;
            }
        }

        @Override
        public void onPostExecute(User usr) {
            if (usr != null) {
                user = usr;
                baseActivity.updateUIBasedOnUser(user);
            }

        }
    }

    private class UpdateUserRatingAsyncTask extends AsyncTask<String, Void, User> {
        private UserEndpoint myApiService = null;
        private Context context;

        public UpdateUserRatingAsyncTask(Context context) {
            this.context = context;
        }


        @Override
        public User doInBackground(String... params) {
            if (myApiService == null) {
                UserEndpoint.Builder builder = new UserEndpoint
                        .Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl(new PropertyUtil(context).getEndPointAddress());
                myApiService = builder.build();
            }

            try {
                return myApiService.updateRatingScore(params[0], Double.parseDouble(params[1])).execute();
            } catch (IOException e) {
                String s = e.getMessage().trim();
                Log.v("ratingService-", s);
                return null;
            }
        }

        @Override
        public void onPostExecute(User usr) {
            if (usr != null) {
                Toast.makeText(context, "Thank you! rating received!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateUserAsyncTask extends AsyncTask<User, Void, User> {
        private UserEndpoint myApiService = null;
        private Context context;

        public UpdateUserAsyncTask(Context context) {
            this.context = context;
        }


        @Override
        public User doInBackground(User... params) {
            if (myApiService == null) {
                UserEndpoint.Builder builder = new UserEndpoint
                        .Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl(new PropertyUtil(context).getEndPointAddress());
                myApiService = builder.build();
            }

            try {
                return myApiService.updateUser(params[0]).execute();
            } catch (IOException e) {
                String s = e.getMessage().trim();
                Log.v("ratingService-", s);
                return null;
            }
        }

        @Override
        public void onPostExecute(User usr) {
            if (usr != null) {
                Toast.makeText(context, "Update user success!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
