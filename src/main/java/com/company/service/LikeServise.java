package com.company.service;

import com.company.Model.Like;

import java.sql.*;
import java.util.List;

public class LikeServise implements Base<Like> {

    @Override
    public boolean add(Like like) {
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement
                        = connection.prepareStatement("select * from add_or_dislikes" +
                        "('" + like.getUserId() + "', '" + like.getPhotosId() + "')")

        ) {

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Like> getList(String id) {
        return null;
    }

    public int getLike(int id) {
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {

            ResultSet resultSet = statement.executeQuery("select * from get_likes('" + id + "')");

            int like = 0;
            while (resultSet.next()) {
                like = resultSet.getInt(1);
            }
            return like;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object get(String id) {
        return null;
    }
}
