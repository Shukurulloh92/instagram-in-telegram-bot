package com.company.service;

import com.company.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements Base<User> {


    @Override
    public boolean add(User user) {
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement
                        = connection.prepareStatement("select * from add_user(?,?,?,?,?,?)")
        ) {

            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getFullName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getAvatarUrl());
            preparedStatement.setBoolean(6, user.is_log_out());

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getList(String id) {
        List<User> userList = new ArrayList<>();
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {

            ResultSet resultSet;
            if (id != null)
                resultSet = statement.executeQuery("select * from get_user('" + Long.parseLong(id) + "')");
            else
                resultSet = statement.executeQuery("select * from get_user(null)");


            while (resultSet.next()) {
                User user = new User();
                user.get(resultSet);
                userList.add(user);
            }
            return userList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object get(String id) {
        return null;
    }


    public void set(User user) {
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement
                        = connection.prepareStatement("select * from set_user(?,?,?,?,?,?)")
        ) {

            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getFullName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getAvatarUrl());
            preparedStatement.setBoolean(6, user.is_log_out());

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<User> getFollowerList(long userId) {
        return null;
    }

    public List<User> getFolloweeList(long userId) {
        return null;
    }

    public String checkUsername(String text) {

        String sql = "select * from check_username('" + text + "')";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            String str = null;

            while (resultSet.next()) {
                str = resultSet.getString(1);
            }
            return str;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String checkPassword(Long id, String text) {
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {

            ResultSet resultSet = statement.executeQuery("select * from check_password('" + id + "', '" + text + "')");

            String str = null;

            while (resultSet.next()) {
                str = resultSet.getString(1);
            }
            return str;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteAccount(Long id) {
        PhotosService photosService = new PhotosService();
        photosService.deletePhoto(id);
        deleteUserInFollows(id);
        deleteUserInComments(id);
        deleteUserInLikes(id);
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("delete from users where id = " + id + ";")
        ) {

            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUserInFollows(Long id) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("delete from follows " +
                        "where follower_id = " + id + " or followee_id = " + id + ";")
        ) {

            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUserInComments(Long id) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("delete from comments where user_id = " + id + ";")
        ) {

            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUserInLikes(Long id) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("delete from likes where user_id = " + id + ";")
        ) {

            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
