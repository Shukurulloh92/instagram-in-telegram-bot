package com.company.service;

import com.company.Model.Photos;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhotosService implements Base<Photos> {

    public void deletePhoto(Long id) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("delete from photos where user_id = " + id + ";")
        ) {

            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add(Photos photos) {
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement
                        = connection.prepareStatement("select * from add_photos(?,?)")
        ) {

            preparedStatement.setString(1, photos.getImageUrl());
            preparedStatement.setLong(2, photos.getUser_id());

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Photos> getList(String id) {
        List<Photos> photoList = new ArrayList<>();
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {

            ResultSet resultSet;
            if (id != null)
                resultSet = statement.executeQuery("select * from get_photos('" + id + "')");
            else
                resultSet = statement.executeQuery("select * from photos");


            while (resultSet.next()) {
                Photos photos = new Photos();
                photos.get(resultSet);
                photoList.add(photos);
            }
            return photoList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Photos get(String id) {
        Photos photos = new Photos();
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {

            ResultSet resultSet = statement.executeQuery("select * from photos where id = " + Integer.parseInt(id));

            while (resultSet.next()) {
                photos.get(resultSet);
            }
            return photos;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getComments(int id) {
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery("select * from get_comments('" + id + "')");

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
}
