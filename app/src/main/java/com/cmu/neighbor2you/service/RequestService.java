package com.cmu.neighbor2you.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.cmu.neighbor2you.util.PropertyUtil;
import com.cmu.newbackend.requestEndpoint.RequestEndpoint;
import com.cmu.newbackend.requestEndpoint.model.Request;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
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
    public Request updateRequest(Request request, Context context) {
        new UpdateRequestAsyncTask(context).execute(request);

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


    private class UpdateRequestAsyncTask extends AsyncTask<Request, Void, Request> {
        private RequestEndpoint myApiService = null;
        private Context context;

        public UpdateRequestAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        public Request doInBackground(Request... params) {
            if (myApiService == null) {
                RequestEndpoint.Builder builder = new RequestEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl(new PropertyUtil(context).getEndPointAddress());
                myApiService = builder.build();
            }

            try {
                return myApiService.updateRequest(params[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(Request request) {
            if (request != null) {
                Toast.makeText(context, "Updated success!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
