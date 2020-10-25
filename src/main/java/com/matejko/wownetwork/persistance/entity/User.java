package com.matejko.wownetwork.persistance.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany
    private List<User> followedUsers = new ArrayList<>();

    public void addToPosts(final Post post) {
        post.setUser(this);
        this.posts.add(post);
    }

    public void addToFollowedUsers(final User user) {
        this.followedUsers.add(user);
    }
}
