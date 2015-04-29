package com.cmu.neighbor2you.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.cmu.backend.requestEndpoint.RequestEndpoint;
//import com.cmu.backend.requestEndpoint.model.Request;
import com.cmu.newbackend.requestEndpoint.RequestEndpoint;
import com.cmu.newbackend.requestEndpoint.model.Request;
import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.model.Product;
import com.cmu.neighbor2you.util.GPSTracker;
import com.cmu.neighbor2you.util.ImageLoader;
import com.cmu.neighbor2you.util.TimestampUtil;
import com.cmu.neighbor2you.util.WalmartUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.Date;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

/**
 * Created by mangobin on 15-4-5.
 */
public class PostRequestActivity extends BaseActivity {
    private EditText itemName;
    private EditText price;
    private EditText address;
    private EditText phone;
    private EditText time;

    private ImageView imageView;
    private TimePicker tp;
    private DatePicker dp;
    private ProgressDialog pDialog;

    private GPSTracker gps;
    private Request request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_request);
        gps = new GPSTracker(this);
        gps.getLocation();
        itemName = (EditText) findViewById(R.id.itemNameEditText);
        price = (EditText) findViewById(R.id.p_priceEdit);
        address = (EditText) findViewById(R.id.p_addressEdit);
        phone = (EditText) findViewById(R.id.p_phoneEdit);
        imageView = (ImageView) findViewById(R.id.posted_image);
        time = (EditText) findViewById(R.id.p_dueEdit);
        View btnScan = findViewById(R.id.posted_image);
        request = new Request();

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
        request.setItemName(itemName.getText().toString());
        if (!price.getText().toString().isEmpty()) {
            request.setItemPrice(Double.parseDouble(price.getText().toString()));
        }
        // need furtherwork to check if it is null
        request.setAddress(address.getText().toString());
        request.setPhoneNumber(phone.getText().toString());

        SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE);
        String requester = null;
        try {
            requester = sharedPrefs.getString("emailKey", "NUll");
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setRequester(requester);
        request.setLatitude(gps.getLatitude());
        request.setLongitude(gps.getLongitude());
        Date date = TimestampUtil.convert(time.getText().toString());
        if (date != null) {
            request.setDeadline(date.getTime());
        }
        if (request.getItemName().isEmpty()) {
            Toast.makeText(this, "Item name is empty!", Toast.LENGTH_SHORT).show();
        } else {
            new PostRequestAsyncTask(this).execute(request);
        }

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
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PostRequestActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading product Detail..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        public Product doInBackground(String... params) {
            WalmartUtil util = new WalmartUtil();
            Product product = util.getItemInfo(params[0]);
            return product;
        }


        @Override
        public void onPostExecute(Product product) {
            pDialog.dismiss();

            if (product != null) {
                price.setText(String.valueOf(product.getPrice()));
                itemName.setText(product.getName());
                ImageLoader loader = new ImageLoader(this.context);
                loader.DisplayImage(product.getThumbnailImage(), imageView);
                request.setUrl(product.getThumbnailImage());
                Log.v("pppp", product.toString());

            } else {
                Toast.makeText(getBaseContext(), "This product information is not available!", Toast.LENGTH_LONG).show();
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
                        .setRootUrl("https://n2y-ci-new.appspot.com/_ah/api/");
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

    public void showPickerDialog(View v) {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.date_time_layout);

        dialog.setTitle("Deadline");
        Button ok = (Button) dialog.findViewById(R.id.getTime);
        Button cancel = (Button) dialog.findViewById(R.id.conceal);
        tp = (TimePicker) dialog.findViewById(R.id.timePicker);
        dp = (DatePicker) dialog.findViewById(R.id.datePicker);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strDateTime = (dp.getMonth() + 1) + "/" + dp.getDayOfMonth() + "/" + dp.getYear() + " " + tp.getCurrentHour() + ":" + tp.getCurrentMinute();
                Log.v("time", strDateTime);
                time.setText(strDateTime);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
