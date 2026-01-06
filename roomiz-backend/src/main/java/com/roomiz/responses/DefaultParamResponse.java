package com.roomiz.responses;

import com.roomiz.entities.BasicUser;

import static com.roomiz.utils.Constants.USER_TYPE_CLIENT;
import static com.roomiz.utils.Constants.USER_TYPE_PROFESSIONAL;

public class DefaultParamResponse extends BasicResponse{
    private int userType;

    public DefaultParamResponse (boolean success, Integer errorCode,
                                 BasicUser basicUser) {
        super(success, errorCode);
        if (basicUser instanceof com.roomiz.entities.ClientEntity) {
            this.userType = USER_TYPE_CLIENT;
        } else if (basicUser instanceof com.roomiz.entities.ProffesionalEntity) {
            this.userType = USER_TYPE_PROFESSIONAL;
        }
    }

    public int getUserType() {
        return userType;
    }
}
