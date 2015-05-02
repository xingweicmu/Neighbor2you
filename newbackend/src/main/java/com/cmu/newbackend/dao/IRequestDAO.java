package com.cmu.newbackend.dao;

import com.cmu.newbackend.model.Request;

import java.util.List;

/**
 * Created by xing on 4/16/15.
 */
public interface IRequestDAO {

    public boolean insertRequest(Request request);
    public boolean deleteRequest(Request request);
    public Request updateRequest(Request request);
    public List<Request> getRequest();
    public Request getRequestById(Long id) throws Exception;
}
