package com.matejko.wownetwork.service;

import com.matejko.wownetwork.exceptions.NoEntityExistsException;
import com.matejko.wownetwork.persistance.entity.Post;
import com.matejko.wownetwork.persistance.repository.PostRepository;
import com.matejko.wownetwork.persistance.repository.UserRepository;
import com.matejko.wownetwork.shared.mapper.PostMapper;
import com.matejko.wownetwork.shared.model.PostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.matejko.wownetwork.service.TestHelper.postEntity;
import static com.matejko.wownetwork.service.TestHelper.userEntity;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PostServiceTest {
    private PostService postService;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private PostMapper postMapper;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        postRepository = mock(PostRepository.class);
        postMapper = spy(Mappers.getMapper(PostMapper.class));

        postService = new PostService(userRepository, postRepository, postMapper);
    }

    @DisplayName("Should create new post")
    @Test
    void createPostSuccess() throws NoEntityExistsException {
        final var entity = userEntity();
        final var nickname = entity.getNickname();

        when(userRepository.findByNickname(nickname)).thenReturn(of(entity));

        final var postMessage = UUID.randomUUID().toString();

        final var post = postService.createPost(nickname, postMessage);

        verify(userRepository, times(1)).save(entity);

        assertThat(post)
                .isNotNull()
                .extracting(PostDto::getMessage)
                .isEqualTo(postMessage);

        assertThat(entity)
                .extracting(e -> e.getPosts().size())
                .isEqualTo(1);
    }


    @DisplayName("Should throw exception on creating new post if user has not been found")
    @Test
    void createPostFailure() throws NoEntityExistsException {
        final var entity = userEntity();
        final var nickname = entity.getNickname();

        when(userRepository.findByNickname(nickname)).thenReturn(empty());


        assertThrows(NoEntityExistsException.class,
                () -> postService.createPost(nickname, UUID.randomUUID().toString()));
    }

    @Test
    void getPostsByUserNickname() {
        final var posts = IntStream.range(0, 20)
                .mapToObj(i -> postEntity()).collect(Collectors.toUnmodifiableList());

        when(postRepository.getPostsByUserNickname(any())).thenReturn(posts.stream());

        final var foundPosts = postService.getPostsByUserNickname(UUID.randomUUID().toString());

        assertThat(foundPosts)
                .hasSize(posts.size())
                .flatExtracting(PostDto::getId)
                .containsExactlyInAnyOrderElementsOf(posts.stream().map(Post::getId).collect(Collectors.toUnmodifiableList()));

        verify(postMapper, times(posts.size())).toDto(any());
    }

    @Test
    void getTimeline() {
        final var posts = IntStream.range(0, 20)
                .mapToObj(i -> postEntity()).collect(Collectors.toUnmodifiableList());

        when(postRepository.getPostsOfFollowedUsersByUserNickname(any())).thenReturn(posts.stream());

        final var foundPosts = postService.getTimelineByUserNickname(UUID.randomUUID().toString());

        assertThat(foundPosts)
                .hasSize(posts.size())
                .flatExtracting(PostDto::getId)
                .containsExactlyInAnyOrderElementsOf(posts.stream().map(Post::getId).collect(Collectors.toUnmodifiableList()));

        verify(postMapper, times(posts.size())).toDto(any());
    }
}