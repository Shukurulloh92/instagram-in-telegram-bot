package com.company.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public interface Base <T>{

    default Connection getConnection(){
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/instagram",
                    "postgres",
                    "root123"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    boolean add(T t);
    List<T> getList(String id);
    Object get(String id);
}
