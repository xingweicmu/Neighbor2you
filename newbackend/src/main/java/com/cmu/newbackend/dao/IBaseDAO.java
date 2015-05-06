package com.cmu.newbackend.dao;

import com.cmu.newbackend.model.Request;

import java.util.List;

/**
 * Created by xing on 5/5/15.
 */
public interface IBaseDAO {

    public boolean insert(Object object);
    public Object update(Object object);
    public List<Object> getAll() throws Exception;
    public Object getRequestById(Long id) throws Exception;

}
