package com.cmu.neighbor2you.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmu.backend.requestEndpoint.model.Request;
import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.util.ImageLoader;

import java.util.List;

/**
 * Created by mangobin on 15-4-28.
 */
public class AcceptedRequestListViewAdapter extends BaseAdapter {
    private Activity activity;
    private List<Request> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public AcceptedRequestListViewAdapter(Activity a, List<Request> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
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
        Request item = data.get(position);

        // Setting all values in listview

        title.setText(item.getItemName());
        imageLoader.DisplayImage(item.getUrl(), thumb_image);
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newStatus = parent.getItemAtPosition(position).toString();
                Log.d("spinner item", newStatus);
                //item.setStatus(newStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return vi;
    }



}
