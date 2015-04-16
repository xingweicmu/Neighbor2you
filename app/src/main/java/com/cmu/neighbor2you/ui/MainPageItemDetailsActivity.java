package com.cmu.neighbor2you.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmu.backend.requestEndpoint.RequestEndpoint;
import com.cmu.backend.requestEndpoint.model.Request;
import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.util.ImageLoader;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.Date;

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

        long id = getIntent().getLongExtra("id",0);
        new GetRequestDetailsAsyncTask(this).execute(id);

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
                        .setRootUrl("https://n2y-ci-3.appspot.com/_ah/api/");
                myApiService = builder.build();
            }

            try {
                return myApiService.getRequestById(params[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(Request request) {
            if (request != null) {
                ImageLoader loader = new ImageLoader(this.context);
                loader.DisplayImage(request.getUrl(),image);
                itemName.setText(request.getItemName());
                price.setText(String.valueOf(request.getItemPrice()));
                address.setText(request.getAddress());
                phone.setText(request.getPhoneNumber());
                deadline.setText(new Date(request.getDeadline()).toString());
                poster.setText(request.getRequester());
            }
        }
    }



}
