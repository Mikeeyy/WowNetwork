package com.matejko.wownetwork.service;

import com.matejko.wownetwork.exceptions.NoEntityExistsException;
import com.matejko.wownetwork.persistance.entity.Post;
import com.matejko.wownetwork.persistance.entity.User;
import com.matejko.wownetwork.persistance.repository.PostRepository;
import com.matejko.wownetwork.persistance.repository.UserRepository;
import com.matejko.wownetwork.shared.mapper.PostMapper;
import com.matejko.wownetwork.shared.model.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service used for operations on {@link Post}
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    /**
     * Creates a new post and attaches it to the given user
     *
     * @param userNickname nickname of post's creator
     * @param postMessage  post's message
     * @return model of saved post
     * @throws NoEntityExistsException if the user does not exist
     */
    public PostDto createPost(final String userNickname, final String postMessage) throws NoEntityExistsException {
        final User userEntity = userRepository.findByNickname(userNickname)
                .orElseThrow(() -> new NoEntityExistsException(String.format("No entity for user [%s]", userNickname)));

        final Post post = new Post();
        post.setMessage(postMessage);
        post.setPostDate(new Date());

        userEntity.addToPosts(post);

        postRepository.save(post);
        userRepository.save(userEntity);

        return postMapper.toDto(post);
    }

    /**
     * Gets all of the posts posted by given user
     *
     * @param userNickname name of the post creator
     * @return list of the posts posted by given user
     */
    public List<PostDto> getPostsByUserNickname(final String userNickname) {
        return postRepository.getPostsByUserNickname(userNickname)
                .map(postMapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Gets all of the posts posted by users that are followed by given user
     *
     * @param userNickname name of the user-follower
     * @return list of the posts posted by users that are followed by given user
     */
    public List<PostDto> getTimelineByUserNickname(final String userNickname) {
        return postRepository.getPostsOfFollowedUsersByUserNickname(userNickname)
                .map(postMapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }
}
