package com.roomiz.responses;

public class LoginResponse extends BasicResponse {
    private String token;
    private int id;

    public LoginResponse(boolean success, Integer errorCode, String token, int id) {
        super(success, errorCode);
        this.token = token;
        this.id = id;
    }
    public  LoginResponse(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
