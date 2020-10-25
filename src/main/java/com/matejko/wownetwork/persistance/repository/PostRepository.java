package com.matejko.wownetwork.persistance.repository;

import com.matejko.wownetwork.persistance.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface PostRepository extends JpaRepository<Post, String> {
    @Query("select p from User u join u.posts p where u.nickname = :nickname order by p.postDate desc")
    Stream<Post> getPostsByUserNickname(@Param("nickname") final String nickname);

    @Query("select p from User follower join follower.followedUsers followed join followed.posts p " +
            "where follower.nickname = :nickname " +
            "order by p.postDate desc")
    Stream<Post> getPostsOfFollowedUsersByUserNickname(@Param("nickname") final String nickname);
}
