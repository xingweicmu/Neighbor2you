package com.cmu.neighbor2you.ui;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.cmu.backend.requestEndpoint.RequestEndpoint;
import com.cmu.backend.requestEndpoint.model.CollectionResponseRequest;
import com.cmu.backend.requestEndpoint.model.Request;
import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.adapter.MainPageListViewAdapter;
import com.cmu.neighbor2you.util.GPSTracker;
import com.cmu.neighbor2you.view.XListView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;


public class MainPageActivity extends BaseActivity implements XListView.IXListViewListener {

    private static XListView listview;
    private static MainPageListViewAdapter adapter;
    private GPSTracker gps;


    private Handler mHandler;
    private List<Request> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_content);
        gps = new GPSTracker(this);
        gps.getLocation();


        listview = (XListView) findViewById(R.id.list);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("items",list.get(position-1).toString());
                Intent it = new Intent(getApplicationContext(),MainPageItemDetailsActivity.class);
                it.putExtra("id",list.get(position-1).getId());
                startActivity(it);
            }
        });

        mHandler = new Handler();
        listview.setXListViewListener(this);
        listview.setPullLoadEnable(true);

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
                        .setRootUrl("https://n2y-ci-3.appspot.com/_ah/api/");
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
                list = reqList.getItems();
                if (list != null && !list.isEmpty()) {
                    adapter = new MainPageListViewAdapter(MainPageActivity.this, list);
                    listview.setAdapter(adapter);
                }
            }
        }
    }

    private void geneItems() {
        new RequestsRetrievalAsyncTask(this).execute(Double.valueOf(gps.getLatitude()), Double.valueOf(gps.getLongitude()));
    }

    private void onLoad() {
        listview.stopRefresh();
        listview.stopLoadMore();
        listview.setRefreshTime("now");
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                geneItems();
                adapter.notifyDataSetChanged();
                adapter = new MainPageListViewAdapter(MainPageActivity.this, list);
                listview.setAdapter(adapter);
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems();
                adapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }


}