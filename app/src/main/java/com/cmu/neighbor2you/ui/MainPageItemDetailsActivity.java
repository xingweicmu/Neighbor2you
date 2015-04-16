package com.cmu.neighbor2you.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.backend.requestEndpoint.RequestEndpoint;
import com.cmu.backend.requestEndpoint.model.Request;
import com.cmu.neighbor2you.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by mangobin on 15-4-16.
 */
public class MainPageItemDetailsActivity extends BaseActivity {

    private TextView itemName;
    private TextView price;
    private TextView address;
    private TextView phone;
    private TextView deadline;
    private TextView poster;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        itemName = (TextView) findViewById(R.id.ac_itemName);
        price = (TextView) findViewById(R.id.ac_price);
        address = (TextView)findViewById(R.id.ac_address);
        phone = (TextView)findViewById(R.id.ac_phone);
        deadline = (TextView)findViewById(R.id.ac_due);
        poster = (TextView)findViewById(R.id.ac_needer);
        image = (ImageView)findViewById(R.id.ac_image);

    }

    private class GetRequestDetailsAsyncTask extends AsyncTask<Long, Void, Request> {
        private RequestEndpoint myApiService = null;
        private Context context;

        public GetRequestDetailsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        public Request doInBackground(Long... params) {
            if (myApiService == null) {
                RequestEndpoint.Builder builder = new RequestEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://n2y-ci-2.appspot.com/_ah/api/");
                myApiService = builder.build();
            }

            try {
                return myApiService;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(Request request) {
            if (request != null) {
                Toast.makeText(getApplicationContext(), "Post success!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this.context, MainPageActivity.class));
            }
        }
    }



}
