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
import com.cmu.neighbor2you.adapter.AcceptedRequestListViewAdapter;
import com.cmu.neighbor2you.view.XListView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by mangobin on 15-4-8.
 */
public class AcceptedRequestActivity extends BaseActivity implements XListView.IXListViewListener {

    private static XListView listview;
    private static AcceptedRequestListViewAdapter adapter;


    private Handler mHandler;
    private List<Request> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accepted_requests_main);

        listview = (XListView) findViewById(R.id.accepted_request_page_list);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("items1", list.get(position - 1).toString());
                Intent it = new Intent(getApplicationContext(), AcceptedRequestItemDetailActivity.class);
                it.putExtra("id", list.get(position - 1).getId());
                startActivity(it);
            }
        });

        mHandler = new Handler();
        listview.setXListViewListener(this);
        listview.setPullLoadEnable(true);
        new AcceptedRequestsRetrievalAsyncTask(this).execute();

    }

    private class AcceptedRequestsRetrievalAsyncTask extends AsyncTask<Void, Void, CollectionResponseRequest> {
        private RequestEndpoint myApiService = null;
        private Context context;

        public AcceptedRequestsRetrievalAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        public CollectionResponseRequest doInBackground(Void... params) {
            if (myApiService == null) {
                RequestEndpoint.Builder builder = new RequestEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://n2y-ci-8.appspot.com/_ah/api/");
                myApiService = builder.build();
            }

            try {
                SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String acceptor = sharedPrefs.getString("emailKey", "NUll");
                return myApiService.getRequestBasedOnAcceptor(acceptor).execute();
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
                    adapter = new AcceptedRequestListViewAdapter(AcceptedRequestActivity.this, list);
                    listview.setAdapter(adapter);
                }
            }
        }
    }

    private void geneItems() {
        new AcceptedRequestsRetrievalAsyncTask(this).execute();
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
                adapter = new AcceptedRequestListViewAdapter(AcceptedRequestActivity.this, list);
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
