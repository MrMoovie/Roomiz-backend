package com.roomiz.responses;

import com.roomiz.entities.BidEntity;
import com.roomiz.entities.PostEntity;

import java.util.List;

public class ClientPostResponse extends BasicResponse {
    private PostModel post;

    public ClientPostResponse() {
    }

    public ClientPostResponse(boolean success, Integer errorCode, PostEntity postEntity, List<BidEntity> bidEntities) {
        super(success, errorCode);
        this.post = new PostModel(postEntity, bidEntities);
    }

    public PostModel getPost() {
        return post;
    }

    public void setPost(PostModel post) {
        this.post = post;
    }
}
