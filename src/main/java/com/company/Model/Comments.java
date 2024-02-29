package com.company.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Comments implements Base {
    private Integer id;
    private String commentText;
    private long user_id;
    private int photo_id;
    private Date createdAt;


    @Override
    public void get(ResultSet resultSet) {
        try {
            this.id = resultSet.getInt("id");
            this.commentText = resultSet.getString("comment_text");
            this.user_id = resultSet.getLong("user_id");
            this.photo_id = resultSet.getInt("photo_id");
            this.createdAt = resultSet.getDate("created_at");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
