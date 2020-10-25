package com.matejko.wownetwork.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matejko.wownetwork.api.model.NewFollowingRequest;
import com.matejko.wownetwork.api.model.NewPostRequest;
import com.matejko.wownetwork.shared.model.PostDto;
import com.matejko.wownetwork.shared.model.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EndToEndTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should create new user and a post")
    void createNewPost() throws Exception {
        final PostDto post = executeCreateNewPost("Ronny", "Lorem ipsum");

        assertThat(post)
                .extracting(PostDto::getMessage)
                .isEqualTo("Lorem ipsum");
    }

    @Test
    @DisplayName("Should get user's wall")
    void getUsersWall() throws Exception {
        final PostDto post1 = executeCreateNewPost("Adam", "1");
        final PostDto post2 = executeCreateNewPost("Adam", "2");
        final PostDto post3 = executeCreateNewPost("Bogdan", "3");

        final var adamWall = executeGetWall("Adam");
        final var bogdanWall = executeGetWall("Bogdan");
        final var jackWall = executeGetWall("Jack");

        assertThat(adamWall)
                .hasSize(2)
                .flatExtracting(PostDto::getMessage)
                .containsExactly("2", "1");

        assertThat(bogdanWall)
                .hasSize(1)
                .flatExtracting(PostDto::getMessage)
                .containsExactly("3");

        assertThat(jackWall)
                .isEmpty();
    }

    @Test
    @DisplayName("Should get wall of followed users")
    void getTimeline() throws Exception {
        executeCreateNewPost("User1", "1");
        executeCreateNewPost("User1", "2");
        executeCreateNewPost("User1", "3");

        executeCreateNewPost("User2", "4");
        executeCreateNewPost("User2", "5");

        executeCreateNewPost("User3", "6");

        // not yet followed, should be empty
        assertThat(executeGetTimeline("User1"))
                .isEmpty();
        assertThat(executeGetTimeline("User2"))
                .isEmpty();
        assertThat(executeGetTimeline("User3"))
                .isEmpty();

        // testing following
        executeFollow("User1", "User2");
        assertThat(executeGetTimeline("User1"))
                .hasSize(2)
                .extracting(PostDto::getMessage)
                .containsExactly("5", "4");
        assertThat(executeGetTimeline("User2"))
                .isEmpty();
        assertThat(executeGetTimeline("User3"))
                .isEmpty();


        // testing two followings
        executeFollow("User1", "User3");
        assertThat(executeGetTimeline("User1"))
                .hasSize(3)
                .extracting(PostDto::getMessage)
                .containsExactly("6", "5", "4");
        assertThat(executeGetTimeline("User2"))
                .isEmpty();
        assertThat(executeGetTimeline("User3"))
                .isEmpty();

        // testing circular following
        executeFollow("User2", "User1");
        assertThat(executeGetTimeline("User1"))
                .hasSize(3)
                .extracting(PostDto::getMessage)
                .containsExactly("6", "5", "4");
        assertThat(executeGetTimeline("User2"))
                .hasSize(3)
                .extracting(PostDto::getMessage)
                .containsExactly("3", "2", "1");
        assertThat(executeGetTimeline("User3"))
                .isEmpty();
    }


    private PostDto executeCreateNewPost(final String user, final String post) throws Exception {
        final NewPostRequest request = new NewPostRequest();
        request.setPostMessage(post);
        request.setUserNickname(user);

        final var content = mockMvc.perform(post("/post")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return objectMapper.readValue(content, PostDto.class);
    }

    private List<PostDto> executeGetWall(final String user) throws Exception {
        final var content = mockMvc.perform(get("/post/wall/" + user))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(content, new PostListTypeReference());
    }

    private List<PostDto> executeGetTimeline(final String user) throws Exception {
        final var content = mockMvc.perform(get("/post/timeline/" + user))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(content, new PostListTypeReference());
    }

    private UserDto executeFollow(final String user1, final String user2) throws Exception {
        final NewFollowingRequest request = new NewFollowingRequest();
        request.setFollowingUserName(user1);
        request.setToBeFollowedUserName(user2);

        final var content = mockMvc.perform(post("/user/follow")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return objectMapper.readValue(content, UserDto.class);
    }

    private static class PostListTypeReference extends TypeReference<List<PostDto>> {

    }
}