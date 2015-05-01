package com.cmu.neighbor2you.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.util.ImageLoader;
import com.cmu.neighbor2you.util.PropertyUtil;
import com.cmu.neighbor2you.util.TimestampUtil;
import com.cmu.newbackend.requestEndpoint.RequestEndpoint;
import com.cmu.newbackend.requestEndpoint.model.Request;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.Date;

//import com.cmu.backend.requestEndpoint.RequestEndpoint;
//import com.cmu.backend.requestEndpoint.model.Request;

/**
 * Created by mangobin on 15-4-28.
 */
public class AcceptedRequestItemDetailActivity extends BaseActivity {
    private TextView itemName;
    private TextView price;
    private TextView address;
    //private TextView phone;
    private TextView deadline;
    private TextView poster;
    private ImageView image;
    private Request req;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accepted_requests_detail);

        itemName = (TextView) findViewById(R.id.ac_itemName);
        price = (TextView) findViewById(R.id.ac_price);
        address = (TextView) findViewById(R.id.ac_address);
        // phone = (TextView)findViewById(R.id.ac_phone);
        deadline = (TextView) findViewById(R.id.ac_due);
        poster = (TextView) findViewById(R.id.ac_needer);
        image = (ImageView) findViewById(R.id.ac_image);

        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);

        long id = getIntent().getLongExtra("id", 0);
        new GetRequestDetailsAsyncTask(this).execute(id);

    }

    public void dialRequester(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"6504178096"));
        startActivity(callIntent);
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
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
            pDialog = new ProgressDialog(AcceptedRequestItemDetailActivity.this);
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
                ImageLoader loader = new ImageLoader(this.context);
                loader.DisplayImage(request.getUrl(), image);
                itemName.setText(request.getItemName());
                price.setText(String.valueOf(request.getItemPrice()));
                address.setText(request.getAddress());
                // phone.setText(request.getPhoneNumber());

                deadline.setText(TimestampUtil.convert(new Date(request.getDeadline())));
                poster.setText(request.getRequester());
            }
        }
    }
}
