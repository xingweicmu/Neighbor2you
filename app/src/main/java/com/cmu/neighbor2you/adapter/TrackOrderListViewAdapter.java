package com.cmu.neighbor2you.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmu.neighbor2you.R;
import com.cmu.newbackend.requestEndpoint.model.Request;

import java.util.List;

/**
 * Created by mangobin on 15-4-29.
 */
public class TrackOrderListViewAdapter extends BaseAdapter {

    private Activity activity;
    private List<Request> data;
    private static LayoutInflater inflater=null;

    public TrackOrderListViewAdapter(Activity a, List<Request> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            vi = inflater.inflate(R.layout.order_status_list, null);

        TextView status = (TextView)vi.findViewById(R.id.order_status);
        ImageView statusIcon = (ImageView)vi.findViewById(R.id.order_status_icon);
        Request item = data.get(position);

        // Setting all values in listview

        String curStatus = item.getStatus();
        status.setText(curStatus);
        if (curStatus.equals("STARTED")) {
            statusIcon.setImageResource(R.drawable.start);
        } else if (curStatus.equals("ONTHEWAY")) {
            statusIcon.setImageResource(R.drawable.going);
        } else if (curStatus.equals("ARRIVED")) {
            statusIcon.setImageResource(R.drawable.arrive);
        } else if (curStatus.equals("WAITING")) {
            statusIcon.setImageResource(R.drawable.waiting);
        }

        return vi;
    }
}
