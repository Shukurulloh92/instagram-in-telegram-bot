package com.company.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Base {
    private Long id;
    private String username;
    private String fullName;
    private String password;
    private Date createdAt;
    private String avatarUrl;
    private boolean is_log_out;
    private String status;

    @Override
    public void get(ResultSet resultSet) {
        try {
            this.id = resultSet.getLong("id");
            this.username = resultSet.getString("username");
            this.fullName = resultSet.getString("full_name");
            this.password = resultSet.getString("password");
            this.avatarUrl = resultSet.getString("avatar_url");
            this.is_log_out = resultSet.getBoolean("is_log_out");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
