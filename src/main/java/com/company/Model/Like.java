package com.company.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Like implements Base{
    private Long userId;
    private Integer photosId;

    @Override
    public void get(ResultSet resultSet) {
        try {
            this.userId = resultSet.getLong("user_id");
            this.photosId = resultSet.getInt("photos_id");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
