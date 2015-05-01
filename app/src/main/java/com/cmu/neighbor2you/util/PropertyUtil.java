package com.cmu.neighbor2you.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mangobin on 15-4-30.
 */
public class PropertyUtil {

    private Context context;

    public PropertyUtil(Context context) {
        this.context = context;
    }

    public String getEndPointAddress() {
        String fileName = "Endpoint.properties";
        Properties properties = new Properties();

        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            Log.d("fileNotFound", e.getMessage());
        } catch (IOException e) {
            Log.d("IOException", e.getMessage());
        }

        return properties.getProperty("endpoint");
    }
}
