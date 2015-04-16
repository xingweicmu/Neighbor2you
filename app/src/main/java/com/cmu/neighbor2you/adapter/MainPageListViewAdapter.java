package com.cmu.neighbor2you.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmu.backend.requestEndpoint.model.Request;
import com.cmu.neighbor2you.R;

import java.util.List;

/**
 * Created by mangobin on 15-4-4.
 */
public class MainPageListViewAdapter extends BaseAdapter {

    private Activity activity;
    private List<Request> data;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader;

    public MainPageListViewAdapter(Activity a, List<Request> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // imageLoader=new ImageLoader(activity.getApplicationContext());
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
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView poster = (TextView)vi.findViewById(R.id.poster); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        TextView distance = (TextView)vi.findViewById(R.id.distance);
        Request item = data.get(position);

        // Setting all values in listview

        title.setText(item.getItemName());
        poster.setText("Posted by " + item.getRequester());
        duration.setText(String.valueOf(item.getDeadline()));
        distance.setText(String.valueOf(item.getDistance()).substring(0, 4) + " m");
      //  imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}
