package com.cmu.neighbor2you.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmu.backend.userEndpoint.UserEndpoint;
import com.cmu.backend.userEndpoint.model.User;
import com.cmu.neighbor2you.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;


public class LoginActivity extends ActionBarActivity {

    private EditText email, password;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String name = "emailKey";
    public static final String pass = "passwordKey";
    private String message;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = (EditText) findViewById(R.id.et_Email);
        password = (EditText) findViewById(R.id.et_Pwd);

    }

    @Override
    protected void onResume() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(name)) {
            if (sharedpreferences.contains(pass)) {
                startActivity(new Intent(this, MainPageActivity.class));
            }
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
            new GcmRegistrationAsyncTask(this).execute();
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
                            .setRootUrl("https://n2y-ci-7.appspot.com/_ah/api/");
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
                message = jsonObject.get("message").getAsString();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }
        }

        @Override
        public void onPostExecute(User user) {
            if (user != null) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                String u = email.getText().toString();
                String p = password.getText().toString();
                editor.putString(name, u);
                editor.putString(pass, p);
                editor.commit();
                startActivity(new Intent(this.context, MainPageActivity.class));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
