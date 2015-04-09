package com.cmu.neighbor2you.ui;

import android.os.Bundle;
import android.util.Log;

import com.cmu.neighbor2you.R;

/**
 * Created by mangobin on 15-4-5.
 */
public class PostRequestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        Log.v("gggt","postrequest");
        setContentView(R.layout.activity_post_request);
    }
}
