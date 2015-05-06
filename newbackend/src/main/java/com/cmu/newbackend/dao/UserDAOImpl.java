package com.cmu.newbackend.dao;

import com.cmu.newbackend.model.Request;
import com.cmu.newbackend.model.User;
import com.cmu.newbackend.util.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.cmu.newbackend.util.DBUtils.getConnection;

/**
 * Created by xing on 4/16/15.
 */
public class UserDAOImpl extends BaseDAOImpl implements IBaseDAO {


    /**
     * Save the User to the database.
     * @param user
     */
    @Override
    public boolean insert(Object user) {

        User userPO = (User)user;
        if (userPO == null) {
            return false;
        }
         try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_USER);
            stmt.setString(1, userPO.getUserName());
            stmt.setString(2, userPO.getPassword());
            stmt.setString(3, userPO.getAddress());
            stmt.setString(4, userPO.getPassword());
            int rowCount = stmt.executeUpdate();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * List all the Users.
     * @return
     * @throws Exception
     */
    @Override
    public List<Object> getAll() throws Exception {
        String query = SQL.FIND_ALL_USERS;
        List<Object> users = new ArrayList<Object>();
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        users = processResults(stmt);
        conn.close();
        return users;
    }

    /**
     * Process the result from the database.
     * @param stmt
     * @return
     * @throws Exception
     */
    private List<Object> processResults(PreparedStatement stmt)
            throws Exception {
        List<Object> users = new ArrayList<Object>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            User po = new User();
            po.setEmail(rs.getString(1));
            po.setUserName(rs.getString(2));
            po.setAddress(rs.getString(3));
            po.setLocation(rs.getString(4));
            po.setPassword(rs.getString(5));
            po.setRatingScore(rs.getInt(6));
            users.add(po);
        }

        return users;
    }

    /**
     * Find the User using name.
     * @param id
     *            - User name to search for.
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object getRequestById(Long id) throws Exception {

        if (id == null) {;
            return null;
        }

        Object po = null;
        Connection conn = getConnection();

        PreparedStatement stmt = conn.prepareStatement(SQL.FIND_USER_BY_NAME);
        stmt.setLong(1, id);

        List<Object> users = processResults(stmt);

        if (users.size() == 0) {
            System.out.print("No user account exists with user id = " + id);
        } else {
            po = users.get(0);
        }

        return po;
    }


    @Override
    public Object update(Object object) {

        User userPO = (User)object;
        if (userPO == null) {
            return false;
        }
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_USER);
            stmt.setString(1, userPO.getUserName());
            stmt.setString(2, userPO.getPassword());
            stmt.setString(3, userPO.getAddress());
            stmt.setString(4, userPO.getPassword());
            int rowCount = stmt.executeUpdate();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }


}
