package com.cmu.neighbor2you.service;


import android.content.Context;

import com.cmu.newbackend.requestEndpoint.model.Request;

import java.util.List;

/**
 * Created by xing on 4/17/15.
 */
public interface IRequestService {
    public boolean insertRequest(Request request);
    public boolean deleteRequest(Request request);
    public Request updateRequest(Request request, Context context);
    public List<Request> getRequest();
    public Request getRequestById();
}
