package com.cmu.neighbor2you.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.cmu.neighbor2you.adapter.PostedRequestListViewAdapter;
import com.cmu.neighbor2you.view.XListView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by mangobin on 15-4-6.
 */
public class PostedRequestActivity extends BaseActivity implements XListView.IXListViewListener {

    private static XListView listview;
    private static PostedRequestListViewAdapter adapter;


    private Handler mHandler;
    private List<Request> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_content);

        listview = (XListView) findViewById(R.id.list);

        /**
         * to modify
         */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("items", list.get(position - 1).toString());
                Intent it = new Intent(getApplicationContext(), MainPageItemDetailsActivity.class);
                it.putExtra("id", list.get(position - 1).getId());
                startActivity(it);
            }
        });

        mHandler = new Handler();
        listview.setXListViewListener(this);
        listview.setPullLoadEnable(true);
        new PostedRequestsRetrievalAsyncTask(this).execute();
    }


    private class PostedRequestsRetrievalAsyncTask extends AsyncTask<Void, Void, CollectionResponseRequest> {
        private RequestEndpoint myApiService = null;
        private Context context;

        public PostedRequestsRetrievalAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        public CollectionResponseRequest doInBackground(Void... params) {
            if (myApiService == null) {
                RequestEndpoint.Builder builder = new RequestEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://n2y-ci-7.appspot.com/_ah/api/");
                myApiService = builder.build();
            }

            try {
                SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String requester = sharedPrefs.getString("emailKey", "NUll");
                return myApiService.getRequestBasedOnRequester(requester).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(CollectionResponseRequest reqList) {
            Log.d("main1", "111");
            if (reqList != null) {
                list = reqList.getItems();
                Log.d("main2", "111");
                if (list != null && !list.isEmpty()) {
                    Log.d("main3", "main");
                    adapter = new PostedRequestListViewAdapter(PostedRequestActivity.this, list);
                    listview.setAdapter(adapter);
                } else {
                    Log.d("main4", "main");
                }
            }
            if (reqList == null) {
                Log.d("main5", "111");
            }
        }
    }

    private void geneItems() {
        new PostedRequestsRetrievalAsyncTask(this).execute();
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
                adapter = new PostedRequestListViewAdapter(PostedRequestActivity.this, list);
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
