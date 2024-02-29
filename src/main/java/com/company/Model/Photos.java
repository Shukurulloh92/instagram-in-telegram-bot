package com.company.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Photos implements Base{
    private int id;
    private String imageUrl;
    private Long user_id;
    private Date createdAt;

    @Override
    public void get(ResultSet resultSet) {
        try {
            this.id = resultSet.getInt("id");
            this.imageUrl = resultSet.getString("image_url");
            this.user_id = resultSet.getLong("user_id");
            this.createdAt = resultSet.getDate("created_at");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
