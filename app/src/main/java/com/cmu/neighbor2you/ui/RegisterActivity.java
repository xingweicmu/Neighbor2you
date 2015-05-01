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


/**
 * Created by mangobin on 15-3-29.
 */
public class RegisterActivity extends ActionBarActivity {

    private EditText username, password, password2, email, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        username = (EditText) findViewById(R.id.et_res_username);
        password = (EditText) findViewById(R.id.et_res_Pwd);
        password2 = (EditText) findViewById(R.id.et_res_conPwd);
        email = (EditText) findViewById(R.id.et_res_email);
        address = (EditText) findViewById(R.id.et_res_address);
        phone = (EditText) findViewById(R.id.et_res_phone);

    }

    public void register(View v) {

        String usernameS = username.getText().toString();
        String passwordS1 = password.getText().toString();
        String passwordS2 = password2.getText().toString();
        String emailS = email.getText().toString();
        String addrS = address.getText().toString();
        String phoneS = phone.getText().toString();

        if (isValid(usernameS,passwordS1, passwordS2, emailS, phoneS)) {
            User user = new User();
            user.setUserName(usernameS);
            user.setPassword(passwordS1);
            user.setEmail(emailS);
            user.setAddress(addrS);
            user.setPhoneNumber(phoneS);
            new GcmRegistrationAsyncTask(this, emailS).execute();
            new RegisterAsyncTask(this).execute(user);

        }

    }

    private boolean isValid(String usernameS, String pass1, String pass2, String email, String phone) {
        boolean ret = true;
        if (usernameS.trim().isEmpty()) {
            ret = false;
            Toast.makeText(this, "User name is empty!", Toast.LENGTH_LONG).show();
        } else
        if (pass1.trim().isEmpty() || pass2.trim().isEmpty()) {
            ret = false;
            Toast.makeText(this, "Password is empty!", Toast.LENGTH_LONG).show();
        } else if (!pass1.equals(pass2)) {
            ret = false;
            Toast.makeText(this, "Two passwords don't match!", Toast.LENGTH_LONG).show();
        } else if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")) {
            ret = false;
            Toast.makeText(this, "Email is malformed!", Toast.LENGTH_LONG).show();
        } else if (!phone.matches("\\d+")) {
            ret = false;
            Toast.makeText(this, "Phone number is malformed!", Toast.LENGTH_LONG).show();
        }

        return ret;

    }


    private class RegisterAsyncTask extends AsyncTask<User, Void, User> {
        private UserEndpoint myApiService = null;
        private Context context;

        public RegisterAsyncTask(Context context) {
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
                return myApiService.signUp(params[0]).execute();
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
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }
        }

        @Override
        public void onPostExecute(User result) {
            if (result != null) {
                String u = email.getText().toString();
                String p = password.getText().toString();

                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
                sharedPreferencesUtil.save(u, p);
                Intent it = new Intent();
                it.setClass(RegisterActivity.this, MainPageActivity.class);
                startActivity(it);
                RegisterActivity.this.finish();

            }

        }
    }

}
