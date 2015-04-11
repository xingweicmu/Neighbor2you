package com.cmu.neighbor2you.ui;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cmu.backend.requestEndpoint.RequestEndpoint;
import com.cmu.backend.requestEndpoint.model.CollectionResponseRequest;
import com.cmu.backend.requestEndpoint.model.Request;
import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.adapter.MainPageListViewAdapter;
import com.cmu.neighbor2you.util.GPSTracker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;


public class MainPageActivity extends BaseActivity {

    private static ListView listview;
    private static MainPageListViewAdapter adapter;
    private GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_content);
        gps = new GPSTracker(this);
        gps.getLocation();

        listview = (ListView) findViewById(R.id.list);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        new RequestsRetrievalAsyncTask(this).execute(Double.valueOf(gps.getLatitude()), Double.valueOf(gps.getLongitude()));

    }

    private class RequestsRetrievalAsyncTask extends AsyncTask<Double, Void, CollectionResponseRequest> {
        private RequestEndpoint myApiService = null;
        private Context context;

        public RequestsRetrievalAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        public CollectionResponseRequest doInBackground(Double... params) {
            if (myApiService == null) {
                RequestEndpoint.Builder builder = new RequestEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://n2y-ci.appspot.com/_ah/api/");
                myApiService = builder.build();
            }

            try {
                return myApiService.getRequest(params[0], params[1]).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(CollectionResponseRequest reqList) {
            if (reqList != null) {
                List<Request> list = reqList.getItems();
                if (list != null && !list.isEmpty()) {
                    adapter = new MainPageListViewAdapter(MainPageActivity.this, list);
                    listview.setAdapter(adapter);
                }
            }
        }
    }


}