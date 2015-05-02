package com.cmu.newbackend.dao;

import com.cmu.newbackend.model.Request;
import com.cmu.newbackend.model.User;
import com.cmu.newbackend.util.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.cmu.newbackend.util.DBUtils.getConnection;

/**
 * Created by xing on 4/16/15.
 */
public class RequestDAOImpl implements IRequestDAO {

    /**
     * Insert a Request to the database.
     * @param request
     * @return
     */
    @Override
    public boolean insertRequest(Request request) {

        if (request == null) {
            return false;
        }
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_USER);
            stmt.setString(1, request.getItemName());
            stmt.setString(2, request.getAcceptor());
            stmt.setString(3, request.getRequester());
            stmt.setString(4, request.getAddress());
            int rowCount = stmt.executeUpdate();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Delete the request from the database;
     * @param request
     * @return
     */
    @Override
    public boolean deleteRequest(Request request) {

        if (request == null) {
            return false;
        }
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_USER);
            stmt.setString(1, request.getItemName());
            stmt.setString(2, request.getAcceptor());
            stmt.setString(3, request.getRequester());
            stmt.setString(4, request.getAddress());
            int rowCount = stmt.executeUpdate();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Update the request.
     * @param request
     * @return
     */
    @Override
    public Request updateRequest(Request request) {


        if (request == null) {
            return request;
        }
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_USER);
            stmt.setString(1, request.getItemName());
            stmt.setString(2, request.getAcceptor());
            stmt.setString(3, request.getRequester());
            stmt.setString(4, request.getAddress());
            int rowCount = stmt.executeUpdate();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return request;

    }

    /**
     * Process the results from the database.
     * @param stmt
     * @return
     * @throws Exception
     */
    private List<Request> processResults(PreparedStatement stmt)
            throws Exception {
        List<Request> users = new ArrayList<Request>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Request po = new Request();
            po.setItemName(rs.getString(1));
            po.setAcceptor(rs.getString(2));
            po.setRequester(rs.getString(3));
            po.setAddress(rs.getString(4));
            po.setDeadline(rs.getLong(5));
            users.add(po);
        }

        return users;
    }

    /**
     * Get all requests.
     * @return
     */
    @Override
    public List<Request> getRequest() {
        List<Request> requests = new ArrayList<Request>();
        try {
            String query = SQL.FIND_ALL_USERS;
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            requests = processResults(stmt);
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * Get Request by id.
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Request getRequestById(Long id) throws Exception{
        if (id == null) {;
            return null;
        }

        Request po = null;
        Connection conn = getConnection();

        PreparedStatement stmt = conn.prepareStatement(SQL.FIND_USER_BY_NAME);
        stmt.setLong(1, id);

        List<Request> users = processResults(stmt);

        if (users.size() == 0) {
            System.out.print("No request exists with id = " + id);
        } else {
            po = users.get(0);
        }

        return po;
    }
}
