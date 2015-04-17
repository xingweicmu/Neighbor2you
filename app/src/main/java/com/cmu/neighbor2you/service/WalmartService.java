package com.cmu.neighbor2you.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by xing on 4/16/15.
 */
public class WalmartService extends Service
{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}
