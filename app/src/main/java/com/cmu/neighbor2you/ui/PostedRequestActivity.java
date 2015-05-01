package com.cmu.neighbor2you.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.adapter.PostedRequestListViewAdapter;
import com.cmu.neighbor2you.util.PropertyUtil;
import com.cmu.neighbor2you.util.SharedPreferencesUtil;
import com.cmu.neighbor2you.view.XListView;
import com.cmu.newbackend.requestEndpoint.RequestEndpoint;
import com.cmu.newbackend.requestEndpoint.model.CollectionResponseRequest;
import com.cmu.newbackend.requestEndpoint.model.Request;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

//import com.cmu.backend.requestEndpoint.RequestEndpoint;
//import com.cmu.backend.requestEndpoint.model.CollectionResponseRequest;
//import com.cmu.backend.requestEndpoint.model.Request;

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
        setContentView(R.layout.posted_requests_main);

        listview = (XListView) findViewById(R.id.posted_request_page_list);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("items", list.get(position - 1).toString());
                Intent it = new Intent(getApplicationContext(), TrackOrderActivity.class);
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
                        .setRootUrl(new PropertyUtil(context).getEndPointAddress());
                myApiService = builder.build();
            }

            try {
                String requester = new SharedPreferencesUtil(context).getUserEmail();
                return myApiService.getRequestBasedOnRequester(requester).execute();
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
                    adapter = new PostedRequestListViewAdapter(PostedRequestActivity.this, list);
                    listview.setAdapter(adapter);
                }
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
