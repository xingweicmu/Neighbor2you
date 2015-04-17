package com.cmu.neighbor2you.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.cmu.neighbor2you.model.Request;

import java.util.List;

/**
 * Created by xing on 4/17/15.
 */
public class RequestService extends Service implements IRequestService {
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

    @Override
    public boolean insertRequest(Request request) {
        return false;
    }

    @Override
    public boolean deleteRequest(Request request) {
        return false;
    }

    @Override
    public Request updateRequest(Request request) {
        return null;
    }

    @Override
    public List<Request> getRequest() {
        return null;
    }

    @Override
    public Request getRequestById() {
        return null;
    }
}
