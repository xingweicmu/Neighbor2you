package com.cmu.neighbor2you.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mangobin on 15-4-30.
 */
public class SharedPreferencesUtil {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String name = "emailKey";
    public static final String pass = "passwordKey";
    public Context context;

    public SharedPreferencesUtil(Context context) {
        this.context = context;
    }

    public void save(String username, String password) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(name, username);
        editor.putString(pass, password);
        editor.commit();

    }

    public void clear() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();

    }

    public String getUserEmail() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        return sharedpreferences.getString(name, null);

    }

    public String getUserPassword() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        return sharedpreferences.getString(pass, null);

    }
}
