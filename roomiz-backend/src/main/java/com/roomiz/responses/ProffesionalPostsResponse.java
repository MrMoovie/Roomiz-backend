package com.roomiz.responses;

import com.roomiz.entities.PostEntity;

import java.util.List;

public class ProffesionalPostsResponse extends BasicResponse {
    private List<PostModel> posts;

    public ProffesionalPostsResponse() {
    }

    public ProffesionalPostsResponse(boolean success, Integer errorCode, List<PostEntity> posts) {
        super(success, errorCode);
        this.posts = posts.stream().map(item -> {
            return new PostModel(item, null);
        }).toList();
    }


    public List<PostModel> getPosts() {
        return posts;
    }

    public void setPosts(List<PostModel> posts) {
        this.posts = posts;
    }
}
