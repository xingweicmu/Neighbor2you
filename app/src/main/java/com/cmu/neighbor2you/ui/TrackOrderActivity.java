package com.cmu.neighbor2you.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.adapter.TrackOrderListViewAdapter;
import com.cmu.neighbor2you.service.IRequestService;
import com.cmu.neighbor2you.service.IUserService;
import com.cmu.neighbor2you.service.RequestService;
import com.cmu.neighbor2you.service.UserService;
import com.cmu.neighbor2you.util.PropertyUtil;
import com.cmu.newbackend.requestEndpoint.RequestEndpoint;
import com.cmu.newbackend.requestEndpoint.model.Request;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Venno on 4/11/15.
 */
public class TrackOrderActivity extends BaseActivity {

    private TextView buyer;
    private ListView listView;
    private Request req;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posted_requests_track_order);
        buyer = (TextView) findViewById(R.id.track_order_buyer);
        listView = (ListView) findViewById(R.id.status_list);

        long id = getIntent().getLongExtra("id", 0);
        new GetRequestDetailsAsyncTask(this).execute(id);

    }

    public void itemReceived(View view) {

        //update request
        IRequestService requestService = new RequestService();
        req.setStatus("ARRIVED");
        requestService.updateRequest(req, this);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_rating);
        dialog.setTitle("Rating");

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                //update rating
                IUserService userService = new UserService();
                userService.updateUserRating(TrackOrderActivity.this, req.getAcceptor(), String.valueOf(ratingBar.getRating()));
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    public void dial(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "6504178096"));
        startActivity(callIntent);
    }

    private class GetRequestDetailsAsyncTask extends AsyncTask<Long, Void, Request> {
        private RequestEndpoint myApiService = null;
        private Context context;

        public GetRequestDetailsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TrackOrderActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading product Detail..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        public Request doInBackground(Long... params) {
            if (myApiService == null) {
                RequestEndpoint.Builder builder = new RequestEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl(new PropertyUtil(context).getEndPointAddress());
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
            pDialog.dismiss();
            if (request != null) {
                req = request;
                List<Request> list = new ArrayList<Request>();
                list.add(request);
                TrackOrderListViewAdapter adapter;
                adapter = new TrackOrderListViewAdapter(TrackOrderActivity.this, list);
                listView.setAdapter(adapter);
                buyer.setText(request.getAcceptor());
                getActionBar().setTitle(req.getItemName());
            }
        }
    }

}
