package com.matejko.wownetwork.service;

import com.matejko.wownetwork.persistance.entity.Post;
import com.matejko.wownetwork.persistance.entity.User;

import java.util.Date;
import java.util.UUID;

class TestHelper {
    public static User userEntity() {
        final var user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setNickname(UUID.randomUUID().toString());
        return user;
    }

    public static Post postEntity() {
        final var post = new Post();
        post.setId(UUID.randomUUID().toString());
        post.setUser(userEntity());
        post.setPostDate(new Date());
        post.setMessage(UUID.randomUUID().toString());
        return post;
    }
}