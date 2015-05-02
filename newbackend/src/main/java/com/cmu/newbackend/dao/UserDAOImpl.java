package com.cmu.newbackend.dao;

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
public class UserDAOImpl implements  IUserDAO {


    /**
     * Save the User to the database.
     * @param userPO
     */
    @Override
    public void save(User userPO) {

        if (userPO == null) {
            return;
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
    }

    /**
     * List all the Users.
     * @return
     * @throws Exception
     */
    @Override
    public List<User> loadUsers() throws Exception {
        String query = SQL.FIND_ALL_USERS;
        List<User> users = new ArrayList<User>();
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
    private List<User> processResults(PreparedStatement stmt)
            throws Exception {
        List<User> users = new ArrayList<User>();
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
     * @param userName
     *            - User name to search for.
     *
     * @return
     * @throws Exception
     */
    @Override
    public User findByName(String userName) throws Exception {

        if (userName == null) {;
            return null;
        }

        User po = null;
        Connection conn = getConnection();

        PreparedStatement stmt = conn.prepareStatement(SQL.FIND_USER_BY_NAME);
        stmt.setString(1, userName.toUpperCase());

        List<User> users = processResults(stmt);

        if (users.size() == 0) {
            System.out.print("No user account exists with userName = " + userName);
        } else {
            po = users.get(0);
        }

        return po;
    }


}
