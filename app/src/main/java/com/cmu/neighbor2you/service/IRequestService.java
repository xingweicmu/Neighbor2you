package com.cmu.neighbor2you.service;

import com.cmu.neighbor2you.model.Request;

import java.util.List;

/**
 * Created by xing on 4/17/15.
 */
public interface IRequestService {
    public boolean insertRequest(Request request);
    public boolean deleteRequest(Request request);
    public Request updateRequest(Request request);
    public List<Request> getRequest();
    public Request getRequestById();
}
