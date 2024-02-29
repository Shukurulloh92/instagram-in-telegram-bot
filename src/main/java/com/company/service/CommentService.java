package com.company.service;

import com.company.Model.Comments;
import com.company.Model.Photos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentService implements Base<Comments> {
    @Override
    public boolean add(Comments comments) {
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement
                        = connection.prepareStatement("select * from add_comments(?,?,?)")
        ) {

            preparedStatement.setString(1, comments.getCommentText());
            preparedStatement.setLong(2, comments.getUser_id());
            preparedStatement.setInt(3, comments.getPhoto_id());

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Comments> getList(String id) {
        List<Comments> commentsList = new ArrayList<>();
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {

            ResultSet resultSet;
            if (id != null)
                resultSet = statement.executeQuery("select * from get_comments('" + Integer.parseInt(id) + "')");
            else
                resultSet = statement.executeQuery("select * from comments");


            while (resultSet.next()) {
                Comments comments = new Comments();
                comments.get(resultSet);
                commentsList.add(comments);
            }
            return commentsList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object get(String id) {
        return null;
    }

    public void deleteComment(Long id, int photoId) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("delete from comments where user_id = " + id + " and photo_id = " + photoId + ";")
        ) {

            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
