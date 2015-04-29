package com.cmu.neighbor2you.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.cmu.backend.requestEndpoint.RequestEndpoint;
//import com.cmu.backend.requestEndpoint.model.Request;
import com.cmu.newbackend.requestEndpoint.RequestEndpoint;
import com.cmu.newbackend.requestEndpoint.model.Request;
import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.util.ImageLoader;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mangobin on 15-4-28.
 */
public class AcceptedRequestListViewAdapter extends BaseAdapter {
    private Activity activity;
    private List<Request> data;
    private static LayoutInflater inflater=null;
    private static Map<String,Integer> map = new HashMap<String,Integer>();
    public ImageLoader imageLoader;

    public AcceptedRequestListViewAdapter(Activity a, List<Request> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        map.put("WAITING",0);
        map.put("STARTED",1);
        map.put("ONTHEWAY",2);
        map.put("ARRIVED",3);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.accepted_requests_list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.accepted_title);
        Spinner status = (Spinner)vi.findViewById(R.id.accepted_status_spinner);
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.accepted_list_image);
        final Request item = data.get(position);

        // Setting all values in listview

        title.setText(item.getItemName());
        imageLoader.DisplayImage(item.getUrl(), thumb_image);
        status.setSelection(map.get(item.getStatus()));

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newStatus = parent.getItemAtPosition(position).toString();
                Log.d("spinner item", newStatus);
                if(!item.getStatus().equals(newStatus)) {
                    item.setStatus(newStatus);
                    new UpdateRequestAsyncTask(activity).execute(item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return vi;
    }

    private class UpdateRequestAsyncTask extends AsyncTask<Request, Void, Request> {
        private RequestEndpoint myApiService = null;
        private Context context;

        public UpdateRequestAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        public Request doInBackground(Request... params) {
            if (myApiService == null) {
                RequestEndpoint.Builder builder = new RequestEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://n2y-ci-8.appspot.com/_ah/api/");
                myApiService = builder.build();
            }

            try {
                return myApiService.updateRequest(params[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(Request request) {
            if (request != null) {
                Toast.makeText(activity, "Updated success!", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
