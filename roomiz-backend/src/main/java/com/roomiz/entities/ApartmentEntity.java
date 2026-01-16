package com.roomiz.entities;

import java.util.Set;

public class ApartmentEntity extends BaseEntity{
    private String address;
    private int passcode;

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
