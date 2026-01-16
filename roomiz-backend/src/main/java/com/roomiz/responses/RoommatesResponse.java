package com.roomiz.responses;

import com.roomiz.entities.UserDTO;
import com.roomiz.entities.UserEntity;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.apache.catalina.User;

import java.util.List;
import java.util.Set;

public class RoommatesResponse extends BasicResponse{
    private List<UserDTO> roommates;
    public RoommatesResponse(boolean success, Integer errorCode ,List<UserDTO> roommates){
        super(success, errorCode);
        this.roommates = roommates;
    }

    public List<UserDTO> getRoommates() {
        return roommates;
    }

    public void setRoommates(List<UserDTO> roommates) {
        this.roommates = roommates;
    }
}
