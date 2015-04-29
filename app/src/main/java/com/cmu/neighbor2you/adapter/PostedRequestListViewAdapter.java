package com.cmu.neighbor2you.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.cmu.backend.requestEndpoint.model.Request;
//import com.cmu.newbackend.request.model.Request;
import com.cmu.newbackend.requestEndpoint.model.Request;
import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.util.ImageLoader;

import java.util.List;

/**
 * Created by mangobin on 15-4-27.
 */
public class PostedRequestListViewAdapter extends BaseAdapter {
    private Activity activity;
    private List<Request> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public PostedRequestListViewAdapter(Activity a, List<Request> d) {
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
            vi = inflater.inflate(R.layout.posted_requests_list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.posted_title);
        TextView status = (TextView)vi.findViewById(R.id.edit_posted_status);
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.posted_list_image);
        Request item = data.get(position);

        // Setting all values in listview

        title.setText(item.getItemName());
        status.setText(item.getStatus());
        imageLoader.DisplayImage(item.getUrl(), thumb_image);
        return vi;
    }
}
