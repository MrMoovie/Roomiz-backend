package com.roomiz.responses;

public class IsAdminResponse extends BasicResponse{
    private boolean isAdmin;
    public IsAdminResponse(boolean success, Integer errorCode, boolean isAdmin){
        super(success, errorCode);
        this.isAdmin= isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
