package com.roomiz.responses;

import com.roomiz.entities.ApartmentEntity;

public class ApartmentResponse extends BasicResponse {
    private int passcode;
    private String address;
    public ApartmentResponse(boolean success,Integer errorCode , ApartmentEntity apartment){
        super(success, errorCode);
        this.passcode = apartment.getPasscode();
        this.address=apartment.getAddress();
    }

    public int getPasscode() {
        return passcode;
    }

    public void setPasscode(int passcode) {
        this.passcode = passcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
