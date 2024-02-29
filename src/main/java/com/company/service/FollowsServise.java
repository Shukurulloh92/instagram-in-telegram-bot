package com.company.service;

import com.company.Model.Follows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class FollowsServise implements Base<Follows>{

    public int getFollowerCount(long id) {
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {

            ResultSet resultSet = statement.executeQuery("select * from get_follower('" + id + "')");

            int follower = 0;
            while (resultSet.next()) {
                follower = resultSet.getInt(1);
            }
            return follower;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getFolloweeCount(long id) {
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {

            ResultSet resultSet = statement.executeQuery("select * from get_followee('" + id + "')");

            int followee = 0;
            while (resultSet.next()) {
                followee = resultSet.getInt(1);
            }
            return followee;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    public boolean add(Follows follows) {
        return false;
    }

    @Override
    public List<Follows> getList(String id) {
        return null;
    }

    @Override
    public Object get(String id) {
        return null;
    }
}
