package com.roomiz.entities;

import org.apache.catalina.User;

public class UserDTO {
    private String username;
    public UserDTO(UserEntity user){
        username = user.getUsername();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
