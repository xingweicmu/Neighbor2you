package com.cmu.neighbor2you.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.cmu.neighbor2you.R;
import com.cmu.neighbor2you.service.UserService;
import com.cmu.neighbor2you.util.SharedPreferencesUtil;
import com.cmu.newbackend.userEndpoint.model.User;

/**
 * Created by mangobin on 15-4-8.
 */
public class ProfileActivity extends BaseActivity {

    private EditText username, phone, address;
    private RatingBar ratingBar;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = (EditText) findViewById(R.id.profile_name);
        phone = (EditText) findViewById(R.id.profile_phone);
        address = (EditText) findViewById(R.id.profile_addr);
        ratingBar = (RatingBar) findViewById(R.id.profile_ratingBar);

        //get user info to populate phone and address fields
        new UserService().findByName(this, new SharedPreferencesUtil(this).getUserEmail());
    }

    public void updateUIBasedOnUser(User user) {
        this.user = user;
        Log.d("updateUI", "profile");
        username.setText(user.getUserName());
        address.setText(user.getAddress());
        phone.setText(user.getPhoneNumber());
        ratingBar.setRating(Float.parseFloat(String.valueOf(user.getRatingScore())));
    }

    public void updateProfile(View view) {
        if (user != null) {
            user.setAddress(address.getText().toString());
            user.setPhoneNumber(phone.getText().toString());
            user.setUserName(username.getText().toString());
            new UserService().save(this,user);
        }

    }


}
