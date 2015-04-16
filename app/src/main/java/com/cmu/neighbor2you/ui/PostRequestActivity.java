package com.cmu.neighbor2you.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmu.backend.requestEndpoint.RequestEndpoint;
import com.cmu.backend.requestEndpoint.model.Request;
import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.model.Product;
import com.cmu.neighbor2you.util.GPSTracker;
import com.cmu.neighbor2you.util.WalmartUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

/**
 * Created by mangobin on 15-4-5.
 */
public class PostRequestActivity extends BaseActivity {
    private EditText itemName;
    private EditText price;
    private GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_request);
        gps = new GPSTracker(this);
        gps.getLocation();
        itemName = (EditText) findViewById(R.id.itemNameEditText);
        price = (EditText) findViewById(R.id.p_priceEdit);
        View btnScan = findViewById(R.id.scanbarcode);

        // Scan button
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.initiateScan(PostRequestActivity.this, R.layout.capture,
                        R.id.viewfinder_view, R.id.preview_view, true);
            }
        });
    }

    public void post(View view) {
        Request request = new Request();
        request.setItemName(itemName.getText().toString());
        request.setItemPrice(Double.parseDouble(price.getText().toString()));
        SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE);
        String requester = null;
        try {
            requester = sharedPrefs.getString("emailKey", "bin@gmail.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setRequester(requester);
        request.setLatitude(gps.getLatitude());
        request.setLongitude(gps.getLongitude());
        Log.v("longitude", String.valueOf(gps.getLatitude()));
        Log.v("latitude", String.valueOf(gps.getLongitude()));
        new PostRequestAsyncTask(this).execute(request);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                final String result = scanResult.getContents();
                if (result != null) {
                    new WalmartAPIRequestAsyncTask(this).execute(result);
                }
                break;
            default:
        }
    }

    private class WalmartAPIRequestAsyncTask extends AsyncTask<String, Void, Product> {
        private Context context;

        public WalmartAPIRequestAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        public Product doInBackground(String... params) {
            WalmartUtil util = new WalmartUtil();
            Product product = util.getItemInfo(params[0]);
            return product;
        }


        @Override
        public void onPostExecute(Product product) {
            if (product != null) {
                price.setText(String.valueOf(product.getPrice()));
                itemName.setText(product.getName());
                Log.v("pppp", product.toString());

            }
        }
    }

    private class PostRequestAsyncTask extends AsyncTask<Request, Void, Request> {
        private RequestEndpoint myApiService = null;
        private Context context;

        public PostRequestAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        public Request doInBackground(Request... params) {
            if (myApiService == null) {
                RequestEndpoint.Builder builder = new RequestEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://n2y-ci-2.appspot.com/_ah/api/");
                myApiService = builder.build();
            }

            try {
                return myApiService.insertRequest(params[0]).execute();
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
