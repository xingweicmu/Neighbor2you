package com.cmu.neighbor2you.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.util.PropertyUtil;
import com.cmu.neighbor2you.util.SharedPreferencesUtil;
import com.cmu.newbackend.userEndpoint.UserEndpoint;
import com.cmu.newbackend.userEndpoint.model.User;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;


public class LoginActivity extends ActionBarActivity {

    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = (EditText) findViewById(R.id.et_Email);
        password = (EditText) findViewById(R.id.et_Pwd);

    }

    @Override
    protected void onResume() {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(this);
        if (sharedPreferencesUtil.getUserEmail() != null && sharedPreferencesUtil.getUserPassword() != null) {
            startActivity(new Intent(this, MainPageActivity.class));
            this.finish();
        }
        super.onResume();
    }

    public void login(View v) {
        String emailS = email.getText().toString();
        String passwordS1 = password.getText().toString();
        if (emailS.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")) {
            User user = new User();
            user.setEmail(emailS);
            user.setPassword(passwordS1);
            new LoginAsyncTask(this).execute(user);
        } else {
            Toast.makeText(this, "Email is malformed!", Toast.LENGTH_LONG).show();
        }

    }


    public void gotoRegister(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private class LoginAsyncTask extends AsyncTask<User, Void, User> {
        private UserEndpoint myApiService = null;
        private Context context;

        public LoginAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        public User doInBackground(User... params) {
            if (myApiService == null) { // Only do this once
//                UserEndpoint.Builder builder = new UserEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
//                        new AndroidJsonFactory(), null)
//                        // options for running against local devappserver
//                        // - 10.0.2.2 is localhost's IP address in Android emulator
//                        // - turn off compression when running against local devappserver
//                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
//                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                            @Override
//                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                                abstractGoogleClientRequest.setDisableGZipContent(true);
//                            }
//                        });
                // end options for devappserver

                UserEndpoint.Builder builder = new UserEndpoint.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl(new PropertyUtil(context).getEndPointAddress());
                myApiService = builder.build();
            }

            try {
                return myApiService.authenticate(params[0]).execute();
            } catch (IOException e) {
                String s = e.getMessage().trim();
                Log.v("TAG", s);
                s = s.substring(s.indexOf("{"));

                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(s).getAsJsonObject();
                final String message = jsonObject.get("message").getAsString();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }
        }

        @Override
        public void onPostExecute(User user) {
            if (user != null) {
                String u = email.getText().toString();
                String p = password.getText().toString();
                SharedPreferencesUtil sharedUtil = new SharedPreferencesUtil(context);
                sharedUtil.save(u, p);
                startActivity(new Intent(context, MainPageActivity.class));
                LoginActivity.this.finish();
            }
        }
    }

}
