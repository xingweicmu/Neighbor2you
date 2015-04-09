package com.cmu.neighbor2you.ui;

import android.os.Bundle;
import android.util.Log;

import com.cmu.neighbor2you.R;

/**
 * Created by mangobin on 15-4-6.
 */
public class PostedRequestActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("ttt", "postedrequest");
        setContentView(R.layout.activity_posted_requests);
    }
}
