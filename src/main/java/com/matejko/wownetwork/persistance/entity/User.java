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
    /**
     * Identifier in database
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    /**
     * Nickname of the user
     */
    @Column(nullable = false, unique = true)
    private String nickname;

    /**
     * Posts that user has added
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    /**
     * Users that are being followed
     */
    @OneToMany
    private List<User> followedUsers = new ArrayList<>();

    /**
     * Adds given posts to posts, creates bi-directional relation
     *
     * @param post post to be added
     */
    public void addToPosts(final Post post) {
        post.setUser(this);
        this.posts.add(post);
    }

    /**
     * Adds given user to followed users
     *
     * @param user user to be followed
     */
    public void addToFollowedUsers(final User user) {
        this.followedUsers.add(user);
    }
}
