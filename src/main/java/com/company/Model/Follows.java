package com.company.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Follows {
    private Long userId;
    private List<User> follower;
    private List<User> followee;
}
